package team.frostfield.xtrafight.mixins.minecraft.common;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import team.frostfield.xtrafight.events.mc.player.PlayerTickDispatchEvent;

@Mixin(Player.class)
public abstract class PlayerMixin {
    private final Player self = (Player) (Object) this;
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            remap = false
    )
    private void injectEventHookPre(CallbackInfo ci) {
        PlayerTickDispatchEvent.Pre tickPre = new PlayerTickDispatchEvent.Pre((Player) (Object) this);
        MinecraftDispatchableEvents.Player.TICK_PRE.postEventWithParams(tickPre, params -> params.onPlayerTick(this.self));
    }

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/EventHooks;firePlayerTickPost(Lnet/minecraft/world/entity/player/Player;)V"),
            method = "tick",
            remap = false
    )
    private void injectEventHookPost(CallbackInfo ci) {
        PlayerTickDispatchEvent.Post tickPost = new PlayerTickDispatchEvent.Post((Player) (Object) this);
        MinecraftDispatchableEvents.Player.TICK_POST.postEventWithParams(tickPost, params -> params.onPlayerTick(this.self));
    }
}
