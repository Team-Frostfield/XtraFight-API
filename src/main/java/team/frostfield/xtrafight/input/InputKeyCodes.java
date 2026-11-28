package team.frostfield.xtrafight.input;

import team.frostfield.xtrafight.builders.minecraft.client.KeyMappingsBuilder.InputType;
import team.frostfield.xtrafight.cls.lists.BasicArrayList;
import team.frostfield.xtrafight.cls.lists.BasicList;

import java.util.Collections;
import java.util.List;

public final class InputKeyCodes {

    private static final BasicList<Integer> KEYBOARD_KEYS = new BasicArrayList<>();
    private static final BasicList<Integer> MOUSE_KEYS = new BasicArrayList<>();
    private static final BasicList<Integer> INPUT_ACTIONS = new BasicArrayList<>();

    private InputKeyCodes() {}

    // --- Digits ---
    public static final int KEY_0 = 48;
    public static final int KEY_1 = 49;
    public static final int KEY_2 = 50;
    public static final int KEY_3 = 51;
    public static final int KEY_4 = 52;
    public static final int KEY_5 = 53;
    public static final int KEY_6 = 54;
    public static final int KEY_7 = 55;
    public static final int KEY_8 = 56;
    public static final int KEY_9 = 57;

    // --- Letters ---
    public static final int KEY_A = 65;
    public static final int KEY_B = 66;
    public static final int KEY_C = 67;
    public static final int KEY_D = 68;
    public static final int KEY_E = 69;
    public static final int KEY_F = 70;
    public static final int KEY_G = 71;
    public static final int KEY_H = 72;
    public static final int KEY_I = 73;
    public static final int KEY_J = 74;
    public static final int KEY_K = 75;
    public static final int KEY_L = 76;
    public static final int KEY_M = 77;
    public static final int KEY_N = 78;
    public static final int KEY_O = 79;
    public static final int KEY_P = 80;
    public static final int KEY_Q = 81;
    public static final int KEY_R = 82;
    public static final int KEY_S = 83;
    public static final int KEY_T = 84;
    public static final int KEY_U = 85;
    public static final int KEY_V = 86;
    public static final int KEY_W = 87;
    public static final int KEY_X = 88;
    public static final int KEY_Y = 89;
    public static final int KEY_Z = 90;

    // --- Function Keys ---
    public static final int KEY_F1 = 290;
    public static final int KEY_F2 = 291;
    public static final int KEY_F3 = 292;
    public static final int KEY_F4 = 293;
    public static final int KEY_F5 = 294;
    public static final int KEY_F6 = 295;
    public static final int KEY_F7 = 296;
    public static final int KEY_F8 = 297;
    public static final int KEY_F9 = 298;
    public static final int KEY_F10 = 299;
    public static final int KEY_F11 = 300;
    public static final int KEY_F12 = 301;
    public static final int KEY_F13 = 302;
    public static final int KEY_F14 = 303;
    public static final int KEY_F15 = 304;
    public static final int KEY_F16 = 305;
    public static final int KEY_F17 = 306;
    public static final int KEY_F18 = 307;
    public static final int KEY_F19 = 308;
    public static final int KEY_F20 = 309;
    public static final int KEY_F21 = 310;
    public static final int KEY_F22 = 311;
    public static final int KEY_F23 = 312;
    public static final int KEY_F24 = 313;
    public static final int KEY_F25 = 314;

    // --- Numpad ---
    public static final int KEY_NUMLOCK = 282;
    public static final int KEY_NUMPAD0 = 320;
    public static final int KEY_NUMPAD1 = 321;
    public static final int KEY_NUMPAD2 = 322;
    public static final int KEY_NUMPAD3 = 323;
    public static final int KEY_NUMPAD4 = 324;
    public static final int KEY_NUMPAD5 = 325;
    public static final int KEY_NUMPAD6 = 326;
    public static final int KEY_NUMPAD7 = 327;
    public static final int KEY_NUMPAD8 = 328;
    public static final int KEY_NUMPAD9 = 329;
    public static final int KEY_NUMPADCOMMA = 330;
    public static final int KEY_NUMPADENTER = 335;
    public static final int KEY_NUMPADEQUALS = 336;

    // --- Navigation & Direction ---
    public static final int KEY_DOWN = 264;
    public static final int KEY_LEFT = 263;
    public static final int KEY_RIGHT = 262;
    public static final int KEY_UP = 265;

    // --- Symbols & Punctuation ---
    public static final int KEY_ADD = 334;
    public static final int KEY_APOSTROPHE = 39;
    public static final int KEY_BACKSLASH = 92;
    public static final int KEY_COMMA = 44;
    public static final int KEY_EQUALS = 61;
    public static final int KEY_GRAVE = 96;
    public static final int KEY_LBRACKET = 91;
    public static final int KEY_MINUS = 45;
    public static final int KEY_MULTIPLY = 332;
    public static final int KEY_PERIOD = 46;
    public static final int KEY_RBRACKET = 93;
    public static final int KEY_SEMICOLON = 59;
    public static final int KEY_SLASH = 47;
    public static final int KEY_SPACE = 32;
    public static final int KEY_TAB = 258;

