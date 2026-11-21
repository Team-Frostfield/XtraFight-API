package team.frostfield.xtrafight.cls.loadable;

import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.events.bus.NeoBus;
import team.frostfield.xtrafight.events.bus.impl.NeoBusImpl;

public interface ILoadableClass {

    /** Primary method to register Items, Blocks, Entities, etc... Called Directly. */
    default void onModConstruct(ModBus bus) {}

    /** Use to register Client-only listeners to the Mod Bus. Called Directly. */
    default void onModClientConstruct(ModBus bus) {}

    /** Use to register listeners to the global NeoForge.EVENT_BUS. Called Directly. */
    default void onGameConstruct(NeoBus bus) {}

    /** Use to register listeners to the client side of NeoForge.EVENT_BUS. Called Directly. */
    default void onGameClientConstruct(NeoBus bus) {}

    /** Overridden so the class does not log/throws an exception that it wasn't loaded */
    default boolean ignoreNotLoadedWarning() {
        return false;
    }

}