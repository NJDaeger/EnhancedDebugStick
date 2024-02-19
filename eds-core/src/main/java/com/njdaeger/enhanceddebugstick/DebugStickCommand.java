package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.api.config.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.mode.ShiftMode;
import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import com.njdaeger.enhanceddebugstick.api.session.Preference;
import com.njdaeger.enhanceddebugstick.i18n.Translation;
import com.njdaeger.enhanceddebugstick.modes.classic.ClassicDebugMode;
import com.njdaeger.enhanceddebugstick.modes.copy.CopyDebugMode;
import com.njdaeger.enhanceddebugstick.modes.freeze.FreezeDebugMode;
import com.njdaeger.enhanceddebugstick.session.DefaultPreferences;
import com.njdaeger.pdk.command.CommandBuilder;
import com.njdaeger.pdk.command.CommandContext;
import com.njdaeger.pdk.command.TabContext;
import com.njdaeger.pdk.command.exception.PDKCommandException;
import com.njdaeger.pdk.types.ParsedType;
import com.njdaeger.pdk.utils.text.Text;
import com.njdaeger.pdk.utils.text.hover.HoverAction;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class DebugStickCommand {

    private EnhancedDebugStickApi plugin;
    
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
                .description(Translation.COMMAND_INFO_DESC.get().apply())
                .register(plugin);
        this.plugin = plugin;
    }

    private void debugStick(CommandContext context) throws PDKCommandException {
        Player player = context.asPlayer();
        IDebugSession session = plugin.getDebugSession(player.getUniqueId());

        if (context.isLength(0) && context.hasPermission(Permissions.GET_COMMAND)) {
            session.sendMessage(Translation.COMMAND_MESSAGES_GIVEN_DEBUG_STICK.get().apply());
            player.getInventory().addItem(EnhancedDebugStickApi.DEBUG_STICK);
            return;
        }
        if (context.subCommandAt(0, "about", true, this::aboutCommand)) return;
        if (context.subCommandAt(0, "help", true, this::helpCommand)) return;
        if (context.subCommandAt(0, "reload", true, this::reloadCommand)) return;
        if (ConfigKey.get().ENABLE_PREFERENCES && context.subCommandAt(0, "preference", true, this::preferenceCommand)) return;
        if (context.subCommand((ctx) -> ctx.isLength(1) && session.getPreference(DefaultPreferences.SHIFT_MODE) == ShiftMode.COMMAND, this::changeModeCommand)) return;
        session.sendMessage(Translation.COMMAND_ERRORS_UNKNOWN_SUBCOMMAND.get().apply(context.argAt(0)));
    }

    private <T> void preferenceCommand(CommandContext context) throws PDKCommandException {
        IDebugSession session = plugin.getDebugSession(context.asPlayer().getUniqueId());

        //Check if the user has permission
        if (!context.hasPermission(Permissions.PREFERENCE_COMMAND)) context.noPermission();

        //Parse the preference given
        Preference<T, ? extends ParsedType<T>> pref = (Preference<T, ? extends ParsedType<T>>) Preference.fromKey(context.argAt(1));
        if (pref == null) {
            session.sendMessage(Translation.COMMAND_ERRORS_UNKNOWN_PREFERENCE.get().apply(context.argAt(1)));
            return;
        }

        T type =  context.argAt(2, pref.getParser(), pref.getDefault());
        session.setPreference(pref, type);
        session.sendMessage(Translation.COMMAND_MESSAGES_PREFERENCE_SET.get().apply(pref, type));
    }

    private void reloadCommand(CommandContext context) throws PDKCommandException {

        //Check permission
        if (!context.hasPermission(Permissions.RELOAD_COMMAND)) context.noPermission();

        plugin.onDisable();
        plugin.onEnable();
        context.send(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + Translation.COMMAND_MESSAGES_PLUGIN_RELOADED.get().apply());
    }

    private void helpCommand(CommandContext context) throws PDKCommandException {

        //Check permission
        if (!context.hasPermission(Permissions.HELP_COMMAND)) context.noPermission();
        IDebugSession session = plugin.getDebugSession(context.asPlayer().getUniqueId());

        //Display/create all the help
        session.sendMessage(Translation.COMMAND_HELP_HEADER.get().apply());

        //No-args command
        Text.of("  /dbs")
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_HELP_DBS.get().apply() + "\n").setBold(true).append("Aliases: ").append("debugstick, dbstick").setColor(ChatColor.GRAY).setItalic(true))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs")
                .sendTo(context.asPlayer());

        //The mode selection
        Text.of("  /dbs <mode>")
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_HELP_MODE.get().apply() + "\n").append("  <mode>").setItalic(true).setColor(ChatColor.GRAY).append(" - The mode to change to"))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs ")
                .sendTo(context.asPlayer());

        //The about command
        Text.of("  /dbs about")
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_HELP_ABOUT.get().apply()).setBold(true))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs about")
                .sendTo(context.asPlayer());

        //The reload command
        Text.of("  /dbs reload")
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_HELP_RELOAD.get().apply()).setBold(true))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs reload")
                .sendTo(context.asPlayer());

        //The mode selection
        Text.of("  /dbs preference <preference> [value]")
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_HELP_PREF_DESC.get().apply() + "\n")
                        .append("  <preference>").setItalic(true).setColor(ChatColor.GRAY).append(" - " + Translation.COMMAND_HELP_PREF_PREF.get().apply() + "\n")
                        .append("  [value]").setItalic(true).setColor(ChatColor.GRAY).append(" - " + Translation.COMMAND_HELP_PREF_VALUE.get().apply()))
                .setColor(ChatColor.GRAY)
                .setItalic(true)
                .setInsertion("/dbs preference ")
                .sendTo(context.asPlayer());
    }

    private void aboutCommand(CommandContext context) throws PDKCommandException {

        //Check permission
        if (!context.hasPermission(Permissions.ABOUT_COMMAND)) context.noPermission();

        //Get basics such as api verson and plugin version
        IDebugSession session = plugin.getDebugSession(context.asPlayer().getUniqueId());
        session.sendMessage(Translation.COMMAND_ABOUT_HEADER.get().apply());
        context.send(Translation.COMMAND_ABOUT_API_VERSION.get().apply(EnhancedDebugStickApi.getApiVersion()));
        context.send(Translation.COMMAND_ABOUT_PLUGIN_VERSION.get().apply(plugin.getDescription().getVersion()));
        context.send(Translation.COMMAND_ABOUT_PERM_HEADER.get().apply());

        //No-args command permission
        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.GET_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_ABOUT_PERM_DBS.get().apply() + "\n").setBold(true).append("  /dbs").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.ABOUT_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_ABOUT_PERM_ABOUT.get().apply() + "\n").setBold(true).append("  /dbs about").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.CLASSIC_MODE)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_ABOUT_PERM_CLASSIC.get().apply() + "\n").setBold(true).append("  /dbs classic ").setColor(ChatColor.GRAY).append(Translation.COMMAND_ABOUT_PERM_IF_ENABLED.get().apply()).setItalic(true))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.COPY_MODE)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_ABOUT_PERM_COPY.get().apply() + "\n").setBold(true).append("  /dbs copy ").setColor(ChatColor.GRAY).append(Translation.COMMAND_ABOUT_PERM_IF_ENABLED.get().apply()).setItalic(true))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.FREEZE_MODE)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_ABOUT_PERM_FREEZE.get().apply() + "\n").setBold(true).append("  /dbs freeze ").setColor(ChatColor.GRAY).append(Translation.COMMAND_ABOUT_PERM_IF_ENABLED.get().apply()).setItalic(true))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.RELOAD_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_ABOUT_PERM_RELOAD.get().apply() + "\n").setBold(true).append("  /dbs reload").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.HELP_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_ABOUT_PERM_HELP.get().apply() + "\n").setBold(true).append("  /dbs help").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());

        Text.of("  - ")
                .setColor(ChatColor.GRAY)
                .append(Permissions.PREFERENCE_COMMAND)
                .setColor(ChatColor.BLUE)
                .setItalic(true)
                .setHoverEvent(HoverAction.SHOW_TEXT, Text.of(Translation.COMMAND_ABOUT_PERM_PREF.get().apply() + "\n").setBold(true).append("  /dbs preference ...").setColor(ChatColor.GRAY))
                .sendTo(context.asPlayer());
    }

    private void changeModeCommand(CommandContext context) throws PDKCommandException {
        //Permission check. Need to have any of the 3 mode permissions to run command
        if (!context.hasAnyPermission(Permissions.CLASSIC_MODE, Permissions.COPY_MODE, Permissions.FREEZE_MODE)) context.noPermission();
        IDebugSession session = plugin.getDebugSession(context.asPlayer().getUniqueId());
        DebugModeType<?, ?> mode = DebugModeType.getDebugMode(context.argAt(0));
        if (mode == null) {
            session.sendForcedBar(Translation.COMMAND_ERRORS_UNKNOWN_DEBUG_MODE.get().apply(context.argAt(0)));
            if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
            return;
        }
        //Check if they have permission for the mode they tried to use
        if (!session.hasPermission(mode)) context.noPermission(Translation.COMMAND_ERRORS_NO_MODE_PERMISSION.get().apply(mode));
        session.sendMessage(Translation.COMMAND_MESSAGES_MODE_SET.get().apply(mode));
        session.setDebugMode(mode);
        if (ConfigKey.get().MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10);
    }

    private void completions(TabContext context) throws PDKCommandException {
        IDebugSession session = plugin.getDebugSession(context.asPlayer().getUniqueId());
        if (context.isLength(1)) {
            List<String> completions = new ArrayList<>();
            if (session.getPreference(DefaultPreferences.SHIFT_MODE) == ShiftMode.COMMAND) completions.addAll(DebugModeType.getDebugModes().stream().filter(mode -> context.hasPermission(mode.getBasePermission())).map(DebugModeType::getNiceName).collect(Collectors.toList()));
            if (ConfigKey.get().ENABLE_PREFERENCES && context.hasPermission(Permissions.PREFERENCE_COMMAND)) completions.add("preference");
            if (context.hasPermission(Permissions.RELOAD_COMMAND)) completions.add("reload");
            if (context.hasPermission(Permissions.ABOUT_COMMAND)) completions.add("about");
            if (context.hasPermission(Permissions.HELP_COMMAND)) completions.add("help");
            context.completion(completions.toArray(new String[0]));
        }
        else if (ConfigKey.get().ENABLE_PREFERENCES && context.hasPermission(Permissions.PREFERENCE_COMMAND)) {
            if (context.isPrevious(true, "preference")) context.completionAt(1, Preference.getPreferences().stream().map(Preference::getKey).toArray(String[]::new));
            if (context.argAt(0).equalsIgnoreCase("preference") && context.isLength(3)) {
                if (context.subCompletionAt(2, true, DefaultPreferences.SHIFT_MODE.getKey(), (c) -> {
                    session.sendBar(ChatColor.DARK_GRAY + ChatColor.BOLD.toString() +"[" + DefaultPreferences.SHIFT_MODE.getKey() + "] " + ChatColor.GRAY + DefaultPreferences.SHIFT_MODE.getDescription());
                    c.completion(Stream.of(ShiftMode.values()).map(ShiftMode::getName).map(String::toLowerCase).toArray(String[]::new));
                })) return;
                context.completionAt(2, (c) -> {
                    Preference<?, ?> pref = Preference.fromKey(context.getPrevious());
                    if (pref == null) {
                        session.sendBar(Translation.COMMAND_ERRORS_UNKNOWN_PREFERENCE.get().apply(context.getPrevious()));
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
