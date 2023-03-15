package dev.lobstershack.server.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.lobstershack.client.util.EnumUtil;
import dev.lobstershack.common.config.options.Option;
import dev.lobstershack.common.config.options.OptionSerializer;
import dev.lobstershack.common.config.options.legacy.BooleanOption;
import dev.lobstershack.common.config.options.legacy.DoubleOption;
import dev.lobstershack.common.config.options.legacy.EnumOption;
import dev.lobstershack.common.config.options.legacy.LegacyOption;
import dev.lobstershack.common.network.NetworkingConstants;
import dev.lobstershack.server.OsmiumServer;
import dev.lobstershack.server.api.OptionApi;
import dev.lobstershack.server.api.PlayerApi;
import dev.lobstershack.server.network.ServerNetworkHandler;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.Level;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class OsmiumCommandManager {

    private static final OptionSerializer serializer = new OptionSerializer();

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(literal("osmium").requires(commandSourceStack -> commandSourceStack.hasPermission(3)).then(literal("option").then(literal("set").then(argument("identifier", StringArgumentType.string()).then(argument("double", DoubleArgumentType.doubleArg()).executes(OsmiumCommandManager::doubleSetCommand))
                        .then(argument("boolean", StringArgumentType.string()).executes(OsmiumCommandManager::booleanSetCommand))
                        .then(argument("enum_type", StringArgumentType.string()).then(argument("enum_value", StringArgumentType.string()).executes(OsmiumCommandManager::enumSetCommand)))
                )).then(literal("refresh").executes(OsmiumCommandManager::refreshCommand))
                .then(literal("remove").then(argument("identifier", StringArgumentType.string()).executes(OsmiumCommandManager::removeCommand)))
                .then(literal("reset").executes(OsmiumCommandManager::clearCommand))))));
    }

    public static int doubleSetCommand(CommandContext<CommandSourceStack> context) {
        try {
            LegacyOption option = new DoubleOption(StringArgumentType.getString(context, "identifier"), DoubleArgumentType.getDouble(context, "double"));
            OptionApi.addSetOption(option);
            context.getSource().sendSuccess(Component.literal("Set LegacyOption value"), true);
        } catch (Exception e) {
            context.getSource().sendSuccess(Component.literal("Error: Invalid LegacyOption Data"), true);
        }
        return 1;
    }

    public static int booleanSetCommand(CommandContext<CommandSourceStack> context) {
        try {
            LegacyOption option = new BooleanOption(StringArgumentType.getString(context, "identifier"), Boolean.parseBoolean(StringArgumentType.getString(context, "boolean")));
            OptionApi.addSetOption(option);
            context.getSource().sendSuccess(Component.literal("Set LegacyOption value"), true);
        } catch (Exception e) {
            context.getSource().sendSuccess(Component.literal("Error: Invalid LegacyOption Data"), true);
        }
        return 1;
    }

    public static int enumSetCommand(CommandContext<CommandSourceStack> context) {
        try {
            LegacyOption option = new EnumOption(StringArgumentType.getString(context, "identifier"), EnumUtil.loadEnumState(OsmiumCommandManager.class.getClassLoader(), StringArgumentType.getString(context, "enum_type"), StringArgumentType.getString(context, "enum_value")));
            OptionApi.addSetOption(option);
            context.getSource().sendSuccess(Component.literal("Set option value"), true);
        } catch (Exception e) {
            context.getSource().sendSuccess(Component.literal("Error: Invalid LegacyOption Data"), true);
        }
        return 1;
    }

    public static int refreshCommand(CommandContext<CommandSourceStack> context) {
        for (ServerPlayer player : PlayerApi.playersRunningOsmium.values()) {
            try {
                FriendlyByteBuf byteBuf = PacketByteBufs.create();
                // write size so we only have to send one packet
                byteBuf.writeInt(OptionApi.getServerSetOptions().size());
                for (Option<?> option : OptionApi.getServerSetOptions()) {
                    String serializedOption = serializer.serialize(option, null, null).toString();
                    byteBuf.writeUtf(serializedOption);
                }
                ServerNetworkHandler.sendPacket(player, NetworkingConstants.SET_SETTING_PACKET_ID, byteBuf);
            } catch (Exception e) {
                OsmiumServer.LOGGER.log(Level.WARN, "Error in refreshing clients options");
            }
        }
        context.getSource().sendSuccess(Component.literal("Refreshed option values"), true);
        return 1;
    }

    public static int removeCommand(CommandContext<CommandSourceStack> context) {
        OptionApi.removeSetOption(StringArgumentType.getString(context, "identifier"));
        context.getSource().sendSuccess(Component.literal("Set LegacyOption value"), true);
        return 1;
    }

    public static int clearCommand(CommandContext<CommandSourceStack> context) {
        OptionApi.clearSetOptions();
        context.getSource().sendSuccess(Component.literal("Reset option values"), true);
        return 1;
    }

}
