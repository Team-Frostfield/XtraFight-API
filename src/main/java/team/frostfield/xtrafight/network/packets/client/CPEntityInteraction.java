package team.frostfield.xtrafight.network.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import team.frostfield.xtrafight.client.gui.InteractionType;
import team.frostfield.xtrafight.cls.Comment;
import team.frostfield.xtrafight.main.XtraFightAPI;
import team.frostfield.xtrafight.main.XtraFightAPIMod;
import team.frostfield.xtrafight.network.interfaces.PacketBufHandler;
import team.frostfield.xtrafight.network.packets.ICustomPacket;

import java.util.List;

public record CPEntityInteraction(int targetEntityId, InteractionType interactionType) implements ICustomPacket<CPEntityInteraction> {

    private CPEntityInteraction() {
        this(0, InteractionType.NONE);
    }

    @Override
    public CustomPacketPayload.@NotNull Type<CPEntityInteraction> type() {
        return new CustomPacketPayload.Type<>(XtraFightAPI.getInstance().identifier("entity_interaction"));
    }

    @Comment("Invoked by PacketBufHandler#codecOf")
    private CPEntityInteraction(FriendlyByteBuf byteBuf) {
        this(byteBuf.readInt(), byteBuf.readEnum(InteractionType.class));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.targetEntityId);
        buf.writeEnum(this.interactionType);
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, CPEntityInteraction> codec() {
        return PacketBufHandler.codecOf(CPEntityInteraction.class, CPEntityInteraction::encode, CPEntityInteraction::decode);
    }

    @Override
    public CPEntityInteraction decode(FriendlyByteBuf buf) {
        return new CPEntityInteraction(buf.readInt(), buf.readEnum(InteractionType.class));
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                Entity target = player.level().getEntity(this.targetEntityId);
                if (target != null && target.isAlive() && player.distanceToSqr(target) <= InteractionType.MAX_INTERACTION_DISTANCE_SQR) {
                    // We don't call this.interactionType.getArguments(); here, so it doesn't throw a null pointer exception
                    if (this.interactionType.getArguments() != null) {
                        Object[] args = this.interactionType.getArguments();
                        this.interactionType.execute(player, target, context, List.of(args));
                    } else {
                        this.interactionType.execute(player, target, context);
                    }
                    XtraFightAPI.LOGGER.debugIfDevSide("Executed interaction: {}", this.interactionType.toString().toLowerCase());
                }
            }
        });
    }
}