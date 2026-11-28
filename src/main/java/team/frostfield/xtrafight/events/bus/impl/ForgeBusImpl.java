package team.frostfield.xtrafight.events.bus.impl;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBusInvokeDispatcher;
import org.jetbrains.annotations.ApiStatus.Internal;
import team.frostfield.xtrafight.events.bus.ForgeBus;

import java.util.function.Consumer;

public class ForgeBusImpl implements ForgeBus {

    @Internal
    public ForgeBusImpl() {}

    public void run(Runnable task) {
        task.run();
    }

    @Override
    public void register(Object target) {
        MinecraftForge.EVENT_BUS.register(target);
    }

    @Override
    public <T extends Event> void addListener(Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addListener(consumer);
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addListener(priority, consumer);
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, boolean receiveCanceled, Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addListener(priority, receiveCanceled, consumer);
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addListener(priority, receiveCanceled, eventType, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addGenericListener(genericClassFilter, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addGenericListener(genericClassFilter, priority, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCanceled, Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addGenericListener(genericClassFilter, priority, receiveCanceled, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addGenericListener(genericClassFilter, priority, receiveCanceled, eventType, consumer);
    }

    @Override
    public void unregister(Object object) {
        MinecraftForge.EVENT_BUS.unregister(object);
    }

    @Override
    public boolean post(Event event) {
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public boolean post(Event event, IEventBusInvokeDispatcher wrapper) {
        return MinecraftForge.EVENT_BUS.post(event, wrapper);
    }

    @Override
    public void shutdown() {
        MinecraftForge.EVENT_BUS.shutdown();
    }

    @Override
    public void start() {
        MinecraftForge.EVENT_BUS.start();
    }
}