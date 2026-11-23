package team.frostfield.xtrafight.events.mc.client;

import net.minecraft.client.Minecraft;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.marks.HybridEvent;

@HybridEvent
public abstract class ClientInitializeDispatchEvent extends DispatchEvent implements ParameterizedEvent {

    protected final Minecraft minecraft;

    public ClientInitializeDispatchEvent(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    @FunctionalInterface
    public interface ClientInitParams extends EventParameters {
        void onInitialize(Minecraft mc);
    }

    public static class Pre extends ClientInitializeDispatchEvent {
        public Pre(Minecraft minecraft) {
            super(minecraft);
        }
    }

    public static class Post extends ClientInitializeDispatchEvent implements ParameterizedEvent {

        public Post(Minecraft minecraft) {
            super(minecraft);
        }
    }
}
