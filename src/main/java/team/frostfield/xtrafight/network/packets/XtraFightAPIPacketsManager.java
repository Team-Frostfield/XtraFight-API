package team.frostfield.xtrafight.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import team.frostfield.xtrafight.main.XtraFightAPIMod;
import team.frostfield.xtrafight.utils.code.AccessType;
import team.frostfield.xtrafight.utils.code.CodeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XtraFightAPIPacketsManager {
    private static final List<ICustomPacket<?>> CLIENT_PACKETS = new ArrayList<>();
    private static final List<ICustomPacket<?>> SERVER_PACKETS = new ArrayList<>();
    private static final List<ICustomPacket<?>> BIDIRECTIONAL_PACKETS = new ArrayList<>();


    public static void register(Class<? extends ICustomPacket<?>> packet) {
        String packetName = packet.getSimpleName();
        String packetType = "";
        ICustomPacket<?> customPacket = CodeUtils.newInstance(packet, AccessType.DECLARED);

        // these were flipped ClientBound is server > client
        if (nameStartsOrContains(packetName, "SP", "SB", "STC", "ClientBound", "ServerPacket")) {
            SERVER_PACKETS.add(customPacket);
            packetType = "server";
        }
        if (nameStartsOrContains(packetName, "CP", "CB", "CTS", "ServerBound", "ClientPacket")) {
            CLIENT_PACKETS.add(customPacket);
            packetType = "client";
        }
        if (nameStartsOrContains(packetName, "BDP", "BI", "BP", "BiDirectionalBound", "BiDirectionalPacket")) {
            BIDIRECTIONAL_PACKETS.add(customPacket);
            packetType = "bidirectional";
        }
        XtraFightAPIMod.debugIfDevSide("Registered {} packet {}", packetType, packetName);
    }

    static {
        Class<? extends ICustomPacket<?>>[] packetClasses = Arrays.stream(XtraFightAPIPackets.values()).map(XtraFightAPIPackets::getPacketCls).<Class<? extends ICustomPacket<?>>>toArray(Class[]::new);
        for (Class<? extends ICustomPacket<?>> packetCls : packetClasses) {
            register(packetCls);
        }
    }

    // the instances in these lists are just templates for the slop
    // so handle the packet we actually got instead
    @SuppressWarnings("unchecked")
    public static <T extends ICustomPacket<T>> void register(PayloadRegistrar registrar) {
        CLIENT_PACKETS.forEach(iCustomPacket ->
                registrar.playToServer(
                        (Type<T>) iCustomPacket.type(),
                        (StreamCodec<? super RegistryFriendlyByteBuf, T>) iCustomPacket.codec(),
                        XtraFightAPIPacketsManager::handleDecoded
                )
        );
        SERVER_PACKETS.forEach(iCustomPacket ->
                registrar.playToClient(
                        (Type<T>) iCustomPacket.type(),
                        (StreamCodec<? super RegistryFriendlyByteBuf, T>) iCustomPacket.codec(),
                        XtraFightAPIPacketsManager::handleDecoded
                )
        );
        BIDIRECTIONAL_PACKETS.forEach(iCustomPacket ->
                registrar.playBidirectional(
                        (Type<T>) iCustomPacket.type(),
                        (StreamCodec<? super RegistryFriendlyByteBuf, T>) iCustomPacket.codec(),
                        XtraFightAPIPacketsManager::handleDecoded
                )
        );
    }

    private static <T extends ICustomPacket<?>> void handleDecoded(T packet, IPayloadContext context) {
        packet.handle(context);
    }

    private static boolean nameStartsOrContains(String name, String... matchings) {
        for (String word : matchings) {
            if (name.startsWith(word) || name.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
