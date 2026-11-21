package team.frostfield.xtrafight.events.bus.impl;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.ApiStatus.Internal;
import team.frostfield.xtrafight.events.bus.ModBus;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Internal
public class ModBusImpl implements ModBus {

    private final IEventBus originalBus;

    public ModBusImpl(String modID) {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(modID);
        if (container.isPresent()) {
            this.originalBus = container.get().getEventBus();
        } else {
            throw new IllegalArgumentException("ModContainer for mod-id '" + modID + "' was not found!");
        }
    }

    public ModBusImpl(IEventBus originalBus) {
        this.originalBus = Objects.requireNonNull(originalBus, "Original bus cannot be null");
    }

    public IEventBus getOriginalBus() {
        return this.originalBus;
    }

    public void run(Runnable task) {
        task.run();
    }

    @Override
    public void register(Object target) {
        this.originalBus.register(target);
    }

    @Override
    public <T extends Event> void addListener(Consumer<T> consumer) {
        this.originalBus.addListener(consumer);
    }

    @Override
    public <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer) {
        this.originalBus.addListener(eventType, consumer);
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, Consumer<T> consumer) {
        this.originalBus.addListener(priority, consumer);
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, Class<T> eventType, Consumer<T> consumer) {
        this.originalBus.addListener(priority, eventType, consumer);
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, boolean receiveCanceled, Consumer<T> consumer) {
        this.originalBus.addListener(priority, receiveCanceled, consumer);
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer) {
        this.originalBus.addListener(priority, receiveCanceled, eventType, consumer);
    }

    @Override
    public <T extends Event> void addListener(boolean receiveCanceled, Consumer<T> consumer) {
        this.originalBus.addListener(receiveCanceled, consumer);
    }

    @Override
    public <T extends Event> void addListener(boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer) {
        this.originalBus.addListener(receiveCanceled, eventType, consumer);
    }

    @Override
    public void unregister(Object object) {
        this.originalBus.unregister(object);
    }

    @Override
    public <T extends Event> T post(T event) {
        return this.originalBus.post(event);
    }

    @Override
    public <T extends Event> T post(EventPriority phase, T event) {
        return this.originalBus.post(phase, event);
    }

    @Override
    public void start() {
        this.originalBus.start();
    }
}