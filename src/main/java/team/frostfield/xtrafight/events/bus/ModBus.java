package team.frostfield.xtrafight.events.bus;

import net.minecraftforge.eventbus.api.IEventBus;
import team.frostfield.xtrafight.events.bus.impl.ModBusImpl;

public interface ModBus extends IEventBus {
    static ModBus from(IEventBus modBus) {
        return new ModBusImpl(modBus);
    }

    static ModBus active() {
        return new ModBusImpl();
    }

    static ModBus fromID(String modID) {
        return new ModBusImpl(modID);
    }

    String getModID();


}
