package team.frostfield.xtrafight.utils.managers;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.gameasset.Armatures;

public final class ArmaturesManager {
    private ArmaturesManager() {}

    public static void setArmatureFor(EntityType<?> type, AssetAccessor<? extends Armature> armatureAccessor) {
        Armatures.registerEntityTypeArmature(type, armatureAccessor);
        ResourceManager manager = AnimationManager.getAnimationResourceManager();
        if (manager != null) {
            Armatures.reload(manager);
        } else {
            throw new NullPointerException("Called setArmatureFor() too soon!");
        }
    }

    public static void reloadArmatures() {
        Armatures.reload(AnimationManager.getAnimationResourceManager());
    }

}
