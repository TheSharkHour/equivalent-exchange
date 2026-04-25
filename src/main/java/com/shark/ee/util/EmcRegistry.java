package com.shark.ee.util;

import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 * @author TheSharkHour
 * @since 04/15/2026 <br>
 * Registration class for EMC values.
 */
public class EmcRegistry {
    private static final Map<EmcKey, Long> EMC_VALUES = new HashMap<>();
    private static final Set<EmcKey> EMC_BLACKLIST = new HashSet<>();

    /**
     * Register method for items.
     *
     * @param id The item id
     * @param meta The item metadata
     * @param value The EMC value
     */
    public static void register(Identifier id, int meta, long value) {
        EMC_VALUES.put(EmcKey.of(id, meta), value);
    }

    /**
     * Register method for items. <br>
     * Has convenience for metadata 0.
     *
     * @param id The item id
     * @param value The EMC value
     */
    public static void register(Identifier id, long value) {
        register(id, 0, value);
    }

    /**
     * Getter method for EMC values.
     *
     * @param id The item id
     * @param meta The item Metadata
     * @return the EMC value of the supplied ID and meta.
     */
    public static long get(Identifier id, int meta) {
        return EMC_VALUES.getOrDefault(EmcKey.of(id, meta), 0L);
    }

    /**
     * Getter method for EMC values. <br>
     * Has convenience for metadata 0.
     *
     * @param id The item id
     * @return the EMC value for the supplied ID and meta.
     */
    public static long get(Identifier id) {
        return get(id, 0);
    }

    /**
     * Whether the EMC values map contains an ID and metadata.
     *
     * @param id The item id
     * @param meta The item metadata
     * @return whether the map contains the supplied ID and metadata.
     */
    public static boolean has(Identifier id, int meta) {
        return EMC_VALUES.containsKey(EmcKey.of(id, meta));
    }

    /**
     * Whether the EMC values map contains an ID. <br>
     * Has convenience for metadata 0.
     *
     * @param id The item id
     * @return whether the map contains the supplied ID.
     */
    public static boolean has(Identifier id) {
        return has(id, 0);
    }

    /**
     * Clear the EMC values list.
     */
    public static void clear() {
        EMC_VALUES.clear();
        EMC_BLACKLIST.clear();
    }

    /**
     * Get the size of the EMC values list.
     * @return the size of the EMC values.
     */
    public static int size() {
        return EMC_VALUES.size();
    }


    /**
     * The EMC entries.
     * @return the EMC map as an unmodifiable set.
     */
    public static @NotNull @UnmodifiableView Set<Map.Entry<EmcKey, Long>> entries() {
        return Collections.unmodifiableSet(EMC_VALUES.entrySet());
    }

    /**
     * Blacklists an item from having an EMC value.
     * @param id The item id
     * @param meta The item metadata
     */
    public static void blacklist(Identifier id, int meta) {
        EMC_BLACKLIST.add(EmcKey.of(id, meta));
        EMC_VALUES.remove(EmcKey.of(id, meta));
    }

    /**
     * Blacklists an item from having an EMC value. <br>
     * Has convenience for metadata 0.
     *
     * @param id The item id
     */
    public static void blacklist(Identifier id) {
        blacklist(id, 0);
    }

    /**
     * Checks whether an item is blacklisted.
     * @param id The item id
     * @param meta The item metadata
     * @return whether the blacklist contains the supplied ID.
     */
    public static boolean isBlacklisted(Identifier id, int meta) {
        return EMC_BLACKLIST.contains(EmcKey.of(id, meta));
    }

    /**
     * Checks whether an item is blacklisted. <br>
     * Has convenience for metadata 0.
     *
     * @param id The item id
     * @return whether the blacklist contains the supplied ID.
     */
    public static boolean isBlacklisted(Identifier id) {
        return isBlacklisted(id, 0);
    }
}