package team.frostfield.xtrafight.gameassets;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import team.frostfield.xtrafight.builders.minecraft.client.KeyMappingsBuilder;
import team.frostfield.xtrafight.builders.minecraft.client.KeyMappingsBuilder.InputType;
import team.frostfield.xtrafight.builders.minecraft.client.KeyMappingsBuilder.KeyCategory;
import team.frostfield.xtrafight.cls.lists.BasicArrayList;
import team.frostfield.xtrafight.cls.lists.BasicList;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;
import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.input.InputKeyCodes;
import team.frostfield.xtrafight.main.XtraFightAPI;
import yesman.epicfight.client.input.CombatKeyMapping;


public class XtraFightAPIKeyMappings implements ILoadableClass {
    private static final BasicList<KeyMapping> KEY_MAPPINGS = new BasicArrayList<>();

    private static final KeyMapping EMPTY = new KeyMapping("", 0, "");
    private static final CombatKeyMapping COMBAT_EMPTY = new CombatKeyMapping("", 0, "");

    @Invoke
    private XtraFightAPIKeyMappings(ModBus modBus) {
        modBus.addListener(this::onKeysRegistry);
    }

    public static final CombatKeyMapping HEAVY_ATTACK = registerCombatKey(
            XtraFightAPITranslations.KEY_HEAVY_ATTACK,
            InputType.MOUSE,
            InputKeyCodes.MOUSE_LEFT_CLICK
    );

    public static final CombatKeyMapping COUNTER_ATTACK = registerCombatKey(
            XtraFightAPITranslations.KEY_COUNTER_ATTACK,
            InputType.KEYBOARD,
            InputKeyCodes.KEY_X
    );;

    private static KeyMapping registerKey(Component name, InputType inputType, int keyCode, KeyCategory category) {
        KeyMapping keyMapping = KeyMappingsBuilder.newKeyMapping(name.getString(), keyCode, inputType, category);
        if (KEY_MAPPINGS.contains(keyMapping)) {
            XtraFightAPI.LOGGER.errorIfDevSide("Keymapping: [{}] is already registered!", name.getString());
            return EMPTY;
        }
        KEY_MAPPINGS.addOnlyOnce(keyMapping);
        return keyMapping;
    }


    private static CombatKeyMapping registerCombatKey(Component name, InputType inputType, int keyCode) {
        CombatKeyMapping keyMapping = KeyMappingsBuilder.newCombatKeyMapping(name.getString(), inputType, keyCode, KeyCategory.EPICFIGHT_COMBAT);
        if (KEY_MAPPINGS.contains(keyMapping)) {
            XtraFightAPI.LOGGER.errorIfDevSide("Keymapping: [{}] is already registered!", name.getString());
            return COMBAT_EMPTY;
        }
        KEY_MAPPINGS.addOnlyOnce(keyMapping);
        return keyMapping;
    }

    private void onKeysRegistry(RegisterKeyMappingsEvent event) {
        for (KeyMapping keyMapping : KEY_MAPPINGS) {
            event.register(keyMapping);
        }
    }

}