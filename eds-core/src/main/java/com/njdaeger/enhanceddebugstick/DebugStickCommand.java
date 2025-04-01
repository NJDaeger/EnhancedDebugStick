package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.api.config.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.mode.ShiftMode;
import com.njdaeger.enhanceddebugstick.api.session.Preference;
import com.njdaeger.enhanceddebugstick.command.DebugModeArgument;
import com.njdaeger.enhanceddebugstick.command.PreferenceArgument;
import com.njdaeger.enhanceddebugstick.i18n.Translation;
import com.njdaeger.enhanceddebugstick.session.DefaultPreferences;
import com.njdaeger.pdk.command.brigadier.ICommandContext;
import com.njdaeger.pdk.command.brigadier.builder.CommandBuilder;
import com.njdaeger.pdk.command.brigadier.builder.PdkArgumentTypes;
import com.njdaeger.pdk.command.exception.CommandSenderTypeException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;

final class DebugStickCommand {

    private EnhancedDebugStickApi plugin;
    
    DebugStickCommand(EnhancedDebugStick plugin) {
        this.plugin = plugin;

        CommandBuilder.of("debugstick", "dbs", "dbstick")
                .permission(Permissions.GET_COMMAND, Permissions.CLASSIC_MODE, Permissions.COPY_MODE, Permissions.FREEZE_MODE)
                .canExecute(this::dbs)
                .description(Translation.COMMAND_INFO_DESC.get().apply())
                .then("about").permission(Permissions.ABOUT_COMMAND).executes(this::about)
                .then("reload").permission(Permissions.RELOAD_COMMAND).executes(this::reload)
                .then("preference").permission(Permissions.PREFERENCE_COMMAND)
                    .then(DefaultPreferences.SHIFT_MODE.getKey().toLowerCase())
                        .then("shiftMode", PdkArgumentTypes.enumArg(ShiftMode.class)).executes(this::preference)
                    .end()
                    .then("preference", new PreferenceArgument())
                        .then("value", PdkArgumentTypes.longArg(0, () -> "The value for this preference.")).executes(this::preference)
                    .end()
                .end()
                .then("help").permission(Permissions.HELP_COMMAND).executes(this::help)
                .then("mode", new DebugModeArgument(plugin)).executes(this::modeChange)
                .register(plugin);
    }

    private void dbs(ICommandContext ctx) throws CommandSenderTypeException {
        var player = ctx.asPlayer();
        var session = plugin.getDebugSession(player.getUniqueId());
        session.sendMessage(Translation.COMMAND_MESSAGES_GIVEN_DEBUG_STICK.get().apply());
        player.getInventory().addItem(EnhancedDebugStickApi.DEBUG_STICK);
    }

    private void modeChange(ICommandContext ctx) throws CommandSenderTypeException {
        var mode = ctx.getTyped("mode", DebugModeType.class);
        var session = plugin.getDebugSession(ctx.asPlayer().getUniqueId());

        session.sendMessage(Translation.COMMAND_MESSAGES_MODE_SET.get().apply(mode));
        session.setDebugMode(mode);
        if (ConfigKey.get().MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10);
    }

