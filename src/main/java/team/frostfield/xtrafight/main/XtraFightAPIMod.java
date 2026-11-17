package team.frostfield.xtrafight.main;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import team.frostfield.xtrafight.cls.loadable.LoadableClassManager;
import team.frostfield.xtrafight.cls.loadable.enums.XtraFightLoadableEnums;
import team.frostfield.xtrafight.compat.plugin.CompatibilityPluginsManager;
import team.frostfield.xtrafight.compat.plugin.ExamplePlugin;
import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.events.bus.NeoBus;
import team.frostfield.xtrafight.events.xtrafight_api.dispatch.DispatchingEvent;
import team.frostfield.xtrafight.input.XtraFightAPIInputActions;
import team.frostfield.xtrafight.skills.XtraFightAPISkillCategories;
import team.frostfield.xtrafight.skills.XtraFightSkillSlots;
import team.frostfield.xtrafight.utils.EnvironmentHelper;
import yesman.epicfight.api.client.input.action.InputAction;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

@Mod(XtraFightAPI.MOD_ID)
public final class XtraFightAPIMod implements XtraFightAPI {
    private final ModContainer container;
    private final ModBus modBus;
    static XtraFightAPIMod instance;

    public XtraFightAPIMod(IEventBus modBus, ModContainer container) {
        instance = this;
        this.modBus = ModBus.from(modBus);
        this.container = container;
        this.doEpicAPIStuff();
    }

    private void doEpicAPIStuff() {
        DispatchingEvent.postEvent();
        LoadableClassManager.loadEnum(this.modBus, XtraFightLoadableEnums.values());
        CompatibilityPluginsManager.registerPlugins(this.modBus, ExamplePlugin.class);
        SkillSlot.ENUM_MANAGER.registerEnumCls(XtraFightAPI.MOD_ID, XtraFightSkillSlots.class);
        SkillCategory.ENUM_MANAGER.registerEnumCls(XtraFightAPI.MOD_ID, XtraFightAPISkillCategories.class);
        InputAction.ENUM_MANAGER.registerEnumCls(XtraFightAPI.MOD_ID, XtraFightAPIInputActions.class);
    }

    @Override
    public boolean isDeveloperEnvironment() {
        return EnvironmentHelper.isDevelopmentEnvironment();
    }

    public ModContainer getModContainer() {
        return this.container;
    }

    public ModBus getModBus() {
        return this.modBus;
    }

    public ResourceLocation identifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(XtraFightAPI.MOD_ID, path);
    }

    @Override
    public NeoBus getNeoBus() {
        return NeoBus.EVENT_BUS;
    }

    // Logger helpers
    public static void err(String message, Object... args) {
        XtraFightAPI.LOGGER.error(message, args);
    }

    public static void errIfDevSide(String message, Object... args) {
        if (EnvironmentHelper.getEnvironment().isDevEnv()) {
            XtraFightAPI.LOGGER.error(message, args);
        }
    }

    public static void warn(String message, Object... args) {
        XtraFightAPI.LOGGER.warn(message, args);
    }

    public static void debug(String message, Object... args) {
        XtraFightAPI.LOGGER.debug(message, args);
    }

    public static void debugIfDevSide(String message, Object... args) {
        if (EnvironmentHelper.getEnvironment().isDevEnv()) {
            XtraFightAPI.LOGGER.debug(message, args);
        }
    }

    public static void info(String message, Object... args) {
        XtraFightAPI.LOGGER.info(message, args);
    }
}
