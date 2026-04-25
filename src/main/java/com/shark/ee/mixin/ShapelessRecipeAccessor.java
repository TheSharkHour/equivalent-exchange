package com.shark.ee.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author TheSharkHour
 * @since 4/16/2026 <br>
 * Accessor mixin for recipe inputs.
 */
@Mixin(value = ShapelessRecipe.class)
public interface ShapelessRecipeAccessor {
    @Accessor("input")
    List<ItemStack> getInput();
}
