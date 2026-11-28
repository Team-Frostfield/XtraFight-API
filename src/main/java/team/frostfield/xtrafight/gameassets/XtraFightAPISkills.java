package team.frostfield.xtrafight.gameassets;

import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;
import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.main.XtraFightAPI;
import team.frostfield.xtrafight.skills.common.CounterAttack;
import team.frostfield.xtrafight.skills.common.HeavyAttack;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;

public class XtraFightAPISkills implements ILoadableClass {

    public static Skill HEAVY_ATTACK;
    public static Skill COUNTER_ATTACK;

    @Invoke
    private XtraFightAPISkills(ModBus modBus) {
       modBus.addListener(XtraFightAPISkills::registerSkills);
    }

    private static void registerSkills(SkillBuildEvent event) {
        SkillBuildEvent.ModRegistryWorker worker = event.createRegistryWorker(XtraFightAPI.MOD_ID);
        HEAVY_ATTACK = worker.build("heavy_attack", HeavyAttack::new, HeavyAttack.createHeavyAttackBuilder());
        COUNTER_ATTACK = worker.build("counter_attack", CounterAttack::new, CounterAttack.createCounterAttackBuilder());
    }

}
