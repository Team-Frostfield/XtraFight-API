package team.frostfield.xtrafight.events.dispatch.dispatchers.entires;

import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventPriority;

public record ParameterizedListenerEntry(EventParameters params, EventPriority priority, int sequence) {}
