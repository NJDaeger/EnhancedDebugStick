package com.njdaeger.enhanceddebugstick;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.defaults.BCIBuilder;
import com.njdaeger.bci.defaults.CommandContext;
import com.njdaeger.bci.defaults.TabContext;
import com.njdaeger.btu.Text;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.DebugStickAPI;
import com.njdaeger.enhanceddebugstick.api.ShiftMode;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.session.Preference;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.njdaeger.enhanceddebugstick.Permissions.*;
import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.DEBUG_STICK;
import static org.bukkit.ChatColor.*;

final class DebugStickCommand {

    DebugStickCommand(EnhancedDebugStick plugin) {

        plugin.registerCommand(BCIBuilder.create("debugstick")
                .executor(this::debugStick)
                .completer(this::completions)
                .permissions("enhanceddebugstick.get",
                        "enhanceddebugstick.about",
                        "enhanceddebugstick.classic",
                        "enhanceddebugstick.copy",
                        "enhanceddebugstick.freeze",
                        "enhanceddebugstick.reload",
                        "enhanceddebugstick.help",
                        "enhanceddebugstick.preferences")
                .locatableSenders()
                .usage("/debugstick")
                .description("Gives you a usable debug stick.")
                .aliases("dbstick", "dbs")
                .senders(SenderType.PLAYER)
                .build());

    }

    private void debugStick(CommandContext context) throws BCIException {
        Player player = context.asPlayer();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(player.getUniqueId());

        if (context.isLength(0)) {
            session.sendMessage(GRAY + "You now have the Debug Stick");
            player.getInventory().addItem(DEBUG_STICK);
            return;
        }
        if (context.subCommandAt(0, "about", true, this::aboutCommand)) return;
        if (context.subCommandAt(0, "help", true, this::helpCommand)) return;
        if (context.subCommandAt(0, "reload", true, this::reloadCommand)) return;
        if (context.subCommandAt(0, "preference", true, this::preferenceCommand)) return;
        if (context.subCommand((ctx) -> ctx.isLength(1) && session.getPref(Preference.SHIFT_MODE) == ShiftMode.COMMAND, this::changeModeCommand)) return;
        else session.sendMessage(GRAY + "Arguments provided do not match any subcommand. Do '/dbs help' for help.");

        /*

        /dbs                                - Gives the debug stick to the user

        /dbs [mode]                         - Changes the current mode of the user

        /dbs about                          - Gives information about the plugin. (permissions etc)

        /dbs preferences <pref> <value>     - Allows the user to set their own preferences

        /dbs reload                         - Reloads the configuration

        /dbs help                           - Gives a list of commands and actions

         */
    }

    private void preferenceCommand(CommandContext context) {

    }

    private void reloadCommand(CommandContext context) throws BCIException {
        if (!context.hasPermission("enhanceddebugstick.reload")) context.noPermission();
        if (!new File(EnhancedDebugStick.getInstance().getDataFolder() + File.separator + "config.yml").exists()) {
            context.send(DARK_GRAY + "[" + BLUE + "EDS" + DARK_GRAY + "] " + GRAY + "config.yml did not exist. Creating it...");
            EnhancedDebugStick.getInstance().saveResource("config.yml", false);
            EnhancedDebugStick.getInstance().configuration = new ConfigurationFile(EnhancedDebugStick.getInstance());
        }
        else EnhancedDebugStick.getInstance().getDebugConfig().reload();
        context.send(DARK_GRAY + "[" + BLUE + "EDS" + DARK_GRAY + "] " + GRAY + "Configuration Reloaded.");
    }

