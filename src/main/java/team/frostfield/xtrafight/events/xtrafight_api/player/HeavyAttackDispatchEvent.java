package team.frostfield.xtrafight.events.xtrafight_api.player;

import team.frostfield.xtrafight.events.dispatch.IDispatchableEvent;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.marks.HybridEvent;
import team.frostfield.xtrafight.events.xtrafight_api.XtraFightAPIDispatchableEvents;
import yesman.epicfight.skill.SkillContainer;


@HybridEvent
public class HeavyAttackDispatchEvent extends DispatchEvent implements IDispatchableEvent<HeavyAttackDispatchEvent> {
    protected final SkillContainer skillContainer;

    public HeavyAttackDispatchEvent(SkillContainer skillContainer) {
        this.skillContainer = skillContainer;
    }

    public SkillContainer getSkillContainer() {
        return this.skillContainer;
    }

    @Override
    public HeavyAttackDispatchEvent post() {
       return XtraFightAPIDispatchableEvents.Player.HEAVY_ATTACK.postEvent(this);
    }
}
