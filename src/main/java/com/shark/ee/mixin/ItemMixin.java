package com.shark.ee.mixin;

import com.shark.ee.util.EmcRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author TheSharkHour
 * @since 4/15/2026 <br>
 * Item mixin for tooltip EMC display.
 */
@Mixin(value = Item.class)
public abstract class ItemMixin implements CustomTooltipProvider {

    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        Identifier id = ItemRegistry.INSTANCE.getId(stack.getItem());

        if (id == null) return new String[]{ originalTooltip };

        long emc = EmcRegistry.get(id, stack.getDamage());
        if (emc > 0) {
            long stackEmc = emc * stack.count;
            if (stack.count > 1) {
                return new String[]{
                        originalTooltip,
                        "§eEMC§f: " + emc,
                        "§eStack EMC§f: " + stackEmc
                };
            }

            return new String[]{
                    originalTooltip,
                    "§eEMC: §f" + emc
            };
        }

        return new String[]{ originalTooltip };
    }
}
