package team.frostfield.xtrafight.events.mc.player;

import net.minecraft.world.entity.player.Player;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;

public abstract class PlayerTickDispatchEvent extends DispatchEvent implements ParameterizedEvent {
    private final Player player;
    public PlayerTickDispatchEvent(Player player) {
        this.player = player;
    }

    @FunctionalInterface
    public interface PlayerTickParams extends EventParameters {
        void onPlayerTick(Player player);
    }

    public Player getPlayer() {
        return this.player;
    }

    public static class Pre extends PlayerTickDispatchEvent {
        public Pre(Player player) {
            super(player);
        }
    }

    public static class Post extends PlayerTickDispatchEvent {
        public Post(Player player) {
            super(player);
        }
    }
}
