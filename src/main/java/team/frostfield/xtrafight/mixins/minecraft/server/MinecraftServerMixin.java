package team.frostfield.xtrafight.mixins.minecraft.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import team.frostfield.xtrafight.events.mc.server.ServerTickDispatchEvent;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    private final MinecraftServer self = (MinecraftServer) (Object) this;
    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/EventHooks;fireServerTickPre(Ljava/util/function/BooleanSupplier;Lnet/minecraft/server/MinecraftServer;)V"),
            method = "tickServer",
            remap = false
    )

    public void injectTickEventHook(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ServerTickDispatchEvent tickEventHook = new ServerTickDispatchEvent(this.self, hasTimeLeft);
        MinecraftDispatchableEvents.Server.TICK.postWithParams(tickEventHook, params -> params.onServerTick(this.self));
    }
}
