package team.frostfield.xtrafight.events.xtrafight_api.player;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.marks.HybridEvent;
import yesman.epicfight.skill.SkillContainer;

@HybridEvent
public class CounterAttackDispatchEvent extends DispatchEvent implements ParameterizedEvent {
    private final SkillContainer skillContainer;

    public CounterAttackDispatchEvent(SkillContainer skillContainer) {
        this.skillContainer = skillContainer;
    }

    public SkillContainer getSkillContainer() {
        return this.skillContainer;
    }

    @FunctionalInterface
    public interface CounterAttackParams extends EventParameters {
        void onCounter(SkillContainer container);
    }


}
