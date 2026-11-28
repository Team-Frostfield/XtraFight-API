package team.frostfield.xtrafight.server.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import team.frostfield.xtrafight.builders.minecraft.CommandsBuilder;
import team.frostfield.xtrafight.utils.managers.MeshesManager;

class MeshesCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return CommandsBuilder.newRoot("meshes")
                .newLiteral("reload")
                .executes(MeshesCommand::reloadMeshes)
                .build();
    }

    private static int reloadMeshes(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal("Reloading meshes..."), false);
        try {
            MeshesManager.reloadMeshes();
            context.getSource().sendSuccess(() -> Component.literal("Meshes reloaded!"), false);
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to reload meshes!, Exception: " + e.getMessage()));
        }
        return Command.SINGLE_SUCCESS;
    }
}
