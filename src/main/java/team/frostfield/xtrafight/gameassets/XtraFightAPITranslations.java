package team.frostfield.xtrafight.gameassets;

import net.minecraft.network.chat.Component;
import team.frostfield.xtrafight.client.text.XtraFightAPIComponent;

// Stores translation keys, names, any kind of text-asset
public final class XtraFightAPITranslations {
    private XtraFightAPITranslations() {}
    private static final String HEAVY_ATTACK_COMMON = "Heavy attack";
    private static final String COUNTER_ATTACK_COMMON = "Counter attack";

    public static final Component KEY_HEAVY_ATTACK = XtraFightAPIComponent.keybind(HEAVY_ATTACK_COMMON);
    public static final Component KEY_COUNTER_ATTACK = XtraFightAPIComponent.keybind(COUNTER_ATTACK_COMMON);
    public static final Component SKILL_CATEGORY_HEAVY_ATTACK = XtraFightAPIComponent.skillCategory(HEAVY_ATTACK_COMMON);
    public static final Component SKILL_CATEGORY_COUNTER_ATTACK = XtraFightAPIComponent.skillCategory(COUNTER_ATTACK_COMMON);

}
