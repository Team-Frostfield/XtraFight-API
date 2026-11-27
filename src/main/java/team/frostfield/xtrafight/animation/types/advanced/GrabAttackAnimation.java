package team.frostfield.xtrafight.animation.types.advanced;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import team.frostfield.xtrafight.animation.types.SimpleAttackAnimation;
import team.frostfield.xtrafight.events.xtrafight_api.XtraFightAPIDispatchableEvents;
import team.frostfield.xtrafight.events.xtrafight_api.animation.GrabAttackDispatchEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GrabAttackAnimation extends SimpleAttackAnimation {
    private final AnimationManager.AnimationAccessor<? extends ActionAnimation> grabbedAnimation;
    private final AnimationManager.AnimationAccessor<? extends ActionAnimation> failAnimation;
    private final AnimationManager.AnimationAccessor<? extends ActionAnimation> blockedAnimation;
    private final AnimationManager.AnimationAccessor<? extends AttackAnimation> successAnimation;

    public GrabAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, Collider collider, Joint joint, AnimationManager.AnimationAccessor<? extends GrabAttackAnimation> accessor, AnimationManager.AnimationAccessor<? extends ActionAnimation> grabbedAnimation, AnimationManager.AnimationAccessor<? extends ActionAnimation> failAnimation, AnimationManager.AnimationAccessor<? extends ActionAnimation> blockedAnimation, AnimationManager.AnimationAccessor<? extends AttackAnimation> successAnimation, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, antic, preDelay, contact, recovery, collider, joint, accessor, armature);
        this.grabbedAnimation = grabbedAnimation;
        this.failAnimation = failAnimation;
        this.blockedAnimation = blockedAnimation;
        this.successAnimation = successAnimation;
        this.addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
        this.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, false);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, null);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_ORIGIN_AS_DESTINATION);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_GET, MoveCoordFunctions.WORLD_COORD);
        this.addProperty(AnimationProperty.ActionAnimationProperty.ENTITY_YROT_PROVIDER, MoveCoordFunctions.LOOK_DEST);
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd) {
        super.end(entitypatch, nextAnimation, isEnd);

        if (isEnd && !entitypatch.isLogicalClient()) {
            LivingEntity hitEntity = entitypatch.getTarget();
            AtomicBoolean isHitSuccess = new AtomicBoolean(false);

            if (hitEntity != null) {
                Phase phase = this.getPhaseByTime(0.0F);
                AnimationPlayer player = entitypatch.getAnimator().getPlayerFor(this.getAccessor());
                float prevPoseTime = player.getPrevElapsedTime();
                float poseTime = player.getElapsedTime();
                List<Entity> collidingEntities = phase.getCollidingEntities(entitypatch, this, prevPoseTime, poseTime, this.getPlaySpeed(entitypatch, this));
                boolean isHitEntityIncluded = collidingEntities.contains(hitEntity);
                GrabAttackDispatchEvent event = new GrabAttackDispatchEvent(entitypatch, isHitEntityIncluded ? hitEntity : null, collidingEntities, null);
                if (isHitEntityIncluded) {
                    DamageSource dmgSource = this.getEpicFightDamageSource(entitypatch, hitEntity, phase);
                    EpicFightCapabilities.<LivingEntity, LivingEntityPatch<LivingEntity>>getParameterizedEntityPatch(hitEntity, LivingEntity.class, LivingEntityPatch.class).ifPresentOrElse(hitEntityPatch -> {
                        if (hitEntityPatch.tryHurt(dmgSource, 0.0F).resultType.dealtDamage()) {
                            event.setState(GrabAttackDispatchEvent.ExecutionState.SUCCESS);
                            XtraFightAPIDispatchableEvents.Animation.GRAB_ATTACK.postWithParams(event, params -> params.onGrab(event.getExecutor(), event.getTarget(), event.getCollidingEntities(), event.getExecutionState()));
                            entitypatch.reserveAnimation(this.successAnimation);
                            entitypatch.setGrapplingTarget(hitEntity);
                            hitEntity.lookAt(EntityAnchorArgument.Anchor.FEET, entitypatch.getOriginal().position());
                            hitEntityPatch.playAnimationSynchronized(this.grabbedAnimation, 0.0F);
                            isHitSuccess.set(true);
                        } else if (hitEntityPatch.tryHurt(dmgSource, 0.0F).resultType.shouldCount()) {
                            event.setState(GrabAttackDispatchEvent.ExecutionState.BLOCKED);
                            XtraFightAPIDispatchableEvents.Animation.GRAB_ATTACK.postWithParams(event, params -> params.onGrab(event.getExecutor(), event.getTarget(), event.getCollidingEntities(), event.getExecutionState()));
                            entitypatch.reserveAnimation(this.blockedAnimation);
                            isHitSuccess.set(true);
                        } else {
                            event.setState(GrabAttackDispatchEvent.ExecutionState.MISSED);
                            XtraFightAPIDispatchableEvents.Animation.GRAB_ATTACK.postWithParams(event, params -> params.onGrab(event.getExecutor(), event.getTarget(), event.getCollidingEntities(), event.getExecutionState()));
                            entitypatch.reserveAnimation(this.failAnimation);
                        }
                        // if hitEntity == null
                    }, () -> {
                        entitypatch.reserveAnimation(this.successAnimation);
                        entitypatch.setGrapplingTarget(hitEntity);
                        isHitSuccess.set(true);
                    });
                }
                if (!isHitSuccess.get()) {
                    event.setState(GrabAttackDispatchEvent.ExecutionState.MISSED);
                    XtraFightAPIDispatchableEvents.Animation.GRAB_ATTACK.postWithParams(event, params -> params.onGrab(event.getExecutor(), event.getTarget(), event.getCollidingEntities(), event.getExecutionState()));
                    entitypatch.reserveAnimation(this.failAnimation);
                }
            }
        }
    }
}
