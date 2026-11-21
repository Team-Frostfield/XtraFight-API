package team.frostfield.xtrafight.events.dispatch.dispatchers.entires;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameterize;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventPriority;

public record ParameterizedListenerEntry<T extends DispatchEvent>(EventParameterize<T> params, EventPriority priority, int sequence) {}
