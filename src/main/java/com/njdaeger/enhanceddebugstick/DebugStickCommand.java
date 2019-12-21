package com.njdaeger.enhanceddebugstick;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.defaults.BCIBuilder;
import com.njdaeger.bci.defaults.CommandContext;
import com.njdaeger.bci.defaults.TabContext;
import com.njdaeger.bci.types.ParsedType;
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

import static com.njdaeger.enhanceddebugstick.api.Permissions.*;
import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.DEBUG_STICK;
import static org.bukkit.ChatColor.*;

final class DebugStickCommand {

    DebugStickCommand(EnhancedDebugStick plugin) {

        plugin.registerCommand(BCIBuilder.create("debugstick")
                .executor(this::debugStick)
                .completer(this::completions)
                .permissions(GET_COMMAND,
                        ABOUT_COMMAND,
                        CLASSIC_MODE,
                        COPY_MODE,
                        FREEZE_MODE,
                        RELOAD_COMMAND,
                        HELP_COMMAND,
                        PREFERENCE_COMMAND)
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

        if (context.isLength(0) && context.hasPermission(GET_COMMAND)) {
            session.sendMessage(GRAY + "You now have the Debug Stick");
            player.getInventory().addItem(DEBUG_STICK);
            return;
        }
        if (context.subCommandAt(0, "about", true, this::aboutCommand)) return;
        if (context.subCommandAt(0, "help", true, this::helpCommand)) return;
        if (context.subCommandAt(0, "reload", true, this::reloadCommand)) return;
        if (ConfigKey.ENABLE_PREFERENCES && context.subCommandAt(0, "preference", true, this::preferenceCommand)) return;
        if (context.subCommand((ctx) -> ctx.isLength(1) && session.getPref(Preference.SHIFT_MODE) == ShiftMode.COMMAND, this::changeModeCommand)) return;
        session.sendMessage(GRAY + "Arguments provided do not match any subcommand. Do '/dbs help' for help.");
    }

    private <T> void preferenceCommand(CommandContext context) throws BCIException {
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());

        //Check if the user has permission
        if (!context.hasPermission(PREFERENCE_COMMAND)) context.noPermission();

        //Parse the preference given
        Preference<T, ? extends ParsedType<T>> pref = (Preference<T, ? extends ParsedType<T>>) Preference.fromKey(context.argAt(1));
        if (pref == null) {
            session.sendMessage(GRAY + "Unknown Preference.");
            return;
        }

        T type =  context.argAt(2, pref.getParser(), pref.getDefault());
        session.setPref(pref, type);
        session.sendMessage(GRAY + "Preference '" + pref.getKey() + "' has been set to '" + (type.toString().toLowerCase()) + ".'");
    }

    private void reloadCommand(CommandContext context) throws BCIException {

        //Check permission
        if (!context.hasPermission(RELOAD_COMMAND)) context.noPermission();

        //Check if the configuration file exists.
        if (!new File(EnhancedDebugStick.getInstance().getDataFolder() + File.separator + "config.yml").exists()) {
            context.send(DARK_GRAY + "[" + BLUE + "EDS" + DARK_GRAY + "] " + GRAY + "config.yml did not exist. Creating it...");
            EnhancedDebugStick.getInstance().saveResource("config.yml", false);
            EnhancedDebugStick.getInstance().configuration = new ConfigurationFile(EnhancedDebugStick.getInstance());
        }
        else EnhancedDebugStick.getInstance().getDebugConfig().reload();
        context.send(DARK_GRAY + "[" + BLUE + "EDS" + DARK_GRAY + "] " + GRAY + "Configuration Reloaded.");
    }

    private void helpCommand(CommandContext context) throws BCIException {

        //Check permission
        if (!context.hasPermission(HELP_COMMAND)) context.noPermission();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());

        //Display/create all the help
        session.sendMessage(GRAY + " <<< EnhancedDebugStick Help (Hover for Details) >>>");

