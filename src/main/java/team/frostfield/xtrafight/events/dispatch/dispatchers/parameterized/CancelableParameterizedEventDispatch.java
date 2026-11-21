package team.frostfield.xtrafight.events.dispatch.dispatchers.parameterized;

import org.jetbrains.annotations.ApiStatus.Experimental;
import team.frostfield.xtrafight.cls.functions.Action;
import team.frostfield.xtrafight.cls.lists.BasicArrayList;
import team.frostfield.xtrafight.cls.lists.BasicList;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.entires.CancelableParameterizedListenerEntry;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameterize;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventPriority;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.side.EventSide;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Experimental
public class CancelableParameterizedEventDispatch<T extends DispatchEvent & ParameterizedEvent, P extends EventParameterize<T>> {
    protected final BasicList<CancelableParameterizedListenerEntry<T>> cancelableEntries = new BasicArrayList<>();
    protected final EventSide side;
    protected final AtomicInteger sequenceGenerator = new AtomicInteger();

    private CancelableParameterizedEventDispatch() {
        this(EventSide.BOTH);
    }

    private CancelableParameterizedEventDispatch(EventSide side) {
        this.side = Objects.requireNonNull(side, "EventSide cannot be null");
    }

    public static <T extends DispatchEvent & ParameterizedEvent, P extends EventParameterize<T>> CancelableParameterizedEventDispatch<T, P> createCancelable() {
        return new CancelableParameterizedEventDispatch<>();
    }

    public static <T extends DispatchEvent & ParameterizedEvent, P extends EventParameterize<T>> CancelableParameterizedEventDispatch<T, P> createCancelable(EventSide side) {
        return new CancelableParameterizedEventDispatch<>(side);
    }

    public CancelableParameterizedEventDispatch<T, P> dispatchEvent(P listener) {
        return this.dispatchEvent(listener, EventPriority.MIDDLE, false);
    }

    public CancelableParameterizedEventDispatch<T, P> dispatchEvent(P listener, EventPriority priority) {
        return this.dispatchEvent(listener, priority, false);
    }

    public CancelableParameterizedEventDispatch<T, P> dispatchEvent(P listener, boolean receiveCancelled) {
        return this.dispatchEvent(listener, EventPriority.MIDDLE, receiveCancelled);
    }

    public CancelableParameterizedEventDispatch<T, P> dispatchEvent(P listener, EventPriority priority, boolean receiveCancelled) {
        Objects.requireNonNull(listener, "Listener cannot be null");
        Objects.requireNonNull(priority, "Priority cannot be null");

        if (priority == EventPriority.OVERRIDE) {
            this.cancelableEntries.removeIf(entry -> entry.priority() != EventPriority.OVERRIDE);
        }

        int sequence = this.sequenceGenerator.incrementAndGet();
        this.addListener(new CancelableParameterizedListenerEntry<>(listener, priority, sequence, receiveCancelled));

        this.cancelableEntries.sort(
                Comparator.<CancelableParameterizedListenerEntry<T>>comparingInt(entry -> entry.priority().ordinal())
                        .reversed()
                        .thenComparingInt(CancelableParameterizedListenerEntry::sequence)
        );

        return this;
    }

    @SuppressWarnings("unchecked")
    public void postEventParameterized(Action<P> action) {
        Objects.requireNonNull(action, "Action cannot be null");

        if (!this.side.shouldDispatch() || this.cancelableEntries.isEmpty()) {
            return;
        }

        for (CancelableParameterizedListenerEntry<T> entry : this.cancelableEntries) {
            action.act((P) entry.params());
        }
    }

    @SuppressWarnings("unchecked")
    public boolean postAndCheckCanceled(Predicate<P> action) {
        Objects.requireNonNull(action, "Action cannot be null");

        if (!this.side.shouldDispatch() || this.cancelableEntries.isEmpty()) {
            return false;
        }

        boolean canceled = false;
        for (CancelableParameterizedListenerEntry<T> entry : this.cancelableEntries) {
            if (canceled && !entry.receiveCancelled()) {
                continue;
            }
            if (action.test((P) entry.params())) {
                canceled = true;
            }
        }

        return canceled;
    }

    public void addListener(CancelableParameterizedListenerEntry<T> entry) {
        this.cancelableEntries.add(entry);
    }
}