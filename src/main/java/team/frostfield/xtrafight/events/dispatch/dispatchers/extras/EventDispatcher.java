package team.frostfield.xtrafight.events.dispatch.dispatchers.extras;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;

@FunctionalInterface
public interface EventDispatcher<T extends DispatchEvent> {
    void dispatch(T event);
}
