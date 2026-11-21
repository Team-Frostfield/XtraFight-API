package team.frostfield.xtrafight.events.dispatch.dispatchers.parameterized;

import org.jetbrains.annotations.ApiStatus.Experimental;
import team.frostfield.xtrafight.cls.functions.Action;
import team.frostfield.xtrafight.cls.lists.BasicArrayList;
import team.frostfield.xtrafight.cls.lists.BasicList;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.entires.ParameterizedListenerEntry;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameterize;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventPriority;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.side.EventSide;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Experimental
public class ParameterizedEventDispatch<T extends DispatchEvent & ParameterizedEvent, P extends EventParameterize<T>> {
    protected final BasicList<ParameterizedListenerEntry<T>> parameterizedEntries = new BasicArrayList<>();
    protected final EventSide side;
    protected final AtomicInteger sequenceGenerator = new AtomicInteger();

    protected ParameterizedEventDispatch() {
        this(EventSide.BOTH);
    }

    protected ParameterizedEventDispatch(EventSide side) {
        this.side = Objects.requireNonNull(side, "EventSide cannot be null");
    }

    public static <T extends DispatchEvent & ParameterizedEvent, P extends EventParameterize<T>> ParameterizedEventDispatch<T, P> createParameterized() {
        return new ParameterizedEventDispatch<>();
    }

    public static <T extends DispatchEvent & ParameterizedEvent, P extends EventParameterize<T>> ParameterizedEventDispatch<T, P> createParameterized(EventSide side) {
        return new ParameterizedEventDispatch<>(side);
    }

    public ParameterizedEventDispatch<T, P> dispatchEvent(P listener) {
        return this.dispatchEvent(listener, EventPriority.MIDDLE);
    }

    public ParameterizedEventDispatch<T, P> dispatchEvent(P listener, EventPriority priority) {
        Objects.requireNonNull(listener, "Listener cannot be null");

        if (priority == EventPriority.OVERRIDE) {
            this.parameterizedEntries.removeIf(entry -> entry.priority() != EventPriority.OVERRIDE);
        }

        int sequence = this.sequenceGenerator.incrementAndGet();
        this.addListener(new ParameterizedListenerEntry<>(listener, priority, sequence));

        this.parameterizedEntries.sort(
                Comparator.<ParameterizedListenerEntry<T>>comparingInt(entry -> entry.priority().ordinal())
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

        for (ParameterizedListenerEntry<T> entry : this.parameterizedEntries) {
            action.act((P) entry.params());
        }
    }


    public boolean postAndCheckCanceled(Predicate<P> action) {
        Objects.requireNonNull(action, "Action cannot be null");

        if (!this.side.shouldDispatch() || this.parameterizedEntries.isEmpty()) {
            return false;
        }

        for (ParameterizedListenerEntry<T> entry : this.parameterizedEntries) {
            if (action.test((P) entry.params())) {
                return true;
            }
        }

        return false;
    }

    public void addListener(ParameterizedListenerEntry<T> entry) {
        this.parameterizedEntries.add(entry);
    }
}