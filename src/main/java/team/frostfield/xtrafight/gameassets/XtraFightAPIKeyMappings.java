package team.frostfield.xtrafight.gameassets;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import team.frostfield.xtrafight.builders.minecraft.client.KeyMappingsBuilder;
import team.frostfield.xtrafight.builders.minecraft.client.KeyMappingsBuilder.InputType;
import team.frostfield.xtrafight.builders.minecraft.client.KeyMappingsBuilder.KeyCategory;
import team.frostfield.xtrafight.cls.lists.BasicList;
import team.frostfield.xtrafight.cls.lists.BasicArrayList;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;
import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.input.InputKeyCodes;
import team.frostfield.xtrafight.main.XtraFightAPIMod;
import yesman.epicfight.client.input.CombatKeyMapping;


public class XtraFightAPIKeyMappings implements ILoadableClass {
    private static final BasicList<KeyMapping> KEY_MAPPINGS = new BasicArrayList<>();

    @Invoke
    private XtraFightAPIKeyMappings(ModBus modBus) {
        modBus.addListener(this::onKeysRegistry);
    }

    public static final CombatKeyMapping HEAVY_ATTACK = XtraFightAPIKeyMappings.registerCombatKey(
            XtraFightAPITranslations.KEY_HEAVY_ATTACK,
            InputType.MOUSE,
            InputKeyCodes.MOUSE_LEFT_CLICK
    );

    public static final CombatKeyMapping COUNTER_ATTACK = XtraFightAPIKeyMappings.registerCombatKey(
            XtraFightAPITranslations.KEY_COUNTER_ATTACK,
            InputType.KEYBOARD,
            InputKeyCodes.KEY_X
    );;

    private static KeyMapping registerKey(Component name, InputType inputType, int keyCode, KeyCategory category) {
        KeyMapping keyMapping = KeyMappingsBuilder.newKeyMapping(name.getString(), keyCode, inputType, category);
        if (KEY_MAPPINGS.contains(keyMapping)) {
            XtraFightAPIMod.errIfDevSide("Keymapping: [{}] is already registered!", name.getString());
            return null;
        }
        KEY_MAPPINGS.add(keyMapping);
        return keyMapping;
    }

    private static CombatKeyMapping registerCombatKey(Component name, InputType inputType, int keyCode) {
        CombatKeyMapping keyMapping = KeyMappingsBuilder.newCombatKeyMapping(name.getString(), inputType, keyCode, KeyCategory.EPICFIGHT_COMBAT);
        if (KEY_MAPPINGS.contains(keyMapping)) {
            XtraFightAPIMod.errIfDevSide("Keymapping: [{}] is already registered!", name.getString());
            return null;
        }
        KEY_MAPPINGS.add(keyMapping);
        return keyMapping;
    }

    private void onKeysRegistry(RegisterKeyMappingsEvent event) {
        for (KeyMapping keyMapping : KEY_MAPPINGS) {
            event.register(keyMapping);
        }
    }

}