package com.shark.ee;

import com.shark.ee.util.EmcCalculator;
import com.shark.ee.util.EmcLoader;
import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.resource.ResourceManagerHelper;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

public class EquivalentExchange implements ModInitializer
{
	@Entrypoint.Namespace
	public static Namespace NAMESPACE;

	@Entrypoint.Logger
	public static Logger LOGGER;


	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new EmcLoader());
	}
}
