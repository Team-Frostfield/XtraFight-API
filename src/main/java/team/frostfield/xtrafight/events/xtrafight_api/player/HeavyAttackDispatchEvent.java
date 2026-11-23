package team.frostfield.xtrafight.events.xtrafight_api.player;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.marks.HybridEvent;
import yesman.epicfight.skill.SkillContainer;


@HybridEvent
public class HeavyAttackDispatchEvent extends DispatchEvent implements ParameterizedEvent {
    protected final SkillContainer skillContainer;

    public HeavyAttackDispatchEvent(SkillContainer skillContainer) {
        this.skillContainer = skillContainer;
    }

    public SkillContainer getSkillContainer() {
        return this.skillContainer;
    }


    public interface HeavyAttackParams extends EventParameters {
        void onHeavyAttack(SkillContainer container);
    }
}
