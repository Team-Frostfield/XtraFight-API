package team.frostfield.xtrafight.events.mc;

import org.jetbrains.annotations.ApiStatus.Experimental;
import team.frostfield.xtrafight.events.dispatch.dispatchers.parameterized.ParameterizedEventDispatch;
import team.frostfield.xtrafight.events.dispatch.side.EventSide;
import team.frostfield.xtrafight.events.mc.client.ClientInitializeDispatchEvent;
import team.frostfield.xtrafight.events.mc.client.ClientInitializeDispatchEvent.ClientInitParams;
import team.frostfield.xtrafight.events.mc.client.ClientTickDispatchEvent;
import team.frostfield.xtrafight.events.mc.client.ClientTickDispatchEvent.ClientTickParams;
import team.frostfield.xtrafight.events.mc.client.WorldLoadDispatchEvent;
import team.frostfield.xtrafight.events.mc.client.WorldLoadDispatchEvent.WorldLoadParams;
import team.frostfield.xtrafight.events.mc.entity.EntitySummonDispatchEvent;
import team.frostfield.xtrafight.events.mc.entity.EntitySummonDispatchEvent.EntitySummonParams;
import team.frostfield.xtrafight.events.mc.player.PlayerJoinDispatchEvent;
import team.frostfield.xtrafight.events.mc.player.PlayerJoinDispatchEvent.PlayerJoinWorldParams;
import team.frostfield.xtrafight.events.mc.player.PlayerTickDispatchEvent;
import team.frostfield.xtrafight.events.mc.player.PlayerTickDispatchEvent.PlayerTickParams;
import team.frostfield.xtrafight.events.mc.server.ServerInitializeDispatchEvent;
import team.frostfield.xtrafight.events.mc.server.ServerInitializeDispatchEvent.ServerInitParams;
import team.frostfield.xtrafight.events.mc.server.ServerStopDispatchEvent;
import team.frostfield.xtrafight.events.mc.server.ServerStopDispatchEvent.ServerStopParams;
import team.frostfield.xtrafight.events.mc.server.ServerTickDispatchEvent;
import team.frostfield.xtrafight.events.mc.server.ServerTickDispatchEvent.ServerTickParams;

@Experimental
public class MinecraftDispatchableEvents {

    public static class Server {
        // fired when the server is loading/starting
        public static final ParameterizedEventDispatch<ServerInitializeDispatchEvent.Pre, ServerInitParams> START_PRE = ParameterizedEventDispatch.createParameterized(EventSide.SERVER);
        // fired when the server is started and running
        public static final ParameterizedEventDispatch<ServerInitializeDispatchEvent.Post, ServerInitParams> START_POST = ParameterizedEventDispatch.createParameterized(EventSide.SERVER);
        // fired when the server shuts off (maybe when it crashes too
        public static final ParameterizedEventDispatch<ServerStopDispatchEvent, ServerStopParams> STOP = ParameterizedEventDispatch.createParameterized(EventSide.SERVER);
        // fired when the server is online and ticking
        public static final ParameterizedEventDispatch<ServerTickDispatchEvent, ServerTickParams> TICK = ParameterizedEventDispatch.createParameterized(EventSide.SERVER);
    }

    public static class Player {
        // both fired when the player is ticking in the world
        public static final ParameterizedEventDispatch<PlayerTickDispatchEvent.Pre, PlayerTickParams> TICK_PRE = ParameterizedEventDispatch.createParameterized();
        public static final ParameterizedEventDispatch<PlayerTickDispatchEvent.Post, PlayerTickParams> TICK_POST = ParameterizedEventDispatch.createParameterized();
        // fired when the player joins a client-side world (single-player/integrated server)
        public static final ParameterizedEventDispatch<PlayerJoinDispatchEvent.Client, PlayerJoinWorldParams> JOIN_CLIENT = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);
        // fired when the player joins on a public server
        public static final ParameterizedEventDispatch<PlayerJoinDispatchEvent.Server, PlayerJoinWorldParams> JOIN_SERVER = ParameterizedEventDispatch.createParameterized(EventSide.SERVER);

    }

    public static class Client {
        // fired when the world is being initialized
        public static final ParameterizedEventDispatch<WorldLoadDispatchEvent.Initialize, WorldLoadParams> WORLD_INIT = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);
        // fired when the player joins an already existing world
        public static final ParameterizedEventDispatch<WorldLoadDispatchEvent.Join, WorldLoadParams> WORLD_JOIN = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);
        // fired when the player creates and joins a world
        public static final ParameterizedEventDispatch<WorldLoadDispatchEvent.Create, WorldLoadParams> WORLD_CREATE = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);
        // both fired when the player initializes the client
        public static final ParameterizedEventDispatch<ClientInitializeDispatchEvent.Pre, ClientInitParams> START_PRE = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);
        public static final ParameterizedEventDispatch<ClientInitializeDispatchEvent.Post, ClientInitParams> START_POST = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);

        // fired when the client is initialized and ticking
        public static final ParameterizedEventDispatch<ClientTickDispatchEvent, ClientTickParams> TICK = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);
    }

    public static class Entity {
        // fired when an entity is summoned in the client world
        public static final ParameterizedEventDispatch<EntitySummonDispatchEvent.Client, EntitySummonParams> SUMMON_CLIENT = ParameterizedEventDispatch.createParameterized(EventSide.CLIENT);
        // fired when an entity is summoned in the server's world
        public static final ParameterizedEventDispatch<EntitySummonDispatchEvent.Server, EntitySummonParams> SUMMON_SERVER = ParameterizedEventDispatch.createParameterized(EventSide.SERVER);
    }
}