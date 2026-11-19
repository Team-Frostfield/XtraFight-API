package team.frostfield.xtrafight.builders.epicfight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.ApiStatus.Internal;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.Armatures.ArmatureAccessor;
import yesman.epicfight.gameasset.Armatures.ArmatureContructor;
import yesman.epicfight.model.armature.CreeperArmature;
import yesman.epicfight.model.armature.DragonArmature;
import yesman.epicfight.model.armature.EndermanArmature;
import yesman.epicfight.model.armature.HoglinArmature;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.model.armature.IronGolemArmature;
import yesman.epicfight.model.armature.PiglinArmature;
import yesman.epicfight.model.armature.RavagerArmature;
import yesman.epicfight.model.armature.SpiderArmature;
import yesman.epicfight.model.armature.VexArmature;
import yesman.epicfight.model.armature.WitherArmature;

import java.util.Map;

public class ArmatureBuilder {

    private ArmatureBuilder() {}

    public static <T extends Armature> Armatures.ArmatureAccessor<T> newEntityArmature(EntityType<?> entity, String parsedPath, ArmatureType<T> type) {
        ResourceLocation parsed = ResourceLocation.parse(parsedPath);
        return newEntityArmatureI(entity, parsed.getNamespace(), parsed.getPath(), type::apply);
    }

    public static <T extends Armature> Armatures.ArmatureAccessor<T> newEntityArmature(EntityType<?> entity, ResourceLocation location, ArmatureType<T> type) {
        return newEntityArmatureI(entity, location.getNamespace(), location.getPath(), type::apply);
    }

    public static <T extends Armature> Armatures.ArmatureAccessor<T> newEntityArmature(EntityType<?> entity, String modid, String path, ArmatureType<T> type) {
        return newEntityArmatureI(entity, modid, path, type::apply);
    }

    public static <T extends Armature> Armatures.ArmatureAccessor<T> newArmature(String parsedPath, ArmatureType<T> type) {
        ResourceLocation parsed = ResourceLocation.parse(parsedPath);
        return newArmatureI(parsed.getNamespace(), parsed.getPath(), type::apply);
    }

    public static <T extends Armature> Armatures.ArmatureAccessor<T> newArmature(ResourceLocation location, ArmatureType<T> type) {
        return newArmatureI(location.getNamespace(), location.getPath(), type::apply);
    }

    public static <T extends Armature> Armatures.ArmatureAccessor<T> newArmature(String modid, String path, ArmatureType<T> type) {
        return newArmatureI(modid, path, type::apply);
    }

    @Internal
    private static <T extends Armature> ArmatureAccessor<T> newArmatureI(String modid, String path, ArmatureContructor<T> contructor) {
        return ArmatureAccessor.create(modid, path, contructor);
    }

    @Internal
    private static <AR extends Armature> ArmatureAccessor<AR> newEntityArmatureI(EntityType<?> entity, String modid, String path, ArmatureContructor<AR> contructor) {
        ArmatureAccessor<AR> accessor = newArmatureI(modid, path, contructor);
        Armatures.registerEntityTypeArmature(entity, accessor);
        return accessor;
    }

    public record ArmatureType<T extends Armature>(ArmatureContructor<T> constructor) {
        public static final ArmatureType<HumanoidArmature> HUMANOID = new ArmatureType<>(HumanoidArmature::new);
        public static final ArmatureType<CreeperArmature> CREEPER = new ArmatureType<>(CreeperArmature::new);
        public static final ArmatureType<PiglinArmature> PIGLIN = new ArmatureType<>(PiglinArmature::new);
        public static final ArmatureType<EndermanArmature> ENDERMAN = new ArmatureType<>(EndermanArmature::new);
        public static final ArmatureType<DragonArmature> DRAGON = new ArmatureType<>(DragonArmature::new);
        public static final ArmatureType<HoglinArmature> HOGLIN = new ArmatureType<>(HoglinArmature::new);
        public static final ArmatureType<IronGolemArmature> IRON_GOLEM = new ArmatureType<>(IronGolemArmature::new);
        public static final ArmatureType<RavagerArmature> RAVAGER = new ArmatureType<>(RavagerArmature::new);
        public static final ArmatureType<SpiderArmature> SPIDER = new ArmatureType<>(SpiderArmature::new);
        public static final ArmatureType<VexArmature> VEX = new ArmatureType<>(VexArmature::new);
        public static final ArmatureType<WitherArmature> WITHER = new ArmatureType<>(WitherArmature::new);
        public static final ArmatureType<?> EMPTY = new ArmatureType<>((name, jointNumber, joint, jointMap) -> null);

        public T apply(String name, int jointNumber, Joint joint, Map<String, Joint> jointMap) {
            return this.constructor.invoke(name, jointNumber, joint, jointMap);
        }

        public static <T extends Armature> ArmatureType<T> of(ArmatureContructor<T> constructor) {
            return new ArmatureType<T>(constructor);
        }


        public ArmatureContructor<? extends Armature> getConstructor() {
            return this.constructor;
        }
    }

}