    // --- Modifiers & Utility ---
    public static final int KEY_LALT = 342;
    public static final int KEY_LCONTROL = 341;
    public static final int KEY_LSHIFT = 340;
    public static final int KEY_LWIN = 343;
    public static final int KEY_RALT = 346;
    public static final int KEY_RCONTROL = 345;
    public static final int KEY_RSHIFT = 344;
    public static final int KEY_RWIN = 347;
    public static final int KEY_RETURN = 257;
    public static final int KEY_ESCAPE = 256;
    public static final int KEY_BACKSPACE = 259;
    public static final int KEY_DELETE = 261;
    public static final int KEY_END = 269;
    public static final int KEY_HOME = 268;
    public static final int KEY_INSERT = 260;
    public static final int KEY_PAGEDOWN = 267;
    public static final int KEY_PAGEUP = 266;
    public static final int KEY_CAPSLOCK = 280;
    public static final int KEY_PAUSE = 284;
    public static final int KEY_SCROLLLOCK = 281;
    public static final int KEY_PRINTSCREEN = 283;

    // --- Input Actions ---
    public static final int PRESS = 1;
    public static final int RELEASE = 0;
    public static final int REPEAT = 2;

    // --- Mouse Buttons ---
    public static final int MOUSE_LEFT_CLICK = 0;
    public static final int MOUSE_BUTTON_MIDDLE = 2;
    public static final int MOUSE_RIGHT_CLICK = 1;

    // --- Modifiers & Cursor modes ---
    public static final int MOD_CONTROL = 2;
    public static final int CURSOR = 208897;
    public static final int CURSOR_DISABLED = 212995;
    public static final int CURSOR_NORMAL = 212993;

    static {
        MOUSE_KEYS.addAsArray(
                MOUSE_LEFT_CLICK,
                MOUSE_BUTTON_MIDDLE,
                MOUSE_RIGHT_CLICK
        );

        KEYBOARD_KEYS.addAsArray(
                KEY_0, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9,
                KEY_A, KEY_B, KEY_C, KEY_D, KEY_E, KEY_F, KEY_G, KEY_H, KEY_I, KEY_J,
                KEY_K, KEY_L, KEY_M, KEY_N, KEY_O, KEY_P, KEY_Q, KEY_R, KEY_S, KEY_T,
                KEY_U, KEY_V, KEY_W, KEY_X, KEY_Y, KEY_Z,
                KEY_F1, KEY_F2, KEY_F3, KEY_F4, KEY_F5, KEY_F6, KEY_F7, KEY_F8, KEY_F9,
                KEY_F10, KEY_F11, KEY_F12, KEY_F13, KEY_F14, KEY_F15, KEY_F16, KEY_F17,
                KEY_F18, KEY_F19, KEY_F20, KEY_F21, KEY_F22, KEY_F23, KEY_F24, KEY_F25,
                KEY_NUMLOCK, KEY_NUMPAD0, KEY_NUMPAD1, KEY_NUMPAD2, KEY_NUMPAD3, KEY_NUMPAD4,
                KEY_NUMPAD5, KEY_NUMPAD6, KEY_NUMPAD7, KEY_NUMPAD8, KEY_NUMPAD9,
                KEY_NUMPADCOMMA, KEY_NUMPADENTER, KEY_NUMPADEQUALS,
                KEY_DOWN, KEY_LEFT, KEY_RIGHT, KEY_UP,
                KEY_ADD, KEY_APOSTROPHE, KEY_BACKSLASH, KEY_COMMA, KEY_EQUALS, KEY_GRAVE,
                KEY_LBRACKET, KEY_MINUS, KEY_MULTIPLY, KEY_PERIOD, KEY_RBRACKET,
                KEY_SEMICOLON, KEY_SLASH, KEY_SPACE, KEY_TAB,
                KEY_LALT, KEY_LCONTROL, KEY_LSHIFT, KEY_LWIN,
                KEY_RALT, KEY_RCONTROL, KEY_RSHIFT, KEY_RWIN,
                KEY_RETURN, KEY_ESCAPE, KEY_BACKSPACE, KEY_DELETE,
                KEY_END, KEY_HOME, KEY_INSERT, KEY_PAGEDOWN, KEY_PAGEUP,
                KEY_CAPSLOCK, KEY_PAUSE, KEY_SCROLLLOCK, KEY_PRINTSCREEN
        );

        INPUT_ACTIONS.addAsArray(
                PRESS,
                RELEASE,
                REPEAT
        );
    }

    public static List<Integer> getKeyboardKeys() {
        return Collections.unmodifiableList(KEYBOARD_KEYS);
    }

    public static List<Integer> getMouseKeys() {
        return Collections.unmodifiableList(MOUSE_KEYS);
    }

    public static List<Integer> getInputActions() {
        return Collections.unmodifiableList(INPUT_ACTIONS);
    }

    public static int getFirstKey(InputType type) {
        switch (type) {
            case KEYBOARD -> {
                return KEYBOARD_KEYS.getFirst();
            }
            case MOUSE -> {
                return MOUSE_KEYS.getFirst();
            }
            case SCANCODE -> throw new IllegalArgumentException("No keys are present for scancode input type");
        }
        return KEYBOARD_KEYS.getFirst();
    }

    public static int getLastKey(InputType type) {
        switch (type) {
            case KEYBOARD -> {
                return KEYBOARD_KEYS.getLast();
            }
            case MOUSE -> {
                return MOUSE_KEYS.getLast();
            }
            case SCANCODE -> {
                throw new IllegalArgumentException("No keys are present for scancode input type");
            }
        }
        return KEYBOARD_KEYS.getFirst();
    }

    public static boolean isInputAction(int keyCode) {
        return INPUT_ACTIONS.contains(keyCode);
    }

    public static boolean isKeyboardKey(int keyCode) {
        return KEYBOARD_KEYS.contains(keyCode);
    }

    public static boolean isMouseButton(int buttonCode) {
        return MOUSE_KEYS.contains(buttonCode);
    }
}