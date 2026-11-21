package team.frostfield.xtrafight.mixins.minecraft.common;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.frostfield.xtrafight.events.mc.player.PlayerTickDispatchEvent;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            remap = false
    )
    private void injectEventHookPre(CallbackInfo ci) {
        PlayerTickDispatchEvent.Pre tickPre = new PlayerTickDispatchEvent.Pre((Player) (Object) this);
        tickPre.post();
    }

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/EventHooks;firePlayerTickPost(Lnet/minecraft/world/entity/player/Player;)V"),
            method = "tick",
            remap = false
    )
    private void injectEventHookPost(CallbackInfo ci) {
        PlayerTickDispatchEvent.Post tickPostEH = new PlayerTickDispatchEvent.Post((Player) (Object) this);
        tickPostEH.post();
    }
}
