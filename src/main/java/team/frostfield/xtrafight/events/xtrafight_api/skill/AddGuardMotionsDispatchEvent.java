package team.frostfield.xtrafight.events.xtrafight_api.skill;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.GuardSkill.Builder;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.function.BiFunction;


public abstract class AddGuardMotionsDispatchEvent extends DispatchEvent implements ParameterizedEvent {

    protected final GuardSkill.Builder builder;

    public AddGuardMotionsDispatchEvent(GuardSkill.Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return this.builder;
    }

    @FunctionalInterface
    public interface AddGuardMotionParams extends EventParameters {
        void onAddMotions(GuardSkill.Builder builder);
    }

    public static class Normal extends AddGuardMotionsDispatchEvent {

        public Normal(Builder builder) {
            super(builder);
        }


        public void addGuardMotion(WeaponCategory category, BiFunction<CapabilityItem, PlayerPatch<?>, AnimationManager.AnimationAccessor<? extends StaticAnimation>> function) {
            this.builder.addGuardMotion(category, function);
        }

        public void addGuardBreakMotion(WeaponCategory category, BiFunction<CapabilityItem, PlayerPatch<?>, AnimationManager.AnimationAccessor<? extends StaticAnimation>> function) {
            this.builder.addGuardBreakMotion(category, function);
        }
    }


    public static class Parry extends AddGuardMotionsDispatchEvent {

        public Parry(ParryingSkill.Builder builder) {
            super(builder);
        }


        public void addAdvancedGuardMotion(WeaponCategory category, BiFunction<CapabilityItem, PlayerPatch<?>, ?> function) {
            this.builder.addAdvancedGuardMotion(category, function);
        }
    }
}
