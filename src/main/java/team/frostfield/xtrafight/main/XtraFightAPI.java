package team.frostfield.xtrafight.main;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.frostfield.xtrafight.events.bus.ModBus;

public sealed interface XtraFightAPI permits XtraFightAPIMod {
    String MOD_ID = "xtrafight";
    String MOD_NAME = "XtraFight-API";
    Logger LOGGER = LogManager.getLogger(MOD_NAME);

    static XtraFightAPI getInstance() {
        return XtraFightAPIMod.instance;
    }

    ModContainer getModContainer();

    ModBus getModBus();

    boolean isDeveloperEnvironment();

    ResourceLocation identifier(String path);

}
