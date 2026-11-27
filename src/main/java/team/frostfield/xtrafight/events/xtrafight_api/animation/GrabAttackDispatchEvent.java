package team.frostfield.xtrafight.events.xtrafight_api.animation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;

public class GrabAttackDispatchEvent extends DispatchEvent implements ParameterizedEvent {
    private final LivingEntityPatch<?> executor;
    private final LivingEntity target;
    private final List<Entity> collidingEntities;
    private ExecutionState state;

    public GrabAttackDispatchEvent(LivingEntityPatch<?> executor, LivingEntity target, List<Entity> collidingEntities, ExecutionState state) {
        this.executor = executor;
        this.target = target;
        this.collidingEntities = collidingEntities;
        this.state = state;
    }

    public LivingEntityPatch<?> getExecutor() {
        return this.executor;
    }

    public List<Entity> getCollidingEntities() {
        return this.collidingEntities;
    }

    public ExecutionState getExecutionState() {
        return this.state;
    }

    public boolean isStateMatch(ExecutionState state) {
        return this.state == state;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public LivingEntityPatch<?> getPatchedTarget() {
        return EpicFightCapabilities.getEntityPatch(this.target, LivingEntityPatch.class);
    }

    public boolean targetIs(Class<? extends Entity> matchingEntity) {
        return matchingEntity.isInstance(this.target);
    }

    public void setState(ExecutionState state) {
        this.state = state;
    }

    @FunctionalInterface
    public interface GrabAttackParams extends EventParameters {
        void onGrab(LivingEntityPatch<?> entityPatch, LivingEntity target, List<Entity> collidingEntities, ExecutionState state);
    }

    public enum ExecutionState {
        SUCCESS,
        BLOCKED,
        MISSED;

        ExecutionState() {}

        public boolean success() {
            return this.is(SUCCESS);
        }

        public boolean missed() {
            return this.is(MISSED);
        }

        public boolean blocked() {
            return this.is(BLOCKED);
        }

        public boolean is(ExecutionState matchingState) {
            return this == matchingState;
        }
    }

}
