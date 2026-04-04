package com.shark.ee;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

public class EquivalentExchangeCore
{
	@Entrypoint.Namespace
	public static Namespace NAMESPACE;

	@Entrypoint.Logger
	public static Logger LOGGER;
}
