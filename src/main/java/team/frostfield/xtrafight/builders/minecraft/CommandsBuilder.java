package team.frostfield.xtrafight.builders.minecraft;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraftforge.server.command.EnumArgument;
import org.jetbrains.annotations.ApiStatus.Experimental;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Experimental
public class CommandsBuilder {

    private CommandsBuilder() {}

    public static RootBuilder newRoot(String name) {
        return new RootBuilder(Commands.literal(name));
    }

    public static class RootBuilder extends NodeBuilder {
        public RootBuilder(LiteralArgumentBuilder<CommandSourceStack> rootNode) {
            super(rootNode, null);
        }

        @Override
        public RootBuilder executes(Command<CommandSourceStack> cmd) {
            super.executes(cmd);
            return this;
        }

        @Override
        public RootBuilder fork(Consumer<NodeBuilder> branch) {
            super.fork(branch);
            return this;
        }
    }

    public static class NodeBuilder {
        protected final ArgumentBuilder<CommandSourceStack, ?> builder;
        protected final NodeBuilder rootReference;
        protected final List<NodeBuilder> children = new ArrayList<>();

        public NodeBuilder(ArgumentBuilder<CommandSourceStack, ?> builder, NodeBuilder rootReference) {
            this.builder = builder;
            this.rootReference = (rootReference == null) ? this : rootReference;
        }

        protected <T extends NodeBuilder> T addChild(T child) {
            this.children.add(child);
            return child;
        }

        public NodeBuilder newLiteral(String name) {
            return addChild(new NodeBuilder(Commands.literal(name), this.rootReference));
        }

        public NodeBuilder newArgument(String name, ArgumentType<?> type) {
            return addChild(new NodeBuilder(Commands.argument(name, type), this.rootReference));
        }

        public <E extends Enum<E>> EnumCommandBuilder<E> newEnum(String name, Class<E> enumClass) {
            RequiredArgumentBuilder<CommandSourceStack, E> next = Commands.argument(name, EnumArgument.enumArgument(enumClass));
            return addChild(new EnumCommandBuilder<>(next, this.rootReference, enumClass));
        }

        public IntegerCommandBuilder newInt(String name, int min, int max) {
            RequiredArgumentBuilder<CommandSourceStack, Integer> next = Commands.argument(name, IntegerArgumentType.integer(min, max));
            return addChild(new IntegerCommandBuilder(next, this.rootReference));
        }

        public FloatCommandBuilder newFloat(String name, float min, float max) {
            RequiredArgumentBuilder<CommandSourceStack, Float> next = Commands.argument(name, FloatArgumentType.floatArg(min, max));
            return addChild(new FloatCommandBuilder(next, this.rootReference));
        }

        public NodeBuilder executes(Command<CommandSourceStack> cmd) {
            this.builder.executes(cmd);
            return this;
        }

        public NodeBuilder fork(Consumer<NodeBuilder> branch) {
            branch.accept(this);
            return this;
        }

        protected ArgumentBuilder<CommandSourceStack, ?> assemble() {
            for (NodeBuilder child : this.children) {
                this.builder.then(child.assemble());
            }
            return this.builder;
        }

        @SuppressWarnings("unchecked")
        public LiteralArgumentBuilder<CommandSourceStack> build() {
            return (LiteralArgumentBuilder<CommandSourceStack>) this.rootReference.assemble();
        }
    }

    public static class EnumCommandBuilder<E extends Enum<E>> extends NodeBuilder {
        protected final Class<E> enumClass;

        public EnumCommandBuilder(ArgumentBuilder<CommandSourceStack, ?> builder, NodeBuilder rootReference, Class<E> enumClass) {
            super(builder, rootReference);
            this.enumClass = enumClass;
        }

        public EnumCommandBuilder<E> withEnum(String name) {
            RequiredArgumentBuilder<CommandSourceStack, E> next = Commands.argument(name, EnumArgument.enumArgument(this.enumClass));
            return addChild(new EnumCommandBuilder<>(next, this.rootReference, this.enumClass));
        }

        public EnumCommandBuilder<E> newTargets(String name, Command<CommandSourceStack> action) {
            RequiredArgumentBuilder<CommandSourceStack, EntitySelector> next = Commands.argument(name, EntityArgument.entities()).executes(action);
            return addChild(new EnumCommandBuilder<>(next, this.rootReference, this.enumClass));
        }
    }

    public static class IntegerCommandBuilder extends NodeBuilder {
        public IntegerCommandBuilder(ArgumentBuilder<CommandSourceStack, ?> builder, NodeBuilder rootReference) {
            super(builder, rootReference);
        }

        public IntegerCommandBuilder chainInt(String name, int min, int max, Command<CommandSourceStack> action) {
            var next = Commands.argument(name, IntegerArgumentType.integer(min, max)).executes(action);
            return addChild(new IntegerCommandBuilder(next, this.rootReference));
        }
    }

    public static class FloatCommandBuilder extends NodeBuilder {
        public FloatCommandBuilder(ArgumentBuilder<CommandSourceStack, ?> builder, NodeBuilder rootReference) {
            super(builder, rootReference);
        }

        public FloatCommandBuilder chainFloat(String name, float min, float max, Command<CommandSourceStack> action) {
            var next = Commands.argument(name, FloatArgumentType.floatArg(min, max)).executes(action);
            return addChild(new FloatCommandBuilder(next, this.rootReference));
        }
    }
}