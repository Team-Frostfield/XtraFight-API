package team.frostfield.xtrafight.mixins.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.frostfield.xtrafight.events.mc.MinecraftDispatchableEvents;
import team.frostfield.xtrafight.events.mc.client.ClientInitializeDispatchEvent;
import team.frostfield.xtrafight.events.mc.client.ClientInitializeDispatchEvent.Post;
import team.frostfield.xtrafight.events.mc.client.ClientInitializeDispatchEvent.Pre;
import team.frostfield.xtrafight.events.mc.client.ClientTickDispatchEvent;
import team.frostfield.xtrafight.events.mc.client.WorldLoadDispatchEvent;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Unique
    private final Minecraft self = (Minecraft) (Object) this;
    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/GameProfileCache;setUsesAuthentication(Z)V"),
            method = "doWorldLoad",
            remap = false
    )

    public void injectCWDE(LevelStorageAccess levelStorage, PackRepository packRepository, WorldStem worldStem, boolean newWorld, CallbackInfo ci) {
        WorldLoadDispatchEvent.Initialize worldInitEventHook = new WorldLoadDispatchEvent.Initialize(levelStorage, packRepository, worldStem, newWorld);
        MinecraftDispatchableEvents.Client.WORLD_INIT.postEventWithParams(worldInitEventHook, params -> params.onWorldLoading(levelStorage, packRepository, worldStem, newWorld));
    }

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;reload()V"),
            method = "<init>",
            remap = false
    )
    public void injectCIDEPre(GameConfig gameConfig, CallbackInfo ci) {
        ClientInitializeDispatchEvent.Pre clientInitDEPre = new Pre(this.self);
        MinecraftDispatchableEvents.Client.START_PRE.postEventWithParams(clientInitDEPre, params -> params.onInitialize(this.self));
    }

    @Inject(
            at = @At("TAIL"),
            method = "<init>",
            remap = false
    )
    public void injectCIDEPost(GameConfig gameConfig, CallbackInfo ci) {
        ClientInitializeDispatchEvent.Post clientInitDEPost = new Post(this.self);
        MinecraftDispatchableEvents.Client.START_POST.postEventWithParams(clientInitDEPost, param -> param.onInitialize(this.self));

    }

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;fireClientTickPre()V"),
            method = "tick",
            remap = false
    )

    public void injectCTDE(CallbackInfo ci) {
        ClientTickDispatchEvent clientTickDE = new ClientTickDispatchEvent(this.self);
        MinecraftDispatchableEvents.Client.TICK.postEventWithParams(clientTickDE, params -> params.onTick(this.self));
    }


}
