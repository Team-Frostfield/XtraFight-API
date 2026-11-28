package team.frostfield.xtrafight.utils.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus.Internal;
import team.frostfield.xtrafight.main.XtraFightAPI;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillDataKey;

public class DeferredRegistryUtil {

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registry, String modID) {
       return DeferredRegister.create(registry, modID);
    }

    public static DeferredRegister<Item> createItems(String modID) {
        return DeferredRegister.create(Registries.ITEM, modID);
    }

    public static DeferredRegister<Skill> createSkills(String modID) {
        return DeferredRegister.create(EpicFightMod.identifier("skill"), modID);
    }

    public static DeferredRegister<SkillDataKey<?>> createSkillDataKeys(String modID) {
        return DeferredRegister.create(EpicFightMod.identifier("skill_data_keys"), modID);
    }

    @Internal
    public static DeferredRegister<SkillDataKey<?>> createDefaultSDK() {
        return DeferredRegister.create(EpicFightMod.identifier("skill_data_keys"), XtraFightAPI.MOD_ID);
    }

}
