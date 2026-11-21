package team.frostfield.xtrafight.compat.plugin;

import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.events.bus.NeoBus;

public class ExamplePlugin implements CompatibilityPlugin {

    @Override
    public String getTargetModID() {
        return "";
    }

    @Override
    public boolean isClientOnly() {
        return false;
    }

    @Override
    public void onInitialize(NeoBus neoBusImpl, ModBus modBusImpl) {}
}
