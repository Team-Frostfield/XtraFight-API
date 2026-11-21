package team.frostfield.xtrafight.events.mc;

import org.jetbrains.annotations.ApiStatus.Experimental;
import team.frostfield.xtrafight.events.dispatch.dispatchers.EventDispatch;
import team.frostfield.xtrafight.events.dispatch.dispatchers.parameterized.ParameterizedEventDispatch;
import team.frostfield.xtrafight.events.dispatch.side.EventSide;
import team.frostfield.xtrafight.events.mc.client.ClientInitializeDispatchEvent;
import team.frostfield.xtrafight.events.mc.client.ClientInitializeDispatchEvent.Post.ClientStartPostParams;
import team.frostfield.xtrafight.events.mc.client.ClientTickDispatchEvent;
import team.frostfield.xtrafight.events.mc.client.WorldLoadDispatchEvent;
import team.frostfield.xtrafight.events.mc.entity.EntitySummonDispatchEvent;
import team.frostfield.xtrafight.events.mc.player.PlayerJoinDispatchEvent;
import team.frostfield.xtrafight.events.mc.player.PlayerTickDispatchEvent;
import team.frostfield.xtrafight.events.mc.server.ServerInitializeDispatchEvent;
import team.frostfield.xtrafight.events.mc.server.ServerStopDispatchEvent;
import team.frostfield.xtrafight.events.mc.server.ServerTickDispatchEvent;

@Experimental
public class MinecraftDispatchableEvents {

    public static class Server {
        // fired when the server is loading/starting
        public static final EventDispatch<ServerInitializeDispatchEvent.Pre> SERVER_START_PRE = EventDispatch.createDispatchable(EventSide.SERVER);
        // fired when the server is started and running
        public static final EventDispatch<ServerInitializeDispatchEvent.Post> SERVER_START_POST = EventDispatch.createDispatchable(EventSide.SERVER);
        // fired when the server shuts off (maybe when it crashes too
        public static final EventDispatch<ServerStopDispatchEvent> SERVER_STOP = EventDispatch.createDispatchable(EventSide.SERVER);
        // fired when the server is online and ticking
        public static final EventDispatch<ServerTickDispatchEvent> SERVER_TICK = EventDispatch.createDispatchable(EventSide.SERVER);
    }

    public static class Player {
        // both fired when the player is ticking in the world
        public static final EventDispatch<PlayerTickDispatchEvent.Pre> TICK_PRE = EventDispatch.createDispatchable();
        public static final EventDispatch<PlayerTickDispatchEvent.Post> TICK_POST = EventDispatch.createDispatchable();
        // fired when the player joins a client-side world (single-player/integrated server)
        public static final EventDispatch<PlayerJoinDispatchEvent.Client> JOIN_CLIENT = EventDispatch.createDispatchable(EventSide.CLIENT);
        // fired when the player joins on a public server
        public static final EventDispatch<PlayerJoinDispatchEvent.Server> JOIN_SERVER = EventDispatch.createDispatchable(EventSide.SERVER);

    }

    public static class Client {
        // fired when the world is being initialized
        public static final EventDispatch<WorldLoadDispatchEvent.Initialize> WORLD_INIT = EventDispatch.createDispatchable(EventSide.CLIENT);
        // fired when the player joins an already existing world
        public static final EventDispatch<WorldLoadDispatchEvent.Join> WORLD_JOIN = EventDispatch.createDispatchable(EventSide.CLIENT);
        // fired when the player creates and joins a world
        public static final EventDispatch<WorldLoadDispatchEvent.Create> WORLD_CREATE = EventDispatch.createDispatchable(EventSide.CLIENT);
        // both fired when the player initializes the client
        public static final EventDispatch<ClientInitializeDispatchEvent.Pre> CLIENT_START_PRE = EventDispatch.createDispatchable(EventSide.CLIENT);
        public static final EventDispatch<ClientInitializeDispatchEvent.Post> CLIENT_START_POST = EventDispatch.createDispatchable(EventSide.CLIENT);
        public static final ParameterizedEventDispatch<ClientInitializeDispatchEvent.Post, ClientStartPostParams> CLIENT_START_POST_P = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);
        // fired when the client is initialized and ticking
        public static final EventDispatch<ClientTickDispatchEvent> CLIENT_TICK = EventDispatch.createDispatchable(EventSide.CLIENT);
    }

    public static class Entity {
        // fired when an entity is summoned in the client world
        public static final EventDispatch<EntitySummonDispatchEvent.Client> SUMMON_CLIENT = EventDispatch.createDispatchable(EventSide.CLIENT);
        // fired when an entity is summoned in the server's world
        public static final EventDispatch<EntitySummonDispatchEvent.Server> SUMMON_SERVER = EventDispatch.createDispatchable(EventSide.SERVER);
    }
}