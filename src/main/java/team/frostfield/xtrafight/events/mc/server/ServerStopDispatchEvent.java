package team.frostfield.xtrafight.events.mc.server;

import team.frostfield.xtrafight.events.dispatch.IDispatchableEvent;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStopDispatchEvent extends DispatchEvent implements IDispatchableEvent<ServerStopDispatchEvent> {
    private final MinecraftServer server;

    public ServerStopDispatchEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    @Override
    public ServerStopDispatchEvent post() {
        return MinecraftDispatchableEvents.Server.SERVER_STOP.postEvent(this);
    }
}
