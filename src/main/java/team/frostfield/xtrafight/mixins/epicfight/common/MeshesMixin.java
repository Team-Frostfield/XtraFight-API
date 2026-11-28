package team.frostfield.xtrafight.mixins.epicfight.common;

import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.frostfield.xtrafight.utils.managers.MeshesManager;
import yesman.epicfight.api.client.model.Meshes;

@Mixin(value = Meshes.class, remap = false, priority = 1005)
public class MeshesMixin {

    @Inject(at = @At("HEAD"), method = "lambda$reload$32", remap = false)
    private static void xtrafight$setManager(ResourceManager resourceManager, CallbackInfo ci) {
        MeshesManager.setManager(resourceManager);
    }
}
