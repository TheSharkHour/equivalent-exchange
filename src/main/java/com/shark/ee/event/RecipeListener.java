package com.shark.ee.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author TheSharkHour
 * @since 4/20/2026 <br>
 * Listener event class for recipe registration.
 */
public class RecipeListener {

    @EventListener
    public void registerRecipes(@NotNull RecipeRegisterEvent event) {
        Identifier type = event.recipeId;
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED.type()) {
            registerShapedRecipes();
        }

        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS.type()) {
            registerShapelessRecipes();
        }
    }

    private void registerShapelessRecipes() {
        CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.LOW_COVALENCE_DUST, 40),
                Block.COBBLESTONE,
                Block.COBBLESTONE,
                Block.COBBLESTONE,
                Block.COBBLESTONE,
                Block.COBBLESTONE,
                Block.COBBLESTONE,
                Block.COBBLESTONE,
                Block.COBBLESTONE,
                new ItemStack(Item.COAL, 1, 1)
        );

        CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.MEDIUM_COVALENCE_DUST, 40),
                Item.IRON_INGOT,
                Item.REDSTONE
        );

        CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.HIGH_COVALENCE_DUST, 40),
                Item.DIAMOND,
                new ItemStack(Item.COAL, 1, 0)
        );
    }

    private void registerShapedRecipes() {
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.PHILOSOPHERS_STONE),
                "121", "232", "121",
                '1', Item.REDSTONE,
                '2', Item.GLOWSTONE_DUST,
                '3', Item.DIAMOND
        );

        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.PHILOSOPHERS_STONE),
                "121", "232", "121",
                '1', Item.GLOWSTONE_DUST,
                '2', Item.REDSTONE,
                '3', Item.DIAMOND
        );
    }
}
