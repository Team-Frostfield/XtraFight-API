package team.frostfield.xtrafight.animation.types.joints;

import team.frostfield.xtrafight.cls.functions.ArraySupplier;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.client.animation.property.JointMask;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.gameasset.Armatures;

import java.util.Arrays;
import java.util.function.Supplier;

public enum JointMasks implements team.frostfield.xtrafight.animation.types.joints.JointMask {
    BIPED_ROOT_UPPER_JOINTS(
            Armatures.BIPED.get().torso,
            Armatures.BIPED.get().chest,
            Armatures.BIPED.get().head,
            // right arms
            Armatures.BIPED.get().shoulderR, Armatures.BIPED.get().armR,
            Armatures.BIPED.get().handR, Armatures.BIPED.get().toolR,
            // left arms
            Armatures.BIPED.get().shoulderL, Armatures.BIPED.get().armL,
            Armatures.BIPED.get().handL, Armatures.BIPED.get().toolL
    ),
    BIPED_LEFT_ARM(
            Armatures.BIPED.get().shoulderL, Armatures.BIPED.get().armL,
            Armatures.BIPED.get().handL, Armatures.BIPED.get().toolL
    ),
    BIPED_RIGHT_ARM(
            Armatures.BIPED.get().shoulderR, Armatures.BIPED.get().armR,
            Armatures.BIPED.get().handR, Armatures.BIPED.get().toolR
    ),
    BIPED_LEGS(
            Armatures.BIPED.get().thighR, Armatures.BIPED.get().kneeR,
            Armatures.BIPED.get().legR, Armatures.BIPED.get().thighL,
            Armatures.BIPED.get().kneeL, Armatures.BIPED.get().legL
    ),
    NONE;

    private final Supplier<ArraySupplier<Joint>> jointsSupplier;
    private final int universalID;

    JointMasks(Joint... joints) {
        this(() -> ArraySupplier.of(joints));
    }

    JointMasks(Supplier<ArraySupplier<Joint>> jointsSupplier) {
        this.jointsSupplier = jointsSupplier;
        this.universalID = team.frostfield.xtrafight.animation.types.joints.JointMask.ENUM_MANAGER.assign(this);
    }

    public static JointMaskEntry createEntry(Joint... joints) {
        if (joints == null || joints.length == 0) {
            return JointMaskEntry.builder()
                    .defaultMask(JointMask.JointMaskSet.of())
                    .create();
        }

        JointMask[] masks = Arrays.stream(joints)
                .map(j -> JointMask.of(j.getName()))
                .toArray(JointMask[]::new);

        return JointMaskEntry.builder()
                .defaultMask(JointMask.JointMaskSet.of(masks))
                .create();
    }

    @Override
    public JointMaskEntry getAsEntry() {
        return createEntry(this.getJoints());
    }

    @Override
    public Joint[] getJoints() {
        ArraySupplier<Joint> supplier = this.jointsSupplier.get();
        return supplier != null ? supplier.get() : new Joint[0];
    }

    @Override
    public int universalOrdinal() {
        return this.universalID;
    }
}