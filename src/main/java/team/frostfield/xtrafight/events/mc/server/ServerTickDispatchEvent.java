package team.frostfield.xtrafight.events.mc.server;

import net.minecraft.server.MinecraftServer;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;

import java.util.function.BooleanSupplier;

public class ServerTickDispatchEvent extends DispatchEvent implements ParameterizedEvent {
    private final MinecraftServer server;
    private final BooleanSupplier boolSupplier;

    public ServerTickDispatchEvent(MinecraftServer server, BooleanSupplier hasTimeLeft) {
        this.server = server;
        this.boolSupplier = hasTimeLeft;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public boolean hasTimeLeft() {
        return this.boolSupplier.getAsBoolean();
    }

    @FunctionalInterface
    public interface ServerTickParams extends EventParameters {
        void onServerTick(MinecraftServer server);
    }

}
