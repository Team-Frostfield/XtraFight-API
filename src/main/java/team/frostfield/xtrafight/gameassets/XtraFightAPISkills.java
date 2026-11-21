package team.frostfield.xtrafight.gameassets;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;
import team.frostfield.xtrafight.events.bus.impl.ModBusImpl;
import team.frostfield.xtrafight.skills.common.CounterAttack;
import team.frostfield.xtrafight.skills.common.HeavyAttack;
import team.frostfield.xtrafight.utils.registry.DeferredRegistryUtil;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;

import java.util.function.Supplier;

public class XtraFightAPISkills implements ILoadableClass {
    private static final DeferredRegister<Skill> SKILLS = DeferredRegistryUtil.createDefaultSkills();

    public static final DeferredHolder<Skill, HeavyAttack> HEAVY_ATTACKS = XtraFightAPISkills.register("heavy_attack", HeavyAttack::createHeavyAttackBuilder);
    public static final DeferredHolder<Skill, CounterAttack> COUNTER_ATTACK = XtraFightAPISkills.register("counter_attack", CounterAttack::createCounterAttackBuilder);

    @Invoke
    private XtraFightAPISkills(ModBusImpl modBusImpl) {
        SKILLS.register(modBusImpl);
    }

    private static <T extends SkillBuilder<?>, S extends Skill> DeferredHolder<Skill, S> register(String name, Supplier<T> supplier) {
        return SKILLS.register(name, key -> supplier.get().build(key));
    }
}
