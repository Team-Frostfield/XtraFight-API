package team.frostfield.xtrafight.events.mc.server;

import net.minecraft.server.MinecraftServer;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;

public class ServerStopDispatchEvent extends DispatchEvent implements ParameterizedEvent {
    private final MinecraftServer server;

    public ServerStopDispatchEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    @FunctionalInterface
    public interface ServerStopParams extends EventParameters {
        void onServerStop(MinecraftServer server);
    }

}
