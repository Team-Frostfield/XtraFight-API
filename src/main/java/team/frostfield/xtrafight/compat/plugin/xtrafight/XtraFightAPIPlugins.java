package team.frostfield.xtrafight.compat.plugin.xtrafight;

import team.frostfield.xtrafight.compat.plugin.CompatibilityPlugin;

public enum XtraFightAPIPlugins {
    // for future use!
    ;
    private final Class<? extends CompatibilityPlugin> pluginCls;

    XtraFightAPIPlugins(Class<? extends CompatibilityPlugin> pluginCls) {
        this.pluginCls = pluginCls;
    }

    public Class<? extends CompatibilityPlugin> getPluginCls() {
        return this.pluginCls;
    }
}