    private void about(ICommandContext ctx) {
        ctx.send(Translation.COMMAND_ABOUT_HEADER.get().apply());
        ctx.send(Translation.COMMAND_ABOUT_API_VERSION.get().apply(EnhancedDebugStickApi.getApiVersion()));
        ctx.send(Translation.COMMAND_ABOUT_PLUGIN_VERSION.get().apply(plugin.getDescription().getVersion()));
        ctx.send(Translation.COMMAND_ABOUT_PERM_HEADER.get().apply());
        var permDbs = Component.text("  - ", NamedTextColor.GRAY).append(Component.text(Permissions.GET_COMMAND, NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_ABOUT_PERM_DBS.get().apply()).appendNewline().append(Component.text("  /dbs", NamedTextColor.GRAY)))));
        var permAbout = Component.text("  - ", NamedTextColor.GRAY).append(Component.text(Permissions.ABOUT_COMMAND, NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_ABOUT_PERM_ABOUT.get().apply()).appendNewline().append(Component.text("  /dbs about", NamedTextColor.GRAY)))));
        var permClassic = Component.text("  - ", NamedTextColor.GRAY).append(Component.text(Permissions.CLASSIC_MODE, NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_ABOUT_PERM_CLASSIC.get().apply()).appendNewline().append(Component.text("  /dbs classic ", NamedTextColor.GRAY).append(Component.text(Translation.COMMAND_ABOUT_PERM_IF_ENABLED.get().apply()).decorate(TextDecoration.ITALIC))))));
        var permCopy = Component.text("  - ", NamedTextColor.GRAY).append(Component.text(Permissions.COPY_MODE, NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_ABOUT_PERM_COPY.get().apply()).appendNewline().append(Component.text("  /dbs copy ", NamedTextColor.GRAY).append(Component.text(Translation.COMMAND_ABOUT_PERM_IF_ENABLED.get().apply()).decorate(TextDecoration.ITALIC))))));
        var permFreeze = Component.text("  - ", NamedTextColor.GRAY).append(Component.text(Permissions.FREEZE_MODE, NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_ABOUT_PERM_FREEZE.get().apply()).appendNewline().append(Component.text("  /dbs freeze ", NamedTextColor.GRAY).append(Component.text(Translation.COMMAND_ABOUT_PERM_IF_ENABLED.get().apply()).decorate(TextDecoration.ITALIC))))));
        var permReload = Component.text("  - ", NamedTextColor.GRAY).append(Component.text(Permissions.RELOAD_COMMAND, NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_ABOUT_PERM_RELOAD.get().apply()).appendNewline().append(Component.text("  /dbs reload", NamedTextColor.GRAY)))));
        var permHelp = Component.text("  - ", NamedTextColor.GRAY).append(Component.text(Permissions.HELP_COMMAND, NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_ABOUT_PERM_HELP.get().apply()).appendNewline().append(Component.text("  /dbs help", NamedTextColor.GRAY)))));
        var permPref = Component.text("  - ", NamedTextColor.GRAY).append(Component.text(Permissions.PREFERENCE_COMMAND, NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_ABOUT_PERM_PREF.get().apply()).appendNewline().append(Component.text("  /dbs preference ...", NamedTextColor.GRAY)))));
        ctx.send(permDbs);
        ctx.send(permAbout);
        ctx.send(permClassic);
        ctx.send(permCopy);
        ctx.send(permFreeze);
        ctx.send(permReload);
        ctx.send(permHelp);
        ctx.send(permPref);
    }

    private void reload(ICommandContext ctx) {
        plugin.onDisable();
        plugin.onEnable();
        ctx.send(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("EDS", NamedTextColor.BLUE))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Translation.COMMAND_MESSAGES_PLUGIN_RELOADED.get().apply()));
    }

    private void preference(ICommandContext ctx) throws CommandSenderTypeException {
        var session = plugin.getDebugSession(ctx.asPlayer().getUniqueId());
        var pref = ctx.getTyped("preference", Preference.class);
        if (pref.getKey().equalsIgnoreCase(DefaultPreferences.SHIFT_MODE.getKey())) {
            var mode = ctx.getTyped("shiftMode", ShiftMode.class);
            session.setPreference(DefaultPreferences.SHIFT_MODE, mode);
            session.sendMessage(Translation.COMMAND_MESSAGES_PREFERENCE_SET.get().apply(pref, mode));
        } else {
            var value = ctx.getTyped("value", long.class, (long) pref.getDefault());
            session.setPreference(pref, value);
            session.sendMessage(Translation.COMMAND_MESSAGES_PREFERENCE_SET.get().apply(pref, value));
        }
    }

    private void help(ICommandContext ctx) {
        ctx.send(Translation.COMMAND_HELP_HEADER.get().apply());

        var dbsHelp = Component.text("  /dbs", NamedTextColor.GRAY, TextDecoration.ITALIC)
                .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_HELP_DBS.get().apply()).appendNewline().append(Component.text("Aliases: ", NamedTextColor.GRAY).append(Component.text("debugstick, dbstick", NamedTextColor.GRAY, TextDecoration.ITALIC)))));

        var dbsMode = Component.text("  /dbs <mode>", NamedTextColor.GRAY, TextDecoration.ITALIC)
                .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_HELP_MODE.get().apply()).appendNewline().append(Component.text("<mode>", NamedTextColor.GRAY, TextDecoration.ITALIC).append(Component.text(" - The mode to change to")))));

        var dbsAbout = Component.text("  /dbs about", NamedTextColor.GRAY, TextDecoration.ITALIC)
                .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_HELP_ABOUT.get().apply())));

        var dbsReload = Component.text("  /dbs reload", NamedTextColor.GRAY, TextDecoration.ITALIC)
                .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_HELP_RELOAD.get().apply())));

        var dbsPref = Component.text("  /dbs preference <preference> [value]", NamedTextColor.GRAY, TextDecoration.ITALIC)
                .hoverEvent(HoverEvent.showText(Component.text(Translation.COMMAND_HELP_PREF_DESC.get().apply()).appendNewline()
                        .append(Component.text("<preference>", NamedTextColor.GRAY, TextDecoration.ITALIC).append(Component.text(" - " + Translation.COMMAND_HELP_PREF_PREF.get().apply())).appendNewline()
                                .append(Component.text("[value]", NamedTextColor.GRAY, TextDecoration.ITALIC).append(Component.text(" - " + Translation.COMMAND_HELP_PREF_VALUE.get().apply()))))));

        ctx.send(dbsHelp);
        ctx.send(dbsMode);
        ctx.send(dbsAbout);
        ctx.send(dbsReload);
        ctx.send(dbsPref);
    }
}
