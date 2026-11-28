package team.frostfield.xtrafight.gameassets;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;
import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.skills.common.CounterAttack;
import team.frostfield.xtrafight.skills.common.HeavyAttack;
import team.frostfield.xtrafight.utils.registry.DeferredRegistryUtil;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillDataKey;

public class XtraFightAPISkillDataKeys implements ILoadableClass {
    private static final DeferredRegister<SkillDataKey<?>> SKILL_DATA_KEYS = DeferredRegistryUtil.createDefaultSDK();

    public static final RegistryObject<SkillDataKey<Integer>> HEAVY_COUNTER = register(
            "heavy_counter",
            PacketBufferCodec.INTEGER,
            0,
            false,
            HeavyAttack.class
    );
    public static final RegistryObject<SkillDataKey<Integer>> MOTION_COUNTER = register(
            "motion_counter",
            PacketBufferCodec.INTEGER,
            0,
            false,
            CounterAttack.class
    );

    @Invoke
    private XtraFightAPISkillDataKeys(ModBus modBus) {
        SKILL_DATA_KEYS.register(modBus);
    }

    @SafeVarargs
    private static <T> RegistryObject<SkillDataKey<T>> register(String name, PacketBufferCodec<T> packetCodec, T defaultValue, boolean synchronizeTrackingPlayers, Class<? extends Skill>... skillClass) {
        return SKILL_DATA_KEYS.register(name, () -> SkillDataKey.createSkillDataKey(packetCodec, defaultValue, synchronizeTrackingPlayers, skillClass));
    }
}
