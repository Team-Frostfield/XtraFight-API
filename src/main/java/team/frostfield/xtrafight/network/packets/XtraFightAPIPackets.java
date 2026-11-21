package team.frostfield.xtrafight.network.packets;

import team.frostfield.xtrafight.network.packets.client.CPEntityInteraction;
import team.frostfield.xtrafight.network.packets.client.CPFreezeEntity;
import team.frostfield.xtrafight.network.packets.server.SPGameRuleSync;

public enum XtraFightAPIPackets {
    GAMERULE_SYNC(SPGameRuleSync.class),
    ENTITY_FREEZE(CPFreezeEntity.class),
    ENTITY_INTERACT(CPEntityInteraction.class);

    private final Class<? extends ICustomPacket<?>> packetCls;

    XtraFightAPIPackets(Class<? extends ICustomPacket<?>> packetCls) {
        this.packetCls = packetCls;
    }

    public Class<? extends ICustomPacket<?>> getPacketCls() {
        return this.packetCls;
    }
}