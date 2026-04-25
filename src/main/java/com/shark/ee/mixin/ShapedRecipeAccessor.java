package com.shark.ee.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author TheSharkHour
 * @since 4/16/2026 <br>
 * Accessor mixin for recipe inputs.
 */
@Mixin(value = ShapedRecipe.class)
public interface ShapedRecipeAccessor {
    @Accessor("input")
    ItemStack[] getInput();
}
