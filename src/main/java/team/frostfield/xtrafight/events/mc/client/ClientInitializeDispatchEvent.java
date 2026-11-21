package team.frostfield.xtrafight.events.mc.client;

import team.frostfield.xtrafight.events.dispatch.IDispatchableEvent;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameterize;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.marks.HybridEvent;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import net.minecraft.client.Minecraft;

@HybridEvent
public abstract class ClientInitializeDispatchEvent extends DispatchEvent implements IDispatchableEvent<ClientInitializeDispatchEvent> {
    @Override
    public abstract ClientInitializeDispatchEvent post();

    protected final Minecraft minecraft;

    public ClientInitializeDispatchEvent(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    public static class Pre extends ClientInitializeDispatchEvent {

        public Pre(Minecraft minecraft) {
            super(minecraft);
        }

        @Override
        public ClientInitializeDispatchEvent post() {
            return MinecraftDispatchableEvents.Client.CLIENT_START_PRE.postEvent(this);
        }
    }

    public static class Post extends ClientInitializeDispatchEvent implements ParameterizedEvent {

        public Post(Minecraft minecraft) {
            super(minecraft);
        }

        @Override
        public ClientInitializeDispatchEvent post() {
            return MinecraftDispatchableEvents.Client.CLIENT_START_POST.postEvent(this);
        }

        public interface ClientStartPostParams extends EventParameterize<Post> {
            void onStart();
        }
    }
}
