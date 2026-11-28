package team.frostfield.xtrafight.events.bus.impl;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.IEventBusInvokeDispatcher;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import org.jetbrains.annotations.ApiStatus.Internal;
import team.frostfield.xtrafight.events.bus.ModBus;

import java.util.Objects;
import java.util.function.Consumer;

@Internal
public class ModBusImpl implements ModBus {
    private String id;

    private final IEventBus originalBus;

    public ModBusImpl() {
        this(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public ModBusImpl(String modID) {
        this.id = modID;
        FMLModContainer container = (FMLModContainer) ModList.get().getModContainerById(modID).get();
        this.originalBus = container.getEventBus();
    }

    @Override
    public String getModID() {
        return this.id;
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
    public <T extends Event> void addListener(EventPriority priority, Consumer<T> consumer) {
        this.originalBus.addListener(priority, consumer);
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
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, Consumer<T> consumer) {
        this.originalBus.addGenericListener(genericClassFilter, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, Consumer<T> consumer) {
        this.originalBus.addGenericListener(genericClassFilter, priority, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCanceled, Consumer<T> consumer) {
        this.originalBus.addGenericListener(genericClassFilter, priority, receiveCanceled, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer) {
        this.originalBus.addGenericListener(genericClassFilter, priority, receiveCanceled, eventType, consumer);
    }

    @Override
    public void unregister(Object object) {
        this.originalBus.unregister(object);
    }

    @Override
    public boolean post(Event event) {
        return this.originalBus.post(event);
    }

    @Override
    public boolean post(Event event, IEventBusInvokeDispatcher wrapper) {
        return this.originalBus.post(event, wrapper);
    }

    @Override
    public void shutdown() {
        this.originalBus.shutdown();
    }

    @Override
    public void start() {
        this.originalBus.start();
    }
}