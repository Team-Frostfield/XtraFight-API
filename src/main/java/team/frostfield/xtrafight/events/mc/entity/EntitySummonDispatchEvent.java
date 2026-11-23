package team.frostfield.xtrafight.events.mc.entity;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;

public abstract class EntitySummonDispatchEvent extends DispatchEvent implements ParameterizedEvent {

    private final Entity entity;
    private final Level level;


    public EntitySummonDispatchEvent(Entity entity, Level level) {
        this.entity = entity;
        this.level = level;
    }

    public Entity getEntity() {
        return this.entity;
    }


    public Level getLevel() {
        return this.level;
    }

    @FunctionalInterface
    public interface EntitySummonParams extends EventParameters {
        void onEntitySummon(Entity entity, Level level);
    }

    public static class Server extends EntitySummonDispatchEvent {
        public Server(Entity entity, ServerLevel level) {
            super(entity, level);
        }
    }

    public static class Client extends EntitySummonDispatchEvent {
        public Client(Entity entity, ClientLevel level) {
            super(entity, level);
        }
    }
}
