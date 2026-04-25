package com.shark.ee.util;

import com.google.gson.*;
import com.shark.ee.EquivalentExchange;
import net.modificationstation.stationapi.api.resource.IdentifiableResourceReloadListener;
import net.modificationstation.stationapi.api.resource.JsonDataLoader;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.util.Map;

/**
 * @author TheSharkHour
 * @since 4/15/2026 <br>
 * EMC JSON Loader from resources.
 */
public class EmcLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();

    public EmcLoader() {
        super(GSON, "emc");
    }

    @Override
    public Identifier getId() {
        return EquivalentExchange.NAMESPACE.id("emc_loader");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> data, ResourceManager manager, Profiler profiler) {
        EmcRegistry.clear();

        for (Map.Entry<Identifier, JsonElement> entry : data.entrySet()) {
            JsonObject json = entry.getValue().getAsJsonObject();
            JsonArray values = json.getAsJsonArray("values");

            for (JsonElement element : values) {
                JsonObject obj = element.getAsJsonObject();
                Identifier id = Identifier.of(obj.get("id").getAsString());
                int meta = obj.has("meta") ? obj.get("meta").getAsInt() : 0;
                long emc = obj.get("emc").getAsLong();

                if (emc == 0) {
                    EmcRegistry.blacklist(id, meta);
                    continue;
                }

                if (EmcRegistry.has(id, meta))
                    EquivalentExchange.LOGGER.warn("EMC value for {}:{} is being overwritten!", id, meta);

                EmcRegistry.register(id, meta, emc);
            }
        }

        EmcCalculator.calculateAndRegister();

        EquivalentExchange.LOGGER.info("Loaded {} EMC values.", EmcRegistry.size());
    }
}
