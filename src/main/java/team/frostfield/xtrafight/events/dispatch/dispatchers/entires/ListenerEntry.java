package team.frostfield.xtrafight.events.dispatch.dispatchers.entires;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventDispatcher;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventPriority;

public record ListenerEntry<T extends DispatchEvent>(EventDispatcher<T> dispatcher, EventPriority priority, int sequence) {}
