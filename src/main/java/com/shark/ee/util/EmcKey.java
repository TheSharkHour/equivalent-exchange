package com.shark.ee.util;

import net.modificationstation.stationapi.api.util.Identifier;

/**
 * @author TheSharkHour
 * @since 04/15/2026 <br>
 * A record for EMC Keys. Used by EmcRegistry.
 *
 * @param id Mod/Game Identifier; i.e. "Minecraft:Cobblestone"
 * @param meta The item metadata
 */
public record EmcKey(Identifier id, int meta) {
    public static EmcKey of(Identifier id, int meta) {
        return new EmcKey(id, meta);
    }

    public static EmcKey of(Identifier id) {
        return new EmcKey(id, 0);
    }
}
