package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.DebugStickAPI;
import com.njdaeger.enhanceddebugstick.api.Permissions;
import com.njdaeger.enhanceddebugstick.api.ShiftMode;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.session.Preference;
import com.njdaeger.pdk.command.CommandBuilder;
import com.njdaeger.pdk.command.CommandContext;
import com.njdaeger.pdk.command.TabContext;
import com.njdaeger.pdk.command.exception.PDKCommandException;
import com.njdaeger.pdk.types.ParsedType;
import com.njdaeger.pdk.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class DebugStickCommand {

    DebugStickCommand(EnhancedDebugStick plugin) {
        CommandBuilder.of("debugstick", "dbs", "dbstick")
                .executor(this::debugStick)
                .completer(this::completions)
                .permissions(Permissions.GET_COMMAND,
                        Permissions.ABOUT_COMMAND,
                        Permissions.CLASSIC_MODE,
                        Permissions.COPY_MODE,
                        Permissions.FREEZE_MODE,
                        Permissions.RELOAD_COMMAND,
                        Permissions.HELP_COMMAND,
                        Permissions.PREFERENCE_COMMAND)
                .usage("/debugstick [mode|subcommand]")
                .description("Gives you a usable debug stick or runs one of the many subcommands. Do '/dbs help' for a list of commands.")
                .register(plugin);
    }

    private void debugStick(CommandContext context) throws PDKCommandException {
        Player player = context.asPlayer();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(player.getUniqueId());

        if (context.isLength(0) && context.hasPermission(Permissions.GET_COMMAND)) {
            session.sendMessage(ChatColor.GRAY + "You now have the Debug Stick");
            player.getInventory().addItem(DebugStickAPI.DEBUG_STICK);
            return;
        }
        if (context.subCommandAt(0, "about", true, this::aboutCommand)) return;
        if (context.subCommandAt(0, "help", true, this::helpCommand)) return;
        if (context.subCommandAt(0, "reload", true, this::reloadCommand)) return;
        if (ConfigKey.get().ENABLE_PREFERENCES && context.subCommandAt(0, "preference", true, this::preferenceCommand)) return;
        if (context.subCommand((ctx) -> ctx.isLength(1) && session.getPref(Preference.SHIFT_MODE) == ShiftMode.COMMAND, this::changeModeCommand)) return;
        session.sendMessage(ChatColor.GRAY + "Arguments provided do not match any subcommand. Do '/dbs help' for a list of commands.");
    }

    private <T> void preferenceCommand(CommandContext context) throws PDKCommandException {
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());

        //Check if the user has permission
        if (!context.hasPermission(Permissions.PREFERENCE_COMMAND)) context.noPermission();

        //Parse the preference given
        var pref = (Preference<T, ? extends ParsedType<T>>) Preference.fromKey(context.argAt(1));
        if (pref == null) {
            session.sendMessage(ChatColor.GRAY + "Unknown Preference.");
            return;
        }

        T type =  context.argAt(2, pref.getParser(), pref.getDefault());
        session.setPref(pref, type);
        session.sendMessage(ChatColor.GRAY + "Preference '" + pref.getKey() + "' has been set to '" + (type.toString().toLowerCase()) + ".'");
    }

    private void reloadCommand(CommandContext context) throws PDKCommandException {

        //Check permission
        if (!context.hasPermission(Permissions.RELOAD_COMMAND)) context.noPermission();

        EnhancedDebugStick.getInstance().onDisable();
        EnhancedDebugStick.getInstance().onEnable();
        context.send(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Plugin Reloaded.");
    }

    private void helpCommand(CommandContext context) throws PDKCommandException {

        //Check permission
        if (!context.hasPermission(Permissions.HELP_COMMAND)) context.noPermission();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());

        //Display/create all the help
        session.sendMessage(ChatColor.GRAY + "<< EDS Command Help (Hover for Details, Click to Run) >>");

        //No-args command
        Text.of("  /dbs")
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Gives you an Enhanced Debug Stick\n").setBold(true).append("Aliases: ").append("debugstick, dbstick").setColor(ChatColor.GRAY).setItalic(true))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs")
                .sendTo(context.asPlayer());

        //The mode selection
        Text.of("  /dbs <mode>")
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Lets you change modes with a command\n").append("  <mode>").setItalic(true).setColor(ChatColor.GRAY).append(" - The mode to change to"))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs ")
                .sendTo(context.asPlayer());

        //The about command
        Text.of("  /dbs about")
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Gives information about the plugin").setBold(true))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs about")
                .sendTo(context.asPlayer());

        //The reload command
        Text.of("  /dbs reload")
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Reload the plugin").setBold(true))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs reload")
                .sendTo(context.asPlayer());

        //The mode selection
        Text.of("  /dbs preference <preference> [value]")
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Custom settings per user\n")
                        .append("  <preference>").setItalic(true).setColor(ChatColor.GRAY).append(" - The preference to change\n")
                        .append("  [value]").setItalic(true).setColor(ChatColor.GRAY).append(" - The new value"))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs preference ")
                .sendTo(context.asPlayer());
    }

    private void aboutCommand(CommandContext context) throws PDKCommandException {

        //Check permission
        if (!context.hasPermission(Permissions.ABOUT_COMMAND)) context.noPermission();

        //Get basics such as api verson and plugin version
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        session.sendMessage(ChatColor.GRAY + " << About the EnhancedDebugStick >>");
        context.send(ChatColor.GRAY + "  API Version: " + ChatColor.ITALIC + ChatColor.BLUE + DebugStickAPI.getApiVersion());
        context.send(ChatColor.GRAY + "  Plugin Version: " + ChatColor.ITALIC + ChatColor.BLUE + EnhancedDebugStick.getInstance().getDescription().getVersion());
        context.send(ChatColor.GRAY + "  Permissions: (Hover for Details)");

        //No-args command permission
        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.GET_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Get the Debug Stick permission\n").setBold(true).append("  /dbs").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.ABOUT_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Get version and permissions of the EDS plugin\n").setBold(true).append("  /dbs about").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.CLASSIC_MODE)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Classic Debug Mode permission\n").setBold(true).append("  /dbs classic").setColor(ChatColor.GRAY).append(" (If command is enabled)").setItalic(true))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.COPY_MODE)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Copy Debug Mode permission\n").setBold(true).append("  /dbs copy").setColor(ChatColor.GRAY).append(" (If command is enabled)").setItalic(true))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.FREEZE_MODE)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Freeze Debug Mode permission\n").setBold(true).append("  /dbs freeze").setColor(ChatColor.GRAY).append(" (If command is enabled)").setItalic(true))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.RELOAD_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Reload plugin permission\n").setBold(true).append("  /dbs reload").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.HELP_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("EDS Help Menu permission\n").setBold(true).append("  /dbs help").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.PREFERENCE_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .hoverEvent(Text.HoverAction.SHOW_TEXT, Text.of("Allows the setting of user preferences\n").setBold(true).append("  /dbs preference ...").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());
    }

    private void changeModeCommand(CommandContext context) throws PDKCommandException {
        //Permission check. Need to have any of the 3 mode permissions to run command
        if (!context.hasAnyPermission(Permissions.CLASSIC_MODE, Permissions.COPY_MODE, Permissions.FREEZE_MODE)) context.noPermission();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        DebugModeType<?, ?> mode = DebugModeType.getDebugMode(context.argAt(0));
        if (mode == null) {
            session.sendForcedBar(ChatColor.RED.toString() +  ChatColor.BOLD + "\"" + context.argAt(0) + "\" is not a debug mode");
            if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
            return;
        }
        //Check if they have permission for the mode they tried to use
        if (!session.hasPermission(mode)) context.noPermission(ChatColor.RED + "You do not have permssion for that Debug Mode.");
        session.sendMessage(ChatColor.GRAY + "Your debug mode is now \"" + mode.getNiceName() + "\"");
        session.setDebugMode(mode);
        if (ConfigKey.get().MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10);
    }

    private void completions(TabContext context) throws PDKCommandException {
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(context.asPlayer().getUniqueId());
        if (context.isLength(1)) {
            List<String> completions = new ArrayList<>();
            if (session.getPref(Preference.SHIFT_MODE) == ShiftMode.COMMAND) completions.addAll(DebugModeType.getDebugModes().stream().filter(mode -> context.hasPermission(mode.getBasePermission())).map(DebugModeType::getNiceName).collect(Collectors.toList()));
            if (ConfigKey.get().ENABLE_PREFERENCES && context.hasPermission(Permissions.PREFERENCE_COMMAND)) completions.add("preference");
            if (context.hasPermission(Permissions.RELOAD_COMMAND)) completions.add("reload");
            if (context.hasPermission(Permissions.ABOUT_COMMAND)) completions.add("about");
            if (context.hasPermission(Permissions.HELP_COMMAND)) completions.add("help");
            context.completion(completions.toArray(new String[0]));
        }
        else if (ConfigKey.get().ENABLE_PREFERENCES && context.hasPermission(Permissions.PREFERENCE_COMMAND)) {
            if (context.isPrevious(true, "preference")) context.completionAt(1, Preference.getPreferences().stream().map(Preference::getKey).toArray(String[]::new));
            if (context.argAt(0).equalsIgnoreCase("preference") && context.isLength(3)) {
                if (context.subCompletionAt(2, true, Preference.SHIFT_MODE.getKey(), (c) -> {
                    session.sendBar(ChatColor.DARK_GRAY + ChatColor.BOLD.toString() +"[" + Preference.SHIFT_MODE.getKey() + "] " + ChatColor.GRAY + Preference.SHIFT_MODE.getDescription());
                    c.completion(Stream.of(ShiftMode.values()).map(ShiftMode::getName).map(String::toLowerCase).toArray(String[]::new));
                })) return;
                context.completionAt(2, (c) -> {
                    Preference<?, ?> pref = Preference.fromKey(context.getPrevious());
                    if (pref == null) {
                        session.sendBar(ChatColor.RED + ChatColor.BOLD.toString() + "Unknown Preference Key. Try tab-completions");
                        return new ArrayList<>();
                    }
                    else {
                        session.sendBar(ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "[" + pref.getKey() + "] " + ChatColor.GRAY + pref.getDescription());
                        return IntStream.rangeClosed(1, 10).map(x -> x * 1000).mapToObj(String::valueOf).collect(Collectors.toList());
                    }
                });
            }
        }
    }

}
