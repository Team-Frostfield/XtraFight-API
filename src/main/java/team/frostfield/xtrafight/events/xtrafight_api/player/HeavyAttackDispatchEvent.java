package team.frostfield.xtrafight.events.xtrafight_api.player;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameterize;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.marks.HybridEvent;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;


@HybridEvent
public class HeavyAttackDispatchEvent extends DispatchEvent implements ParameterizedEvent {
    protected final SkillContainer skillContainer;

    public HeavyAttackDispatchEvent(SkillContainer skillContainer) {
        this.skillContainer = skillContainer;
    }

    public SkillContainer getSkillContainer() {
        return this.skillContainer;
    }


    public interface HeavyAttackInvoker extends EventParameterize<HeavyAttackDispatchEvent> {
        void onHeavyAttack(PlayerPatch<?> player);
    }
}
