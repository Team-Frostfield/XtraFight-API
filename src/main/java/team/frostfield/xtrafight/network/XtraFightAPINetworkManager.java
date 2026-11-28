package team.frostfield.xtrafight.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.PacketDistributor;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;
import team.frostfield.xtrafight.events.bus.ModBus;
import team.frostfield.xtrafight.network.packets.XtraFightAPIPacketsManager;
import yesman.epicfight.network.EntityPairingPacketType;
import yesman.epicfight.network.server.SPEntityPairingPacket;

public class XtraFightAPINetworkManager implements ILoadableClass {

    @Invoke
    private XtraFightAPINetworkManager(ModBus modBus) {
        modBus.addListener(this::registerNetworking);
    }

    private void registerNetworking(final FMLCommonSetupEvent event) {
        // Somehow can't accept lambda???
        event.enqueueWork(() -> XtraFightAPIPacketsManager.registerPackets());
    }

    public static <MSG> void sendToServer(MSG message) {
        XtraFightAPIPacketsManager.CHANNEL.send(PacketDistributor.SERVER.noArg(), message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        XtraFightAPIPacketsManager.CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToAllTrackingThisEntity(Entity entity, MSG message) {
        XtraFightAPIPacketsManager.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    public static <MSG> void sendToAllTrackingThisEntityAndSelf(Entity entity, MSG message) {
        XtraFightAPIPacketsManager.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    public static <MSG> void sendToPlayer(ServerPlayer serverPlayer, MSG message) {
        XtraFightAPIPacketsManager.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
    }

    public static <MSG> void sendToAllPlayersInChunk(ServerLevel serverLevel, BlockPos blockPos, MSG message) {
        XtraFightAPIPacketsManager.CHANNEL.send(
                PacketDistributor.TRACKING_CHUNK.with(() -> serverLevel.getChunkAt(blockPos)),
                message
        );
    }

    public static void sendPairingPacket(Entity entity, EntityPairingPacketType pairingType, Object... arguments) {
        SPEntityPairingPacket entityPairPacket = new SPEntityPairingPacket(entity.getId(), pairingType);
        for (Object argument : arguments) {
            if (argument instanceof Integer integer) {
                entityPairPacket.getBuffer().writeInt(integer);
            } else if (argument instanceof Float flt) {
                entityPairPacket.getBuffer().writeFloat(flt);
            } else if (argument instanceof Long lng) {
                entityPairPacket.getBuffer().writeLong(lng);
            } else if (argument instanceof Short shrt) {
                entityPairPacket.getBuffer().writeShort(shrt);
            } else if (argument instanceof Byte byt) {
                entityPairPacket.getBuffer().writeByte(byt);
            } else if (argument instanceof Boolean bool) {
                entityPairPacket.getBuffer().writeBoolean(bool);
            } else if (argument instanceof Enum<?> enumCls) {
                entityPairPacket.getBuffer().writeEnum(enumCls);
            } else {
                throw new IllegalStateException("Unexpected argument: " + argument);
            }
        }
        sendToAllTrackingThisEntityAndSelf(entity, entityPairPacket);
    }
}