package team.frostfield.xtrafight.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum InteractionType {
    TALK((player, target, ctx) ->
            player.sendSystemMessage(Component.literal("Interacted with " + target.getName().getString()))
    ),

    ATTACK((player, target, ctx) ->
            target.hurt(player.damageSources().playerAttack(player), 2.0F)
    ),

    HEAL((player, target, ctx, args) -> {
        float amount = args.isEmpty() ? 4.0F : ((Number) args.get(0)).floatValue();
        if (target instanceof LivingEntity living) {
            living.heal(amount);
        }
    }),

    OPEN_TRADE((player, target, ctx) -> {
        if (target instanceof Villager villager && villager.getTradingPlayer() == null && !villager.isBaby()) {
            villager.setTradingPlayer(player);
            villager.openTradingScreen(player, villager.getDisplayName(), villager.getVillagerData().getLevel());
        }
    }),

    GIVE_ITEM((player, target, ctx, args) -> {
        if (!args.isEmpty() && args.get(0) instanceof ItemStack stack) {
            ItemHandlerHelper.giveItemToPlayer(player, stack.copy());
        }
    }),

    NONE((player, target, ctx) -> {});

    @Nullable
    private Object[] arguments;
    // 6 blocks
    public static final double MAX_INTERACTION_DISTANCE_SQR = 6.0 * 6.0;

    @FunctionalInterface
    public interface ParameterizedHandler {
        void execute(ServerPlayer player, Entity target, NetworkEvent.Context context, List<Object> args);
    }

    @FunctionalInterface
    public interface InteractionHandler {
        void handle(ServerPlayer player, Entity target, NetworkEvent.Context context);
    }

    private final ParameterizedHandler parameterizedHandler;
    private final InteractionHandler interactionHandler;

    InteractionType(ParameterizedHandler handler) {
        this.parameterizedHandler = handler;
        this.interactionHandler = null;
    }

    InteractionType(InteractionHandler handler) {
        this.interactionHandler = handler;
        this.parameterizedHandler = null;
    }

    public void withArgs(Object... arguments) {
        this.arguments = arguments;
    }

    @Internal @Nullable
    public Object[] getArguments() {
        return this.arguments;
    }

    public void execute(ServerPlayer player, Entity target, NetworkEvent.Context context) {
        this.execute(player, target, context, List.of());
    }

    public void execute(ServerPlayer player, Entity target, NetworkEvent.Context context, List<Object> args) {
        if (!target.isAlive() || player.distanceToSqr(target) > MAX_INTERACTION_DISTANCE_SQR) {
            return;
        }

        if (this.parameterizedHandler != null && this.arguments != null) {
            this.parameterizedHandler.execute(player, target, context, args);
        } else if (this.interactionHandler != null) {
            this.interactionHandler.handle(player, target, context);
        }
    }
}