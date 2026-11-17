package team.frostfield.xtrafight.events.mc.client;

import team.frostfield.xtrafight.events.dispatch.IDispatchableEvent;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import net.minecraft.client.Minecraft;

public class ClientTickDispatchEvent extends DispatchEvent implements IDispatchableEvent<ClientTickDispatchEvent> {

    private final Minecraft minecraft;

    public ClientTickDispatchEvent(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    @Override
    public ClientTickDispatchEvent post() {
        return MinecraftDispatchableEvents.Client.CLIENT_TICK.postEvent(this);
    }
}
