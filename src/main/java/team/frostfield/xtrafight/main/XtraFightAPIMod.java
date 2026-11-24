package team.frostfield.xtrafight.main;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import team.frostfield.xtrafight.cls.loadable.LoadableClassManager;
import team.frostfield.xtrafight.cls.loadable.enums.XtraFightLoadableEnums;
import team.frostfield.xtrafight.compat.plugin.CompatibilityPlugin;
import team.frostfield.xtrafight.compat.plugin.CompatibilityPluginsManager;
import team.frostfield.xtrafight.compat.plugin.xtrafight.XtraFightAPIPlugins;
import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.events.xtrafight_api.dispatch.DispatchingEvent;
import team.frostfield.xtrafight.input.XtraFightAPIInputActions;
import team.frostfield.xtrafight.skills.XtraFightAPISkillCategories;
import team.frostfield.xtrafight.skills.XtraFightSkillSlots;
import team.frostfield.xtrafight.utils.EnvironmentHelper;
import yesman.epicfight.api.client.input.action.InputAction;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

import java.util.Arrays;

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

        Class<? extends CompatibilityPlugin>[] plugins = Arrays.stream(XtraFightAPIPlugins.values()).map(XtraFightAPIPlugins::getPluginCls).<Class<? extends CompatibilityPlugin>>toArray(Class[]::new);
        if (plugins.length != 0) {
            CompatibilityPluginsManager.registerPlugins(this.modBus, plugins);
        }
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
}
