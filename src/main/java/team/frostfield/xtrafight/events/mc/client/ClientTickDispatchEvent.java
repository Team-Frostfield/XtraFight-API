package team.frostfield.xtrafight.events.mc.client;

import net.minecraft.client.Minecraft;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;

public class ClientTickDispatchEvent extends DispatchEvent implements ParameterizedEvent {

    private final Minecraft minecraft;

    public ClientTickDispatchEvent(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @FunctionalInterface
    public interface ClientTickParams extends EventParameters {
        void onTick(Minecraft mc);
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }


}
