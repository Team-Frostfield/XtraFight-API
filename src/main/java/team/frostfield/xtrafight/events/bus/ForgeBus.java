package team.frostfield.xtrafight.events.bus;

import net.minecraftforge.eventbus.api.IEventBus;
import team.frostfield.xtrafight.events.bus.impl.ForgeBusImpl;

public interface ForgeBus extends IEventBus {
    ForgeBus EVENT_BUS = new ForgeBusImpl();
}
