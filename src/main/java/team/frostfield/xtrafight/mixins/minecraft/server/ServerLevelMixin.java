package team.frostfield.xtrafight.mixins.minecraft.server;

import org.spongepowered.asm.mixin.Unique;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import team.frostfield.xtrafight.events.mc.entity.EntitySummonDispatchEvent;
import team.frostfield.xtrafight.events.mc.player.PlayerJoinDispatchEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    // every event here is only posted if it was in a dedicated server
    @Shadow @Final private MinecraftServer server;
    @Unique
    private final ServerLevel self = (ServerLevel) (Object) this;

    @Inject(
            at = @At("HEAD"),
            method = "addEntity",
            remap = false
    )
    public void injectSummonEventHook(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!this.server.isDedicatedServer()) return;
        EntitySummonDispatchEvent.Server serverSummonEH = new EntitySummonDispatchEvent.Server(entity, this.self);
        MinecraftDispatchableEvents.Entity.SUMMON_SERVER.postWithParams(serverSummonEH, params -> params.onEntitySummon(entity, this.self));
    }

    @Inject(
            at = @At("HEAD"),
            method = "addPlayer",
            remap = false
    )
    public void injectPlayerJoinEventHook(ServerPlayer player, CallbackInfo ci) {
        if (!this.server.isDedicatedServer()) return;
        PlayerJoinDispatchEvent.Server serverPlayerJoinEH = new PlayerJoinDispatchEvent.Server(player, this.self);
        MinecraftDispatchableEvents.Player.JOIN_SERVER.postWithParams(serverPlayerJoinEH, params -> params.onPlayerJoin(player, this.self));
    }

}
