package team.frostfield.xtrafight.mixins.neoforge.server;

import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import team.frostfield.xtrafight.events.mc.server.ServerInitializeDispatchEvent;
import team.frostfield.xtrafight.events.mc.server.ServerStopDispatchEvent;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLifecycleHooks.class)
public class ServerLifecycleHooksMixin {

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/LogicalSidedProvider;setServer(Ljava/util/function/Supplier;)V"),
            method = "handleServerAboutToStart",
            remap = false
    )
    private static void injectEventHookPre(MinecraftServer server, CallbackInfo ci) {
        ServerInitializeDispatchEvent.Pre serverInitPre = new ServerInitializeDispatchEvent.Pre(server);
        MinecraftDispatchableEvents.Server.START_PRE.postEventWithParams(serverInitPre, params -> params.onInitialize(server));
    }
    @Inject(
            at = @At(value = "HEAD"),
            method = "handleServerStarted",
            remap = false
    )
    private static void injectEventHookPost(MinecraftServer server, CallbackInfo ci) {
        ServerInitializeDispatchEvent.Post serverInitPost = new ServerInitializeDispatchEvent.Post(server);
        MinecraftDispatchableEvents.Server.START_POST.postEventWithParams(serverInitPost, params -> params.onInitialize(server));
    }

    @Inject(
            at = @At("HEAD"),
            method = "handleServerStopped",
            remap = false
    )
    private static void injectEventHookStop(MinecraftServer server, CallbackInfo callbackInfo) {
        ServerStopDispatchEvent stopEvent = new ServerStopDispatchEvent(server);
        MinecraftDispatchableEvents.Server.STOP.postEventWithParams(stopEvent, params -> params.onServerStop(server));
    }

}
