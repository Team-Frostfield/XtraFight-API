package team.frostfield.xtrafight.events.mc.server;

import net.minecraft.server.MinecraftServer;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;

public abstract class ServerInitializeDispatchEvent extends DispatchEvent implements ParameterizedEvent {

    protected final MinecraftServer server;

    public ServerInitializeDispatchEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    @FunctionalInterface
    public interface ServerInitParams extends EventParameters {
        void onInitialize(MinecraftServer server);
    }

    public static class Pre extends ServerInitializeDispatchEvent {
        public Pre(MinecraftServer server) {
            super(server);
        }

    }

    public static class Post extends ServerInitializeDispatchEvent {
        public Post(MinecraftServer server) {
            super(server);
        }

    }

}
