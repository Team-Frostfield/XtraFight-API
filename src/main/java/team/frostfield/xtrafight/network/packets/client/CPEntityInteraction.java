package team.frostfield.xtrafight.network.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import team.frostfield.xtrafight.client.gui.InteractionType;
import team.frostfield.xtrafight.main.XtraFightAPI;
import team.frostfield.xtrafight.network.packets.ICustomPacket;

import java.util.List;

public record CPEntityInteraction(int targetEntityId, InteractionType interactionType) implements ICustomPacket<CPEntityInteraction> {

    private CPEntityInteraction(FriendlyByteBuf byteBuf) {
        this(byteBuf.readInt(), byteBuf.readEnum(InteractionType.class));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.targetEntityId);
        buf.writeEnum(this.interactionType);
    }

    @Override
    public CPEntityInteraction decode(FriendlyByteBuf buf) {
        return new CPEntityInteraction(buf.readInt(), buf.readEnum(InteractionType.class));
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Entity target = context.getSender().level().getEntity(this.targetEntityId);
            if (target != null && target.isAlive() && context.getSender().distanceToSqr(target) <= InteractionType.MAX_INTERACTION_DISTANCE_SQR) {
                // We don't call this.interactionType.getArguments(); here, so it doesn't throw a null pointer exception
                if (this.interactionType.getArguments() != null) {
                    Object[] args = this.interactionType.getArguments();
                    this.interactionType.execute(context.getSender(), target, context, List.of(args));
                } else {
                    this.interactionType.execute(context.getSender(), target, context);
                }
                XtraFightAPI.LOGGER.debugIfDevSide("Executed interaction: {}", this.interactionType.toString().toLowerCase());
            }
        });
        context.setPacketHandled(true);
    }
}