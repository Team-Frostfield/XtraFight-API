package team.frostfield.xtrafight.server.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import team.frostfield.xtrafight.builders.minecraft.CommandsBuilder;
import team.frostfield.xtrafight.utils.managers.ArmaturesManager;

class ArmaturesCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return CommandsBuilder.newRoot("armatures")
                .newLiteral("reload")
                .executes(ArmaturesCommand::reloadArmatures)
                .build();
    }

    private static int reloadArmatures(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal("Reloading armatures..."), false);
        try {
            ArmaturesManager.reloadArmatures();
            context.getSource().sendSuccess(() -> Component.literal("Armatures reloaded!"), false);
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to reload armatures!, Exception: " + e.getMessage()));
        }
        return Command.SINGLE_SUCCESS;
    }
}
