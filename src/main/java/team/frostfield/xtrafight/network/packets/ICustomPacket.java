package team.frostfield.xtrafight.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface ICustomPacket<T extends ICustomPacket<T>> {

    void encode(FriendlyByteBuf buf);

    T decode(FriendlyByteBuf buf);

    void handle(NetworkEvent.Context context);
}