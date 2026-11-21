package team.frostfield.xtrafight.compat.plugin;

import net.neoforged.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.frostfield.xtrafight.cls.maps.BasicHashMap;
import team.frostfield.xtrafight.cls.maps.BasicMap;
import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.events.bus.impl.NeoBusImpl;
import team.frostfield.xtrafight.utils.EnvironmentHelper;
import team.frostfield.xtrafight.utils.code.AccessType;
import team.frostfield.xtrafight.utils.code.CodeUtils;

public final class CompatibilityPluginsManager {
    static final Logger LOGGER = LogManager.getLogger(CompatibilityPluginsManager.class.getSimpleName());
    private static final BasicMap<String, CompatibilityPlugin> PLUGIN_BY_MOD_ID = new BasicHashMap<>();

    @SafeVarargs
    public static void registerPlugins(@NotNull ModBus modBus, Class<? extends CompatibilityPlugin>... pluginClasses) {
        for (Class<? extends CompatibilityPlugin> pluginClass : pluginClasses) {
            String simpleName = pluginClass.getSimpleName();
            try {
                CompatibilityPlugin plugin = CodeUtils.newEmptyInstance(pluginClass, AccessType.DECLARED);
                if (plugin.getTargetModID() == null) {
                    LOGGER.error("Mod-ID for plugin {} can NOT be null!", simpleName);
                } else if (plugin.getTargetModID().isBlank()) {
                    LOGGER.error("Mod-ID for plugin {} is blank!", simpleName);
                }
                if (ModList.get().isLoaded(plugin.getTargetModID())) {
                    if (plugin.isClientOnly()) {
                        if (!EnvironmentHelper.getEnvironment().isClient()) {
                            LOGGER.info("Skipped plugin: [{}], not a client environment!", pluginClass);
                            continue;
                        }
                    }
                    plugin.onInitialize(NeoBusImpl.EVENT_BUS, modBus);
                }

                PLUGIN_BY_MOD_ID.put(plugin.getTargetModID(), plugin);
                LOGGER.info("Initialized Compatibility Plugin: [{}]", simpleName);
            } catch (Exception e) {
                LOGGER.error("Failed to initialize plugin [{}], {}", pluginClass.getName(), e);
                e.printStackTrace();
            }
        }
    }

    public static boolean isPluginLoadedFor(String modID) {
        return PLUGIN_BY_MOD_ID.get(modID) != null;
    }

    public static CompatibilityPlugin getPluginByID(String modID) {
        return PLUGIN_BY_MOD_ID.get(modID);
    }
}