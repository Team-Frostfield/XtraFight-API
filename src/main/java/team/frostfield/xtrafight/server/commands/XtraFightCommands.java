package team.frostfield.xtrafight.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;
import team.frostfield.xtrafight.events.bus.ForgeBus;

public class XtraFightCommands implements ILoadableClass {

    public static final int OP = 2;

    @Invoke
    private XtraFightCommands(ForgeBus forgeBus) {
        forgeBus.addListener(XtraFightCommands::registerCommands);
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("xtrafight")
                        .requires(stack -> stack.hasPermission(OP))
                        .then(ArmaturesCommand.register())
                        .then(MeshesCommand.register())
        );
    }

}
