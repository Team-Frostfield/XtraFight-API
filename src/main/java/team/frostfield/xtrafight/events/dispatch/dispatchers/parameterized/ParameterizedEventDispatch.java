package team.frostfield.xtrafight.events.dispatch.dispatchers.parameterized;

import org.jetbrains.annotations.ApiStatus.Experimental;
import team.frostfield.xtrafight.cls.functions.Action;
import team.frostfield.xtrafight.cls.lists.BasicArrayList;
import team.frostfield.xtrafight.cls.lists.BasicList;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.EventDispatch;
import team.frostfield.xtrafight.events.dispatch.dispatchers.entires.ParameterizedListenerEntry;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventPriority;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.side.EventSide;
import team.frostfield.xtrafight.main.XtraFightAPI;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

@Experimental
public class ParameterizedEventDispatch<T extends DispatchEvent & ParameterizedEvent, P extends EventParameters> extends EventDispatch<T> {
    protected final BasicList<ParameterizedListenerEntry> parameterizedEntries = new BasicArrayList<>();

    protected ParameterizedEventDispatch() {
        super(EventSide.BOTH);
    }

    protected ParameterizedEventDispatch(EventSide side) {
        super(side);
    }

    public static <T extends DispatchEvent & ParameterizedEvent, P extends EventParameters> ParameterizedEventDispatch<T, P> createParameterized() {
        return new ParameterizedEventDispatch<>();
    }

    public static <T extends DispatchEvent & ParameterizedEvent, P extends EventParameters> ParameterizedEventDispatch<T, P> createParameterized(EventSide side) {
        return new ParameterizedEventDispatch<>(side);
    }

    public ParameterizedEventDispatch<T, P> dispatchParameters(P listener) {
        return this.dispatchParameters(listener, EventPriority.MIDDLE);
    }

    public ParameterizedEventDispatch<T, P> dispatchParameters(P listener, EventPriority priority) {
        Objects.requireNonNull(listener, "Listener cannot be null");

        if (priority == EventPriority.OVERRIDE) {
            if (!this.event.canBeOverridden()) {
                XtraFightAPI.LOGGER.error("Cannot override event {}, not overridable!", this.event.getEventClsName());
                return this;
            }
            this.parameterizedEntries.removeIf(entry -> entry.priority() != EventPriority.OVERRIDE && this.event.canBeOverridden());
        }

        int sequence = this.sequenceGenerator.incrementAndGet();
        this.addListener(new ParameterizedListenerEntry(listener, priority, sequence));

        this.parameterizedEntries.sort(
                Comparator.<ParameterizedListenerEntry>comparingInt(entry -> entry.priority().ordinal())
                        .reversed()
                        .thenComparingInt(ParameterizedListenerEntry::sequence)
        );

        return this;
    }

    public void postEventParameterized(Action<P> action) {
        Objects.requireNonNull(action, "Action cannot be null");

        if (!this.side.shouldDispatch() || this.parameterizedEntries.isEmpty()) {
            return;
        }

        for (ParameterizedListenerEntry entry : this.parameterizedEntries) {
            action.act((P) entry.params());
        }
    }

    public void postWithParams(T dispatchableEvent, Action<P> params) {
        super.postEvent(dispatchableEvent);
        this.postEventParameterized(params);
    }

    public boolean postWithParamsAndCheckCanceled(T dispatchableEvent, Action<P> params) {
        this.postEventParameterized(params);
        return super.postAndCheckCanceled(dispatchableEvent);
    }

    public boolean postAndCheckCanceled(Predicate<P> action) {
        Objects.requireNonNull(action, "Action cannot be null");

        if (!this.side.shouldDispatch() || this.parameterizedEntries.isEmpty()) {
            return false;
        }

        for (ParameterizedListenerEntry entry : this.parameterizedEntries) {
            if (action.test((P) entry.params())) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void clearListeners() {
        this.parameterizedEntries.clear();
    }

    public void addListener(ParameterizedListenerEntry entry) {
        this.parameterizedEntries.add(entry);
    }
}