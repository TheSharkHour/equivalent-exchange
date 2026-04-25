package com.shark.ee.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Namespace;

/**
 * @author TheSharkHour
 * @since 04/15/2026 <br>
 * Listener event class for Item registration.S&S&
 */
@SuppressWarnings("unused")
public class ItemListener {

    public static Item LOW_COVALENCE_DUST;
    public static Item MEDIUM_COVALENCE_DUST;
    public static Item HIGH_COVALENCE_DUST;

    public static Item PHILOSOPHERS_STONE;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        LOW_COVALENCE_DUST = new TemplateItem(NAMESPACE.id("low_covalence_dust"))
                .setTranslationKey(NAMESPACE, "low_covalence_dust");
        MEDIUM_COVALENCE_DUST = new TemplateItem(NAMESPACE.id("medium_covalence_dust"))
                .setTranslationKey(NAMESPACE, "medium_covalence_dust");
        HIGH_COVALENCE_DUST = new TemplateItem(NAMESPACE.id("high_covalence_dust"))
                .setTranslationKey(NAMESPACE, "high_covalence_dust");

        PHILOSOPHERS_STONE = new TemplateItem(NAMESPACE.id("philosophers_stone"))
                .setTranslationKey(NAMESPACE, "philosophers_stone")
                .setMaxCount(1);
    }
}
