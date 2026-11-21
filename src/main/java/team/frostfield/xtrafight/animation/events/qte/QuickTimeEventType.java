package team.frostfield.xtrafight.animation.events.qte;

import team.frostfield.xtrafight.builders.minecraft.client.KeyMappingsBuilder.InputType;
import team.frostfield.xtrafight.input.InputKeyCodes;
import team.frostfield.xtrafight.utils.AdvancedRandom;

public enum QuickTimeEventType {
    FAST_CLICK(InputType.MOUSE, AdvancedRandom.between(InputKeyCodes.getFirstKey(InputType.MOUSE), InputKeyCodes.getLastKey(InputType.MOUSE))),
    RANDOM_KEYBINDS(InputType.KEYBOARD, AdvancedRandom.between(InputKeyCodes.getFirstKey(InputType.KEYBOARD), InputKeyCodes.getLastKey(InputType.KEYBOARD))),
    KEYBIND_SET(InputType.KEYBOARD);
    
    QuickTimeEventType(InputType type, int... keyCodes) {}

}
