package team.frostfield.xtrafight.network.packets.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import team.frostfield.xtrafight.client.gui.InteractionType;
import team.frostfield.xtrafight.main.XtraFightAPI;
import team.frostfield.xtrafight.network.packets.ICustomPacket;

public record CPFreezeEntity(int entityId, boolean freeze) implements ICustomPacket<CPFreezeEntity> {
    // remembers if the mob already had NoAI before it got frozen
    private static final String FROZEN_TAG = XtraFightAPI.MOD_ID + ":frozen_no_ai";

    private CPFreezeEntity() {
        this(0, false);
    }

    private CPFreezeEntity(FriendlyByteBuf byteBuf) {
        this(byteBuf.readInt(), byteBuf.readBoolean());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeBoolean(this.freeze);
    }


    @Override
    public CPFreezeEntity decode(FriendlyByteBuf buf) {
        return new CPFreezeEntity(buf);
    }


    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (!(context.getSender() instanceof ServerPlayer)) return;
            ServerPlayer sender = context.getSender();

            Entity target = sender.level().getEntity(this.entityId);
            if (target == null) return;

            if (this.freeze) {
                freeze(sender, target);
            } else if (target instanceof Mob mob) {
                // no range check here, the client sends this after you walk away
                unfreeze(mob);
            }
        });
        context.setPacketHandled(true);
    }

    private static void freeze(ServerPlayer sender, Entity target) {
        if (!target.isAlive() || sender.distanceToSqr(target) > InteractionType.MAX_INTERACTION_DISTANCE_SQR) return;

        if (target instanceof Mob mob && !mob.isFallFlying()) {
            CompoundTag data = mob.getPersistentData();
            if (!data.contains(FROZEN_TAG)) {
                data.putBoolean(FROZEN_TAG, mob.isNoAi());
            }
            mob.getNavigation().stop();
            mob.setDeltaMovement(0, mob.getDeltaMovement().y, 0);
            mob.stopUsingItem();
            mob.setNoAi(true);
        } else if (target instanceof Player player) {
            player.stopUsingItem();
            player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
            player.setSprinting(false);
        }
    }

    // only undo our own freeze, leave NoAI from others alone
    public static void unfreeze(Mob mob) {
        CompoundTag data = mob.getPersistentData();
        if (data.contains(FROZEN_TAG)) {
            mob.setNoAi(data.getBoolean(FROZEN_TAG));
            data.remove(FROZEN_TAG);
        }
    }

    // if someone logs off mid-dialogue the mob gets saved frozen, so let it go when it loads back in
    @Mod.EventBusSubscriber(modid = XtraFightAPI.MOD_ID)
    public static final class FrozenMobCleanup {
        @SubscribeEvent
        public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
            if (event.loadedFromDisk() && !event.getLevel().isClientSide() && event.getEntity() instanceof Mob mob) {
                unfreeze(mob);
            }
        }
    }
}
