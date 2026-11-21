package team.frostfield.xtrafight.events.bus;

import net.neoforged.bus.api.IEventBus;
import team.frostfield.xtrafight.events.bus.impl.NeoBusImpl;

public interface NeoBus extends IEventBus {
    NeoBus EVENT_BUS = new NeoBusImpl();
}
