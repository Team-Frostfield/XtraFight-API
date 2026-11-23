package team.frostfield.xtrafight.events.mc.player;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;

public abstract class PlayerJoinDispatchEvent extends DispatchEvent implements ParameterizedEvent {

    private final Player player;
    private final Level level;

    public PlayerJoinDispatchEvent(Player player, Level level) {
        this.player = player;
        this.level = level;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Level getLevel() {
        return this.level;
    }

    @FunctionalInterface
    public interface PlayerJoinWorldParams extends EventParameters {
        void onPlayerJoin(Player player, Level level);
    }

    public static class Client extends PlayerJoinDispatchEvent {
        public Client(Player player, ClientLevel level) {
            super(player, level);
        }
    }

    public static class Server extends PlayerJoinDispatchEvent {
        public Server(Player player, ServerLevel level) {
            super(player, level);
        }
    }
}
