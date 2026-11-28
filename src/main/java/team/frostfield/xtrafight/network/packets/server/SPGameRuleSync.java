package team.frostfield.xtrafight.network.packets.server;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import team.frostfield.xtrafight.cls.Comment;
import team.frostfield.xtrafight.main.XtraFightAPI;
import team.frostfield.xtrafight.network.packets.ICustomPacket;

public record SPGameRuleSync(String ruleName, int value) implements ICustomPacket<SPGameRuleSync> {

    @Comment("Invoked by SimpleChannel decoder")
    public SPGameRuleSync(FriendlyByteBuf byteBuf) {
        this(byteBuf.readUtf(), byteBuf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.ruleName);
        buf.writeInt(this.value);
    }

    public SPGameRuleSync decode(FriendlyByteBuf buf) {
        return new SPGameRuleSync(buf);
    }

    public void handle(NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleGameRulesSync(this)));
        ctx.setPacketHandled(true);
    }

    private static void handleGameRulesSync(SPGameRuleSync packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            GameRules gameRules = mc.level.getGameRules();
            GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
                @Override
                public <T extends GameRules.Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                    if (key.getId().equals(packet.ruleName())) {
                        T rule = gameRules.getRule(key);
                        if (rule instanceof GameRules.BooleanValue boolRule) {
                            boolRule.set(packet.value() != 0, null);
                        } else if (rule instanceof GameRules.IntegerValue intRule) {
                            intRule.set(packet.value(), null);
                        } else {
                            throw new IllegalStateException("Unexpected game rule type: " + rule);
                        }
                        XtraFightAPI.LOGGER.debugIfDevSide("Synced rule {}", key.getId());
                    }
                }
            });
        }
    }
}