package team.frostfield.xtrafight.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import team.frostfield.xtrafight.main.XtraFightAPI;
import team.frostfield.xtrafight.utils.code.AccessType;
import team.frostfield.xtrafight.utils.code.CodeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class XtraFightAPIPacketsManager {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            XtraFightAPI.getInstance().identifier("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static final List<ICustomPacket<?>> CLIENT_PACKETS = new ArrayList<>();
    private static final List<ICustomPacket<?>> SERVER_PACKETS = new ArrayList<>();
    private static final List<ICustomPacket<?>> BIDIRECTIONAL_PACKETS = new ArrayList<>();

    private static int nextId = 0;

    static {
        Class<? extends ICustomPacket<?>>[] packetClasses = Arrays.stream(XtraFightAPIPackets.values())
                .map(XtraFightAPIPackets::getPacketCls)
                .<Class<? extends ICustomPacket<?>>>toArray(Class[]::new);
        for (Class<? extends ICustomPacket<?>> packetCls : packetClasses) {
            register(packetCls);
        }
    }

    public static void register(Class<? extends ICustomPacket<?>> packet) {
        String packetName = packet.getSimpleName();
        String packetType = "";
        ICustomPacket<?> customPacket = CodeUtils.newEmptyInstance(packet, AccessType.DECLARED);

        if (SERVER_PACKETS.contains(customPacket) || CLIENT_PACKETS.contains(customPacket) || BIDIRECTIONAL_PACKETS.contains(customPacket)) {
            throw new IllegalArgumentException("Packet: " + packetName + " is already registered!");
        }

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

        XtraFightAPI.LOGGER.debugIfDevSide("Registered {} packet {}", packetType, packetName);
    }

    public static void registerPackets() {
        registerPackets(CHANNEL);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ICustomPacket<T>> void registerPackets(SimpleChannel channel) {
        for (ICustomPacket<?> packet : CLIENT_PACKETS) {
            registerSingle(channel, (ICustomPacket<T>) packet, NetworkDirection.PLAY_TO_SERVER);
        }
        for (ICustomPacket<?> packet : SERVER_PACKETS) {
            registerSingle(channel, (ICustomPacket<T>) packet, NetworkDirection.PLAY_TO_CLIENT);
        }
        for (ICustomPacket<?> packet : BIDIRECTIONAL_PACKETS) {
            registerSingle(channel, (ICustomPacket<T>) packet, null);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends ICustomPacket<T>> void registerSingle(
            SimpleChannel channel,
            ICustomPacket<T> prototype,
            NetworkDirection direction
    ) {
        Class<T> packetClass = (Class<T>) prototype.getClass();
        int id = nextId++;

        SimpleChannel.MessageBuilder<T> builder = (direction != null)
                ? channel.messageBuilder(packetClass, id, direction)
                : channel.messageBuilder(packetClass, id);

        builder
                .encoder(ICustomPacket::encode)
                .decoder(buf -> decodePacket(packetClass, buf))
                .consumerMainThread(XtraFightAPIPacketsManager::handleDecoded)
                .add();
    }

    private static <T extends ICustomPacket<T>> T decodePacket(Class<T> packetClass, FriendlyByteBuf buf) {
        T instance = CodeUtils.newEmptyInstance(packetClass, AccessType.DECLARED);
        return instance.decode(buf);
    }

    private static <T extends ICustomPacket<T>> void handleDecoded(T packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        packet.handle(context);
        context.setPacketHandled(true);
    }

    private static boolean nameStartsOrContains(String name, String... matchings) {
        for (String word : matchings) {
            if (name.startsWith(word) || name.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public static SimpleChannel getChannel() {
        return CHANNEL;
    }

    public static Optional<SimpleChannel> getChannelOptional() {
        return Optional.of(CHANNEL);
    }
}