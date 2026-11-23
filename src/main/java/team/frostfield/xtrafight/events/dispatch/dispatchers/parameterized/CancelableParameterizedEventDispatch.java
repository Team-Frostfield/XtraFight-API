package team.frostfield.xtrafight.events.dispatch.dispatchers.parameterized;

import org.jetbrains.annotations.ApiStatus.Experimental;
import team.frostfield.xtrafight.cls.functions.Action;
import team.frostfield.xtrafight.cls.lists.BasicArrayList;
import team.frostfield.xtrafight.cls.lists.BasicList;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.entires.CancelableParameterizedListenerEntry;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventPriority;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.side.EventSide;
import team.frostfield.xtrafight.main.XtraFightAPI;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

@Experimental
public class CancelableParameterizedEventDispatch<T extends DispatchEvent & ParameterizedEvent, P extends EventParameters> extends ParameterizedEventDispatch<T, P> {
    protected final BasicList<CancelableParameterizedListenerEntry> cancelableEntries = new BasicArrayList<>();

    private CancelableParameterizedEventDispatch() {
        super(EventSide.BOTH);
    }

    private CancelableParameterizedEventDispatch(EventSide side) {
        super(side);
    }

    public static <T extends DispatchEvent & ParameterizedEvent, P extends EventParameters> CancelableParameterizedEventDispatch<T, P> createCancelable() {
        return new CancelableParameterizedEventDispatch<>();
    }

    public static <T extends DispatchEvent & ParameterizedEvent, P extends EventParameters> CancelableParameterizedEventDispatch<T, P> createCancelable(EventSide side) {
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
            if (!this.event.canBeOverridden()) {
                XtraFightAPI.LOGGER.error("Cannot override event {}, not overridable!", this.event.getEventClsName());
            }
            this.cancelableEntries.removeIf(entry -> entry.priority() != EventPriority.OVERRIDE && this.event.canBeOverridden());
        }

        int sequence = this.sequenceGenerator.incrementAndGet();
        this.addListener(new CancelableParameterizedListenerEntry(listener, priority, sequence, receiveCancelled));

        this.cancelableEntries.sort(
                Comparator.<CancelableParameterizedListenerEntry>comparingInt(entry -> entry.priority().ordinal())
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

        for (CancelableParameterizedListenerEntry entry : this.cancelableEntries) {
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
        for (CancelableParameterizedListenerEntry entry : this.cancelableEntries) {
            if (canceled && !entry.receiveCancelled()) {
                continue;
            }
            if (action.test((P) entry.params())) {
                canceled = true;
            }
        }

        return canceled;
    }

    public void addListener(CancelableParameterizedListenerEntry entry) {
        this.cancelableEntries.add(entry);
    }
}