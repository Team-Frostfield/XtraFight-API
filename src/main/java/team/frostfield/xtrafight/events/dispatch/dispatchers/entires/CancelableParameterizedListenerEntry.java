package team.frostfield.xtrafight.events.dispatch.dispatchers.entires;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameterize;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventPriority;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;

public record CancelableParameterizedListenerEntry<T extends DispatchEvent & ParameterizedEvent>(
        EventParameterize<T> params,
        EventPriority priority,
        int sequence,
        boolean receiveCancelled
) {}
