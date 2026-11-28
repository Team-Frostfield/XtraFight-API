package team.frostfield.xtrafight.mixins.forge.server;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import team.frostfield.xtrafight.events.mc.server.ServerInitializeDispatchEvent;
import team.frostfield.xtrafight.events.mc.server.ServerStopDispatchEvent;

@Mixin(ServerLifecycleHooks.class)
public class ServerLifecycleHooksMixin {

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/util/LogicalSidedProvider;setServer(Ljava/util/function/Supplier;)V"),
            method = "handleServerAboutToStart",
            remap = false
    )
    private static void injectEventHookPre(MinecraftServer server, CallbackInfoReturnable<Boolean> cir) {
        ServerInitializeDispatchEvent.Pre serverInitPre = new ServerInitializeDispatchEvent.Pre(server);
        MinecraftDispatchableEvents.Server.START_PRE.postWithParams(serverInitPre, params -> params.onInitialize(server));
    }
    @Inject(
            at = @At(value = "HEAD"),
            method = "handleServerStarted",
            remap = false
    )
    private static void injectEventHookPost(MinecraftServer server, CallbackInfo ci) {
        ServerInitializeDispatchEvent.Post serverInitPost = new ServerInitializeDispatchEvent.Post(server);
        MinecraftDispatchableEvents.Server.START_POST.postWithParams(serverInitPost, params -> params.onInitialize(server));
    }

    @Inject(
            at = @At("HEAD"),
            method = "handleServerStopped",
            remap = false
    )
    private static void injectEventHookStop(MinecraftServer server, CallbackInfo callbackInfo) {
        ServerStopDispatchEvent stopEvent = new ServerStopDispatchEvent(server);
        MinecraftDispatchableEvents.Server.STOP.postWithParams(stopEvent, params -> params.onServerStop(server));
    }

}
