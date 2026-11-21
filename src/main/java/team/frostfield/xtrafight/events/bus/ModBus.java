package team.frostfield.xtrafight.events.bus;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import team.frostfield.xtrafight.events.bus.impl.ModBusImpl;

public interface ModBus extends IEventBus {
    static ModBus from(IEventBus modBus) {
        return new ModBusImpl(modBus);
    }

    static ModBus byID(String modID) {
        return new ModBusImpl(modID);
    }

    static ModBus active() {
        return new ModBusImpl(ModLoadingContext.get().getActiveContainer().getEventBus());
    }


}
