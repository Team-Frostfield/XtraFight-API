package team.frostfield.xtrafight.client.text;

import net.minecraft.network.chat.Component;
import team.frostfield.xtrafight.utils.StringUtils;

public interface XtraFightAPIComponent {
    static Component keybind(String name) {
        return Component.translatable("key.xtrafight." + StringUtils.toLowerCaseReplaceSpace(name));
    }

    static Component skillCategory(String name) {
        return Component.translatable("skill.xtrafight.category." + StringUtils.toLowerCaseReplaceSpace(name));
    }

    static Component item(String name) {
        return Component.translatable("item.xtrafight." + StringUtils.toLowerCaseReplaceSpace(name));
    }

    static Component skill(String name) {
        return Component.translatable("skill.xtrafight." + StringUtils.toLowerCaseReplaceSpace(name));
    }

    static Component entity(String name) {
        return Component.translatable("entity.xtrafight." + StringUtils.toLowerCaseReplaceSpace(name));
    }

    static Component effect(String name) {
        return Component.translatable("effect.xtrafight." + StringUtils.toLowerCaseReplaceSpace(name));
    }

    static Component block(String name) {
        return Component.translatable("block.xtrafight." + StringUtils.toLowerCaseReplaceSpace(name));
    }

    static Component gui(String name) {
        return Component.translatable("gui.xtrafight." + StringUtils.toLowerCaseReplaceSpace(name));
    }

    static Component message(String name) {
        return Component.translatable("message.xtrafight." + StringUtils.toLowerCaseReplaceSpace(name));
    }
}
