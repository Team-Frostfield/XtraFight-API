package team.frostfield.xtrafight.mixins.minecraft.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import team.frostfield.xtrafight.events.mc.entity.EntitySummonDispatchEvent;
import team.frostfield.xtrafight.events.mc.player.PlayerJoinDispatchEvent;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    private final ClientLevel self = (ClientLevel) (Object) this;

    @Inject(
            at = @At("HEAD"),
            method = "addEntity",
            remap = false
    )
    public void injectJoinEventHook(Entity entity, CallbackInfo ci) {
        // we put it in an "if-else" block, so we avoid firing both events if the entity was an instance of a player
        if (entity instanceof Player player) {
            PlayerJoinDispatchEvent.Client playerJoinClientEH = new PlayerJoinDispatchEvent.Client(player, this.self);
            MinecraftDispatchableEvents.Player.JOIN_CLIENT.postWithParams(playerJoinClientEH, params -> params.onPlayerJoin(player, this.self));
        } else {
            EntitySummonDispatchEvent.Client entitySummonClientEH = new EntitySummonDispatchEvent.Client(entity, this.self);
            MinecraftDispatchableEvents.Entity.SUMMON_CLIENT.postWithParams(entitySummonClientEH, params -> params.onEntitySummon(entity, this.self));
        }
    }
}
