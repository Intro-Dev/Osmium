package com.intro.server.command;

import com.intro.client.util.EnumUtil;
import com.intro.common.config.OptionSerializer;
import com.intro.common.config.options.BooleanOption;
import com.intro.common.config.options.DoubleOption;
import com.intro.common.config.options.EnumOption;
import com.intro.common.config.options.Option;
import com.intro.common.network.NetworkingConstants;
import com.intro.server.OsmiumServer;
import com.intro.server.api.OptionApi;
import com.intro.server.network.ServerNetworkHandler;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.Level;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CommandManager {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("osmium").then(literal("option").then(literal("set").then(argument("identifier", StringArgumentType.string()).then(argument("double", DoubleArgumentType.doubleArg()).executes(CommandManager::doubleSetCommand))
                    .then(argument("boolean", StringArgumentType.string()).executes(CommandManager::booleanSetCommand))
                    .then(argument("enum_type", StringArgumentType.string()).then(argument("enum_value", StringArgumentType.string()).executes(CommandManager::enumSetCommand)))
            )).then(literal("refresh").executes(CommandManager::refreshCommand))
                    .then(literal("remove").then(argument("identifier", StringArgumentType.string()).executes(CommandManager::removeCommand)))));
        });
    }

    public static int doubleSetCommand(CommandContext<CommandSourceStack> context) {
        try {
            Option option = new DoubleOption(StringArgumentType.getString(context, "identifier"), DoubleArgumentType.getDouble(context, "double"));
            OptionApi.addSetOption(option);
            context.getSource().sendSuccess(new TextComponent("Set Option value"), true);
        } catch (Exception e) {
            context.getSource().sendSuccess(new TextComponent("Error: Invalid Option Data"), true);
        }
        return 1;
    }

    public static int booleanSetCommand(CommandContext<CommandSourceStack> context) {
        try {
            Option option = new BooleanOption(StringArgumentType.getString(context, "identifier"), Boolean.parseBoolean(StringArgumentType.getString(context, "boolean")));
            OptionApi.addSetOption(option);
            context.getSource().sendSuccess(new TextComponent("Set Option value"), true);
        } catch (Exception e) {
            context.getSource().sendSuccess(new TextComponent("Error: Invalid Option Data"), true);
        }
        return 1;
    }

    public static int enumSetCommand(CommandContext<CommandSourceStack> context) {
        try {
            Option option = new EnumOption(StringArgumentType.getString(context, "identifier"), EnumUtil.loadEnumState(CommandManager.class.getClassLoader(), StringArgumentType.getString(context, "enum_type"), StringArgumentType.getString(context, "enum_value")));
            OptionApi.addSetOption(option);
            context.getSource().sendSuccess(new TextComponent("Set Option value"), true);
        } catch (Exception e) {
            context.getSource().sendSuccess(new TextComponent("Error: Invalid Option Data"), true);
        }
        return 1;
    }

    public static int refreshCommand(CommandContext<CommandSourceStack> context) {
        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            try {
                OptionSerializer serializer = new OptionSerializer();
                FriendlyByteBuf byteBuf = PacketByteBufs.create();
                // write size so we only have to send one packet
                byteBuf.writeInt(OptionApi.getServerSetOptions().size());
                for (Option option : OptionApi.getServerSetOptions()) {
                    String serializedOption = serializer.serialize(option, null, null).toString();
                    byteBuf.writeUtf(serializedOption);
                }
                ServerNetworkHandler.sendPacket(player, NetworkingConstants.SET_SETTING_PACKET_ID, byteBuf);
            } catch (Exception e) {
                OsmiumServer.LOGGER.log(Level.WARN, "Error in refreshing clients options");
            }
        }
        return 1;
    }

    public static int removeCommand(CommandContext<CommandSourceStack> context) {
        OptionApi.removeSetOption(StringArgumentType.getString(context, "identifier"));
        context.getSource().sendSuccess(new TextComponent("Set Option value"), true);
        return 1;
    }

}
