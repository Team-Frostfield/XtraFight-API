package team.frostfield.xtrafight.compat.plugin;

import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.events.bus.ForgeBus;

public interface CompatibilityPlugin {
    // The target for the compatibility, if not present, it will not initialize the plugin
    String getTargetModID();

    // ONLY runs on the client side if the compatibility is client-side
    boolean isClientOnly();

    void onInitialize(ForgeBus forgeBusImpl, ModBus modBusImpl);
}
