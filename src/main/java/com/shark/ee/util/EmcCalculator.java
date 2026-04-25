package com.shark.ee.util;

import com.mojang.datafixers.util.Either;
import com.shark.ee.EquivalentExchange;
import com.shark.ee.mixin.ShapedRecipeAccessor;
import com.shark.ee.mixin.ShapelessRecipeAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TheSharkHour
 * @since 4/16/2026 <br>
 * Calculate EMC costs based on recipe.
 */
public class EmcCalculator {

    /**
     * Calculates EMC cost based on recipe. <br>
     * Doesn't overwrite pre-set values.
     */
    public static void calculateAndRegister() {
        List<?> craftingRecipes = CraftingRecipeManager.getInstance().getRecipes();

        boolean changed = true;
        while (changed) {
            changed = false;

            // Crafting Recipes
            for (Object o : craftingRecipes) {
                CraftingRecipe recipe = (CraftingRecipe) o;

                ItemStack output = recipe.getOutput();
                if (output == null) continue;

                Identifier id = ItemRegistry.INSTANCE.getId(output.getItem());
                if (id == null) continue;

                if (EmcRegistry.isBlacklisted(id, output.getDamage() == -1 ? 0 : output.getDamage())) continue;

                // Quick note! If the EMC cost of a recipe is LESS than a
                // previously registered one, it gets overwritten.
                List<Either<TagKey<Item>, ItemStack>> ingredients = getIngredients(recipe);
                long cost = calculateCost(ingredients);
                if (cost > 0) {
                    int meta = output.getDamage() == -1 ? 0 : output.getDamage();
                    long costPerItem = cost / output.count;

                    if (!EmcRegistry.has(id, meta) || costPerItem < EmcRegistry.get(id, meta)) {
                        EmcRegistry.register(id, meta, cost / output.count);
                        changed = true;
                    }
                }
            }

            // Furnace Recipes
            for (Object o : SmeltingRecipeManager.getInstance().getRecipes().entrySet()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;

                int inputId = (int) entry.getKey();
                ItemStack output = (ItemStack) entry.getValue();
                if (output == null) continue;

                Identifier outputId = ItemRegistry.INSTANCE.getId(output.getItem());
                if (outputId == null) continue;
                if (EmcRegistry.has(outputId, output.getDamage() == -1 ? 0 : output.getDamage())) continue;

                Item inputItem = ItemRegistry.INSTANCE.get(inputId);
                if (inputItem == null) continue;

                Identifier inputIdentifier = ItemRegistry.INSTANCE.getId(inputItem);
                if (inputIdentifier == null) continue;

                long inputEmc = EmcRegistry.get(inputIdentifier, 0);
                if (inputEmc == 0) continue;

                if (EmcRegistry.isBlacklisted(inputIdentifier, output.getDamage() == -1 ? 0 : output.getDamage()))
                    continue;

                int meta = output.getDamage() == -1 ? 0 : output.getDamage();
                EmcRegistry.register(outputId, meta, inputEmc / output.count);
                changed = true;
            }
        }

        EquivalentExchange.LOGGER.info("Recipe calculation complete, registry now has {} values", EmcRegistry.size());
    }

    /**
     * Helper method to calculate the cost of recipes for the final result.
     * @param ingredients List of ingredients
     * @return EMC value
     */
    private static long calculateCost(List<Either<TagKey<Item>, ItemStack>> ingredients) {
        long total = 0;

        for (Either<TagKey<Item>, ItemStack> ingredient : ingredients) {
            long emc = getEmcFromEither(ingredient);
            if (emc <= 0) return 0;
            total += emc;
        }

        return total;
    }

    /**
     * Helper to get the ingredients of recipes. <br>
     * Checks StationAPI, Crafting, and Furnace recipes.
     * @param recipe The recipe
     * @return the inputs of shaped or shapeless recipes, or null
     */
    private static List<Either<TagKey<Item>, ItemStack>> getIngredients(CraftingRecipe recipe) {
        if (recipe instanceof StationShapedRecipe station)
            return Arrays.stream(station.getGrid())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        if (recipe instanceof StationShapelessRecipe station)
            return Arrays.stream(station.getIngredients())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        if (recipe instanceof ShapedRecipe shaped)
            return Arrays.stream(((ShapedRecipeAccessor) shaped).getInput())
                    .filter(Objects::nonNull)
                    .map(Either::<TagKey<Item>, ItemStack>right)
                    .collect(Collectors.toList());

        if (recipe instanceof ShapelessRecipe shapeless)
            return ((ShapelessRecipeAccessor) shapeless).getInput().stream()
                    .filter(Objects::nonNull)
                    .map(Either::<TagKey<Item>, ItemStack>right)
                    .collect(Collectors.toList());

        return Collections.emptyList();
    }

    /**
     * Helper method to get EMC from either a Tag or an ItemStack.
     * @param either Either a TagKey or an ItemStack
     * @return EMC value
     */
    private static long getEmcFromEither(Either<TagKey<Item>, ItemStack> either) {
        if (either == null) return 0;

        // First, check the ItemStack. If one is present, get the ID and damage.
        // If the damage is below 0 (-1), set it to 0.
        if (either.right().isPresent()) {
            ItemStack stack = either.right().get();
            Identifier id = ItemRegistry.INSTANCE.getId(stack.getItem());
            if (id == null) return 0;

            return EmcRegistry.get(id, stack.getDamage() == -1 ? 0 : stack.getDamage());
        }

        // Now check if a TAG KEY is present.
        // Get the id of the tag key and check the item registry for it.
        // Also filter the EMC of the items by the MINIMUM value.
        if (either.left().isPresent()) {
            TagKey<Item> tag = either.left().get();
            return ItemRegistry.INSTANCE.getEntryList(tag)
                    .map(entries -> entries.stream()
                            .mapToLong(entry -> {
                                Identifier id = ItemRegistry.INSTANCE.getId(entry.value());
                                return id == null ? 0L : EmcRegistry.get(id, 0);
                            })
                            .filter(emc -> emc > 0)
                            .min()  // lowest EMC item in the tag
                            .orElse(0L))
                    .orElse(0L);
        }

        return 0;
    }
}
