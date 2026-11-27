package team.frostfield.xtrafight.animation.types;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import team.frostfield.xtrafight.animation.SimpleAnimationProperty;
import team.frostfield.xtrafight.cls.lists.BasicArrayList;
import team.frostfield.xtrafight.cls.lists.BasicList;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.registry.entries.EpicFightParticles;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

// An advanced version of ComboAttackAnimation-BasicAttackAnimation class, it alone can combine:
// DashAttackAnimation, AirAttackAnimation, AttackAnimation, enabling trail registry too without the need for .jsons for each animation
// It can calculate attack animations too by their Frame-Window, no need to time them in seconds.
public class SimpleAttackAnimation extends AttackAnimation {
    private final BasicList<TrailDefinition> trailDefinitions = new BasicArrayList<>();

    // Seconds Calculation.
    public SimpleAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends SimpleAttackAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, antic, preDelay, contact, recovery, collider, colliderJoint, accessor, armature);
        this.addCommonProperties();
        this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
    }

    // FPS Calculation.
    public SimpleAttackAnimation(float transitionTime, float antic, int preDelayFrame, int contactFrame, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends SimpleAttackAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, antic, (float) preDelayFrame / 60, (float) contactFrame / 60, recovery, collider, colliderJoint, accessor, armature);
        this.addCommonProperties();
        this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
    }

    // shouldBend Should be disabled if the animation was a dash or an air slash,
    // Since it makes the chest rotate while attacking wherever the player is looking.
    public SimpleAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, boolean shouldBend, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends SimpleAttackAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, antic, preDelay, contact, recovery, collider, colliderJoint, accessor, armature);
        this.addCommonProperties();
        if (shouldBend) {
            this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        }
    }
    // You can modify the basis speed of the AttackAnimation using this constructor.
    public SimpleAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, boolean shouldBend, float basisSpeed, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends SimpleAttackAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, antic, preDelay, contact, recovery, collider, colliderJoint, accessor, armature);
        this.addCommonProperties();
        this.addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, basisSpeed);
        if (shouldBend) {
            this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
        }
    }

    // FPS Calculation.
    public SimpleAttackAnimation(float transitionTime, float antic, int preDelayFrame, int contactFrame, float recovery, float basisSpeed, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends SimpleAttackAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, antic, (float) preDelayFrame / 60, (float) contactFrame / 60, recovery, collider, colliderJoint, accessor, armature);
        this.addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, basisSpeed);
        this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
    }

    public SimpleAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, path, armature, new Phase(antic, antic, preDelay, contact, recovery, recovery, colliderJoint, collider));
        this.addCommonProperties();
    }

    public SimpleAttackAnimation(float transitionTime, String path, AssetAccessor<? extends Armature> armature, Phase... phases) {
        super(transitionTime, path, armature, phases);
        this.addCommonProperties();
    }

    public SimpleAttackAnimation(float transitionTime, AnimationManager.AnimationAccessor<? extends SimpleAttackAnimation> accessor, AssetAccessor<? extends Armature> armature, Phase... phases) {
        super(transitionTime, accessor, armature, phases);
        this.addCommonProperties();
    }

    public SimpleAttackAnimation addTrail(Joint joint, TrailColor color) {
        this.trailDefinitions.add(new TrailDefinition(joint, color, Arrays.stream(this.phases).iterator().next().preDelay, Arrays.stream(this.phases).iterator().next().contact, TrailPreset.SWORD));
        return this;
    }
    public SimpleAttackAnimation addTrail(Joint joint, TrailColor color, TrailPreset preset) {
        this.trailDefinitions.add(new TrailDefinition(joint, color, Arrays.stream(this.phases).iterator().next().preDelay, Arrays.stream(this.phases).iterator().next().contact, preset));
        return this;
    }
    public SimpleAttackAnimation addTrail(Joint joint, TrailColor color, float begin, float end, TrailPreset preset) {
        this.trailDefinitions.add(new TrailDefinition(joint, color, begin, end, preset));
        return this;
    }
    // FPS Calculation
    public SimpleAttackAnimation addTrail(Joint joint, TrailColor color, int beginFrame, int endFrane, TrailPreset preset) {
        this.trailDefinitions.add(new TrailDefinition(joint, color, (float) beginFrame / 60, (float) endFrane / 60, preset));
        return this;
    }

    public SimpleAttackAnimation addTrail(Joint joint, TrailColor color, float begin, float end) {
        this.trailDefinitions.add(new TrailDefinition(joint, color, begin, end, TrailPreset.SWORD));
        return this;
    }

    private void addCommonProperties() {
        this.addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true);
        this.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, false);
        this.addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F);
    }

    @Override
    protected void bindPhaseState(Phase phase) {;
        this.stateSpectrumBlueprint
                .newTimePair(phase.start, phase.preDelay)
                .addState(EntityState.PHASE_LEVEL, 1)

                .newTimePair(phase.start, phase.contact)
                .addState(EntityState.SKILL_EXECUTABLE, false)

                .newTimePair(phase.start, phase.recovery)
                .addState(EntityState.MOVEMENT_LOCKED, true)
                .addState(EntityState.UPDATE_LIVING_MOTION, false)
                .addState(EntityState.COMBO_ATTACKS_DOABLE, false)

                .newTimePair(phase.start, phase.end)
                .addState(EntityState.INACTION, true)

                .newTimePair(phase.preDelay, phase.contact)
                .addState(EntityState.ATTACKING, true)
                .addState(EntityState.PHASE_LEVEL, 2)

                .newTimePair(phase.contact, phase.end)
                .addState(EntityState.PHASE_LEVEL, 3)
                .addState(EntityState.TURNING_LOCKED, true);
    }

    @Override
    public void loadAnimation() {
        super.loadAnimation();

        if (!this.properties.containsKey(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED)) {
            float basisSpeed = Float.parseFloat(String.format(Locale.US, "%.2f", 1.0F / this.getTotalTime()));
            this.addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, basisSpeed);
        }

        if (!this.properties.containsKey(ClientAnimationProperties.TRAIL_EFFECT)) {
            TrailPreset propertyPreset = null;
            if (this.properties.containsKey(SimpleAnimationProperty.TRAIL_PRESET)) {
                Object rawPreset = this.properties.get(SimpleAnimationProperty.TRAIL_PRESET);
                if (rawPreset instanceof TrailPreset preset && !preset.equals(TrailPreset.EMPTY)) {
                    propertyPreset = preset;
                }
            }

            TrailColor propertyColor = null;
            if (this.properties.containsKey(SimpleAnimationProperty.TRAIL_COLOR)) {
                Object rawColor = this.properties.get(SimpleAnimationProperty.TRAIL_COLOR);
                if (rawColor instanceof TrailColor color && !color.equals(TrailColor.EMPTY)) {
                    propertyColor = color;
                }
            }

            List<TrailInfo> activeTrails = new ArrayList<>();
            for (TrailDefinition def : this.trailDefinitions) {
                TrailPreset effectivePreset = def.preset();
                if (effectivePreset == null || effectivePreset.equals(TrailPreset.EMPTY)) {
                    effectivePreset = propertyPreset != null ? propertyPreset : TrailPreset.SWORD;
                }

                TrailColor effectiveColor = def.color();
                if (effectiveColor == null || effectiveColor.equals(TrailColor.EMPTY)) {
                    effectiveColor = propertyColor != null ? propertyColor : TrailColor.WHITE;
                }

                activeTrails.add(TrailInfo.builder()
                        .time(def.startDelay(), def.endDelay())
                        .joint(def.joint().getName())
                        .r(effectiveColor.r())
                        .g(effectiveColor.g())
                        .b(effectiveColor.b())
                        .startPos(effectivePreset.start())
                        .endPos(effectivePreset.end())
                        .texture(TrailInfo.GENERIC_TRAIL_TEXTURE)
                        .type(EpicFightParticles.SWING_TRAIL.get())
                        .lifetime(effectivePreset.lifetime())
                        .interpolations(effectivePreset.interpolateCount())
                        .create());
            }

            if (!activeTrails.isEmpty()) {
                this.addProperty(ClientAnimationProperties.TRAIL_EFFECT, activeTrails);
            }
        }
    }

    public static Phase newPhase(int startFrame, int endFrame, int recoveryFrame, Joint attackingJoint, Collider collider) {
        float preDelay = (float) startFrame / 60;
        float contact = (float) endFrame / 60;
        float recovery = (float) recoveryFrame / 60;
        return new Phase(preDelay, preDelay, contact, recovery, recovery, attackingJoint, collider);
    }

    public static Phase newPhase(int startFrame, int endFrame, int recoveryFrame, InteractionHand hand, Joint attackingJoint, Collider collider) {
        float preDelay = (float) startFrame / 60;
        float contact = (float) endFrame / 60;
        float recovery = (float) recoveryFrame / 60;
        return new Phase(preDelay, preDelay, contact, recovery, recovery, hand, attackingJoint, collider);
    }

    public static Phase newPhase(int startFrame, int endFrame, int recoveryFrame, InteractionHand hand, Joint firstJoint, Joint secondJoint, Collider collider) {
        float preDelay = (float) startFrame / 60;
        float contact = (float) endFrame / 60;
        float recovery = (float) recoveryFrame / 60;
        return new Phase(0.0F ,preDelay, preDelay, contact, recovery, recovery, hand, JointColliderPair.of(firstJoint, collider), JointColliderPair.of(secondJoint, collider));
    }


    public SimpleAttackAnimation multiplyDamage(float multiplier) {
        this.addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(multiplier));
        return this;
    }
    public SimpleAttackAnimation multiplyImpact(float multiplier) {
        this.addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(multiplier));
        return this;
    }

    private record TrailDefinition(Joint joint, TrailColor color, float startDelay, float endDelay, TrailPreset preset) {}

    public record TrailColor(float r, float g, float b)  {
        public static final TrailColor WOOD = new TrailColor(0.55F, 0.41F, 0.29F);
        public static final TrailColor STONE = new TrailColor(0.6F, 0.6F, 0.6F);
        public static final TrailColor IRON = new TrailColor(0.85F, 0.85F, 0.85F);
        public static final TrailColor GOLD = new TrailColor(0.992F, 1.0F, 0.463F);
        public static final TrailColor DIAMOND = new TrailColor(0.2F, 0.9F, 0.7F);
        public static final TrailColor NETHERITE = new TrailColor(0.3F, 0.25F, 0.3F);
        public static final TrailColor RED = new TrailColor(1.0F, 0.2F, 0.2F);
        public static final TrailColor GREEN = new TrailColor(0.2F, 1.0F, 0.2F);
        public static final TrailColor BLUE = new TrailColor(0.2F, 0.2F, 1.0F);
        public static final TrailColor WHITE = new TrailColor(1.0F, 1.0F, 1.0F);
        public static final TrailColor EMPTY = new TrailColor(0F, 0F, 0F);

        public static TrailColor ofClamped(float r, float g, float b) {
            return new TrailColor(
                    Math.min(r, 1.0F),
                    Math.min(g, 1.0F),
                    Math.min(b, 1.0F)
            );
        }
    }

    public record TrailPreset(Vec3 start, Vec3 end, int lifetime, int interpolateCount) {
        public static final TrailPreset AXE = new TrailPreset(
                new Vec3(0.0, -0.15, -0.35), new Vec3(0.0, -0.15, -0.7), 4, 6
        );
        public static final TrailPreset DAGGER = new TrailPreset(
                new Vec3(0, 0, 0), new Vec3(0, 0, -0.6), 4, 4
        );
        public static final TrailPreset SWORD = new TrailPreset(
                new Vec3(0, 0, 0), new Vec3(0, 0, -1.2), 4, 6
        );
        public static final TrailPreset GREATSWORD = new TrailPreset(
                new Vec3(0, 0, -0.2), new Vec3(0, -0.24, -1.8), 6, 4
        );
        public static final TrailPreset LONGSWORD = new TrailPreset(
                new Vec3(0, 0, 0.2), new Vec3(0, 0, -1.7), 4, 4
        );
        public static final TrailPreset TACHI = new TrailPreset(
                new Vec3(0, 0, -0.2), new Vec3(0, 0.4, -1.75), 6, 4
        );
        public static final TrailPreset SPEAR = new TrailPreset(
                new Vec3(0.0, 0.2, -0.9), new Vec3(0.0, 0.25, -2.0), 6, 4
        );
        public static final TrailPreset EMPTY = new TrailPreset(null, null, 0, 0);

        public static TrailPreset of(Vec3 start, Vec3 end, int lifetime, int interpolateCount) {
            return new TrailPreset(start, end, lifetime, interpolateCount);
        }
    }
}