    private void helpCommand(CommandContext context) throws BCIException {
        if (!context.hasPermission("enhanceddebugstick.help")) context.noPermission();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        session.sendMessage(GRAY + " <<< EnhancedDebugStick Help (Hover for Details) >>>");
        Text.of("  /dbs").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Gives you an Enhanced Debug Stick\n").append("Aliases:").append("debugstick, dbstick").setColor(GRAY).setItalics(true));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  /dbs <mode>").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Lets you change modes with a command\n").append("  <mode>").setItalics(true).setColor(GRAY).append(" - The mode to change to"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  /dbs about").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Gives information about the plugin"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  /dbs reload").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Reload the plugin"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  /dbs preference <preference> [value]").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Custom settings per user\n")
                    .append("  <preference>").setItalics(true).setColor(GRAY).append(" - The preference to change")
                    .append("  [value]").setItalics(true).setColor(GRAY).append(" - The new value"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
    }

    private void aboutCommand(CommandContext context) throws BCIException {
        if (!context.hasPermission("enhanceddebugstick.about")) context.noPermission();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        session.sendMessage(GRAY + " <<< About the EnhancedDebugStick >>>");
        context.send(GRAY + "  API Version: " + ITALIC + DebugStickAPI.getApiVersion());
        context.send(GRAY + "  Plugin Version: " + ITALIC + EnhancedDebugStick.getInstance().getDescription().getVersion());
        context.send(GRAY + "  Permissions: (Hover for Details)");
        Text.of("  - " + GET_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Get the Debug Stick"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  - " + ABOUT_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Get Version and Permissions of the EDS plugin"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  - " + CLASSIC_MODE).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Classic Debug Stick Permissions"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  - " + COPY_MODE).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Copy Debug Stick Permissions"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  - " + FREEZE_MODE).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Freeze Debug Stick Permissions"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  - " + RELOAD_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Reloads the EDS plugin"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  - " + HELP_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Lists all the commands for EDS"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
        Text.of("  - " + PREFERENCE_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> Text.of("Allows the setting of user preferences"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
    }

    private void changeModeCommand(CommandContext context) throws BCIException {
        if (!context.hasAnyPermission(CLASSIC_MODE, COPY_MODE, FREEZE_MODE)) context.noPermission();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        DebugModeType<?, ?> mode = DebugModeType.getDebugMode(context.argAt(0));
        if (mode == null) {
            session.sendForcedBar(ChatColor.RED.toString() +  ChatColor.BOLD + "\"" + context.argAt(0) + "\" is not a debug mode");
            if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
        }
        else {
            if (!session.hasPermission(mode)) context.noPermission(ChatColor.RED + "You do not have permssion for that Debug Mode.");
            session.sendMessage(GRAY + "Your debug mode is now \"" + mode.getNiceName() + "\"");
            session.setDebugMode(mode);
        }
    }

    private void completions(TabContext context) throws BCIException {
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        if (context.isLength(1)) {
            List<String> completions = new ArrayList<>();
            if (session.getPref(Preference.SHIFT_MODE) == ShiftMode.COMMAND) completions.addAll(DebugModeType.getDebugModes().stream().filter(mode -> context.hasPermission(mode.getBasePermission())).map(DebugModeType::getNiceName).collect(Collectors.toList()));
            if (ConfigKey.ENABLE_PREFERENCES && context.hasPermission(PREFERENCE_COMMAND)) completions.add("preference");
            if (context.hasPermission(RELOAD_COMMAND)) completions.add("reload");
            if (context.hasPermission(ABOUT_COMMAND)) completions.add("about");
            if (context.hasPermission(HELP_COMMAND)) completions.add("help");
            context.completion(completions.toArray(new String[0]));
        }
        else if (ConfigKey.ENABLE_PREFERENCES && context.hasPermission(PREFERENCE_COMMAND)) {
            context.completionAt(1, Preference.getPreferences().stream().map(Preference::getKey).toArray(String[]::new));
            if (context.subCompletionAt(2, true, Preference.SHIFT_MODE.getKey(), (c) -> {
                session.sendBar(DARK_GRAY + "[" + Preference.SHIFT_MODE.getKey() + "] " + GRAY + Preference.SHIFT_MODE.getDescription());
                c.completion(Stream.of(ShiftMode.values()).map(ShiftMode::getName).map(String::toLowerCase).toArray(String[]::new));
            })) return;
            context.completionAt(2, (c) -> {
                Preference<?> pref = Preference.fromKey(context.getPrevious());
                if (pref == null) {
                    session.sendBar(RED + "Unknown Preference Key. Try tab-completions");
                    return new ArrayList<>();
                }
                else {
                    session.sendBar(DARK_GRAY + "[" + pref.getKey() + "] " + GRAY + pref.getDescription());
                    return IntStream.rangeClosed(1, 10).map(x -> x * 1000).mapToObj(String::valueOf).collect(Collectors.toList());
                }
            });
        }
    }

}
