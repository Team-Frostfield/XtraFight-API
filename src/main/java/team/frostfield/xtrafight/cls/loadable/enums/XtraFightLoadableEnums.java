package team.frostfield.xtrafight.cls.loadable.enums;

import org.jetbrains.annotations.ApiStatus.Internal;
import team.frostfield.xtrafight.client.sound.data.XtraFightAPISoundsManager;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.config.XtraFightAPIConfigs;
import team.frostfield.xtrafight.gameassets.XtraFightAPIKeyMappings;
import team.frostfield.xtrafight.gameassets.XtraFightAPISkillDataKeys;
import team.frostfield.xtrafight.gameassets.XtraFightAPISkills;
import team.frostfield.xtrafight.network.XtraFightAPINetworkManager;

@Internal
public enum XtraFightLoadableEnums implements LoadableEnum {
    NETWORK_MANAGER(XtraFightAPINetworkManager.class),
    CONFIG(XtraFightAPIConfigs.class),
    SOUND_MANAGER(XtraFightAPISoundsManager.class),
    SKILLS(XtraFightAPISkills.class),
    SKILL_DATA_KEYS(XtraFightAPISkillDataKeys.class),
    // Client Registries
    KEY_MAPPINGS(XtraFightAPIKeyMappings.class, true);

    private final Class<? extends ILoadableClass> loadableClass;
    private final boolean clientOnly;

    XtraFightLoadableEnums(Class<? extends ILoadableClass> loadableClass) {
        this(loadableClass, false);
    }

    XtraFightLoadableEnums(Class<? extends ILoadableClass> loadableClass, boolean clientOnly) {
        this.loadableClass = loadableClass;
        this.clientOnly = clientOnly;
    }

    public Class<? extends ILoadableClass> getLoadableClass() {
        return this.loadableClass;
    }

    public boolean isClientOnly() {
        return this.clientOnly;
    }
}
