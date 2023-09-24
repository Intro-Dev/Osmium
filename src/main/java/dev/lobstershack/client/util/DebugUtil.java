package dev.lobstershack.client.util;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.mixin.client.ChatComponentInvoker;
import dev.lobstershack.client.render.widget.drawable.DrawableRenderer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DebugUtil {

    private static final Logger LOGGER = LogManager.getLogger("OsmiumDebug");
    private static boolean DEBUG = false;
    private static boolean shouldDisplayLogsInChat = false;

    public static void enableDebugMode() {
        DEBUG = true;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static void initDebugCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("OsmiumDebug")
                .then(ClientCommandManager.literal("ReloadDrawables").executes((context) -> {
                    DrawableRenderer.reloadDrawables();
                    context.getSource().sendFeedback(Component.literal("[Osmium Debug] Drawables reloaded").withStyle(ChatFormatting.LIGHT_PURPLE));
                    return 0;
                }))
                .then(ClientCommandManager.literal("Toggle").executes((context -> {
                    DEBUG = !DEBUG;
                    context.getSource().sendFeedback(Component.literal("[Osmium Debug] Debug mode is now " + (DEBUG ? "enabled" : "disabled")).withStyle(ChatFormatting.LIGHT_PURPLE));
                    return 0;
                })))
                .then(ClientCommandManager.literal("DisplayInChat").executes(context -> {
                    shouldDisplayLogsInChat = !shouldDisplayLogsInChat;
                    context.getSource().sendFeedback(Component.literal("[Osmium Debug] Chat log display " + (shouldDisplayLogsInChat ? "enabled" : "disabled")).withStyle(ChatFormatting.LIGHT_PURPLE));
                    return 0;
                }))
                .then(ClientCommandManager.literal("ReloadCapes").executes(context -> {
                    OsmiumClient.cosmeticManager.reloadAllCapes();
                    context.getSource().sendFeedback(Component.literal("[Osmium Debug] Reloaded all Capes").withStyle(ChatFormatting.LIGHT_PURPLE));
                    return 0;
                }))
                .then(ClientCommandManager.literal("CapeImpersonate").then(ClientCommandManager.argument("name", StringArgumentType.greedyString()).executes(context -> {
                    ExecutionUtil.submitTask(() -> {
                        String name = context.getArgument("name", String.class);
                        OsmiumClient.cosmeticManager.setImpersonationName(name);
                        OsmiumClient.cosmeticManager.downloadPlayerCape(Minecraft.getInstance().player);
                        context.getSource().sendFeedback(Component.literal("[Osmium Debug] Now impersonating cape of player " + name).withStyle(ChatFormatting.LIGHT_PURPLE));
                    });
                    return 0;
                })))
                .then(ClientCommandManager.literal("ToggleImpersonation").executes(context -> {
                  OsmiumClient.cosmeticManager.setShouldImpersonate(!OsmiumClient.cosmeticManager.isImpersonating());
                  context.getSource().sendFeedback(Component.literal("[Osmium Debug] Cape impersonation is now " + (DEBUG ? "enabled" : "disabled")).withStyle(ChatFormatting.LIGHT_PURPLE));
                  return 0;
                }))
        ));
    }

    public static void logIfDebug(Object o, Level level) {
        if(DEBUG) {
            try {
                StackTraceElement callingStack = Thread.currentThread().getStackTrace()[2];
                int lastDotIndex = callingStack.getClassName().lastIndexOf(".");
                String className = callingStack.getClassName().substring(lastDotIndex + 1);
                LOGGER.log(level, className + "." + callingStack.getMethodName() +  "(): " + o);
                if(Minecraft.getInstance().level != null && shouldDisplayLogsInChat) {
                    // solid 200 characters
                    // i love java
                    ((ChatComponentInvoker) Minecraft.getInstance().gui.getChat()).invokeAddMessage(Component.literal("[OsmiumDebug] " + level.name() + " ").withStyle(ChatFormatting.LIGHT_PURPLE).append(Component.literal(className + "." + callingStack.getMethodName() +  "()").withStyle(ChatFormatting.GREEN).append(Component.literal(": " + o).withStyle(ChatFormatting.WHITE))), null, Minecraft.getInstance().gui.getGuiTicks(), Minecraft.getInstance().isSingleplayer() ? GuiMessageTag.systemSinglePlayer() : GuiMessageTag.system(), false);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARN, "Congrats, the Osmium debugger just crashed. How did this happen?");
                LOGGER.log(Level.ERROR, e);
            }
        }
    }

}