        //No-args command
        Text.of("  /dbs").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Gives you an Enhanced Debug Stick\n").setBold(true)
                    .append("Aliases: ").append("debugstick, dbstick").setColor(GRAY).setItalics(true));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The mode selection
        Text.of("  /dbs <mode>").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Lets you change modes with a command\n").setBold(true)
                    .append("  <mode>").setItalics(true).setColor(GRAY).append(" - The mode to change to"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The about command
        Text.of("  /dbs about").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Gives information about the plugin").setBold(true));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The reload command
        Text.of("  /dbs reload").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Reload the plugin").setBold(true));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The preference command
        Text.of("  /dbs preference <preference> [value]").hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Custom settings per user\n").setBold(true)
                    .append("  <preference>").setItalics(true).setColor(GRAY).append(" - The preference to change\n")
                    .append("  [value]").setItalics(true).setColor(GRAY).append(" - The new value"));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
    }

    private void aboutCommand(CommandContext context) throws BCIException {

        //Check permission
        if (!context.hasPermission(ABOUT_COMMAND)) context.noPermission();

        //Get basics such as api verson and plugin version
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        session.sendMessage(GRAY + " <<< About the EnhancedDebugStick >>>");
        context.send(GRAY + "  API Version: " + ITALIC + BLUE + DebugStickAPI.getApiVersion());
        context.send(GRAY + "  Plugin Version: " + ITALIC + BLUE + EnhancedDebugStick.getInstance().getDescription().getVersion());
        context.send(GRAY + "  Permissions: (Hover for Details)");

        //No-args command permission
        Text.of("  - " + BLUE +  GET_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Get the Debug Stick\n").setBold(true)
                            .append("  /dbs").setColor(GRAY));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The about command permission
        Text.of("  - " + BLUE + ABOUT_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Get Version and Permissions of the EDS plugin\n").setBold(true)
                            .append("  /dbs about").setColor(GRAY));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The classic debug mode permission
        Text.of("  - " + BLUE + CLASSIC_MODE).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Classic Debug Mode Permission\n").setBold(true)
                            .append("  /dbs classic").setColor(GRAY)
                            .append(" (If command is enabled)").setItalics(true));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The copy debug mode permission
        Text.of("  - " + BLUE + COPY_MODE).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Copy Debug Mode Permission\n").setBold(true)
                        .append("  /dbs copy").setColor(GRAY)
                        .append(" (If command is enabled)").setItalics(true));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The freeze debug mode permission
        Text.of("  - " + BLUE + FREEZE_MODE).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Freeze Debug Mode Permission\n").setBold(true)
                            .append("  /dbs freeze").setColor(GRAY)
                            .append(" (If command is enabled)").setItalics(true));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The reload command permission
        Text.of("  - " + BLUE + RELOAD_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Reload plugin permission\n").setBold(true)
                            .append("  /dbs reload").setColor(GRAY));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The help command permission
        Text.of("  - " + BLUE + HELP_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Permission for the plugin help menu\n").setBold(true)
                            .append("  /dbs help").setColor(GRAY));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());

        //The preference command permission
        Text.of("  - " + BLUE + PREFERENCE_COMMAND).hoverEvent((e) -> {
            e.action(Text.HoverAction.SHOW_TEXT);
            e.hover((h) -> h.append("Allows the setting of user preferences\n").setBold(true)
                            .append("  /dbs preference").setColor(GRAY));
        }).setColor(GRAY).setItalics(true).sendTo(context.asPlayer());
    }

    private void changeModeCommand(CommandContext context) throws BCIException {
        //Permission check. Need to have any of the 3 mode permissions to run command
        if (!context.hasAnyPermission(CLASSIC_MODE, COPY_MODE, FREEZE_MODE)) context.noPermission();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        DebugModeType<?, ?> mode = DebugModeType.getDebugMode(context.argAt(0));
        if (mode == null) {
            session.sendForcedBar(ChatColor.RED.toString() +  ChatColor.BOLD + "\"" + context.argAt(0) + "\" is not a debug mode");
            if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
            return;
        }
        //Check if they have permission for the mode they tried to use
        if (!session.hasPermission(mode)) context.noPermission(ChatColor.RED + "You do not have permssion for that Debug Mode.");
        session.sendMessage(GRAY + "Your debug mode is now \"" + mode.getNiceName() + "\"");
        session.setDebugMode(mode);
        if (ConfigKey.MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10);
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
        else if (ConfigKey.ENABLE_PREFERENCES && context.hasPermission(PREFERENCE_COMMAND) && context.isPrevious(true, "preference")) {
            context.completionAt(1, Preference.getPreferences().stream().map(Preference::getKey).toArray(String[]::new));
            if (context.subCompletionAt(2, true, Preference.SHIFT_MODE.getKey(), (c) -> {
                session.sendBar(DARK_GRAY + BOLD.toString() +"[" + Preference.SHIFT_MODE.getKey() + "] " + GRAY + Preference.SHIFT_MODE.getDescription());
                c.completion(Stream.of(ShiftMode.values()).map(ShiftMode::getName).map(String::toLowerCase).toArray(String[]::new));
            })) return;
            context.completionAt(2, (c) -> {
                Preference<?, ?> pref = Preference.fromKey(context.getPrevious());
                if (pref == null) {
                    session.sendBar(RED + BOLD.toString() +"Unknown Preference Key. Try tab-completions");
                    return new ArrayList<>();
                }
                else {
                    session.sendBar(DARK_GRAY + BOLD.toString() + "[" + pref.getKey() + "] " + GRAY + pref.getDescription());
                    return IntStream.rangeClosed(1, 10).map(x -> x * 1000).mapToObj(String::valueOf).collect(Collectors.toList());
                }
            });
        }
    }

}
