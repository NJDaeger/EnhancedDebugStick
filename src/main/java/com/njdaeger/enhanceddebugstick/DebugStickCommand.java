package com.njdaeger.enhanceddebugstick;

import com.njdaeger.bci.defaults.BCIBuilder;
import com.njdaeger.bci.defaults.CommandContext;
import com.njdaeger.bci.defaults.TabContext;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.DEBUG_STICK;

final class DebugStickCommand {

    DebugStickCommand(EnhancedDebugStick plugin) {

        plugin.registerCommand(BCIBuilder.create("debugstick")
                .executor(this::debugStick)
                .completer(this::completions)
                .permissions("enhanceddebugstick.command")
                .maxArgs(ConfigKey.MS_COMMAND_SHIFTING ? 1 : 0)
                .locatableSenders()
                .usage("/debugstick")
                .description("Gives you a usable debug stick.")
                .aliases("dbstick", "dbs")
                .build());

    }

    private void debugStick(CommandContext context) {
        Player player = context.asPlayer();
        DebugSession session = EnhancedDebugStick.getInstance().getDebugSession(player.getUniqueId());

        if (context.isLength(1)) {
            DebugModeType<?, ?> mode = DebugModeType.getDebugMode(context.argAt(0));
            if (mode == null) {
                session.sendForcedBar(ChatColor.RED.toString() +  ChatColor.BOLD + "\"" + context.argAt(0) + "\" is not a debug mode");
                if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
            }
            else {
                session.sendMessage(ChatColor.GRAY + "Your debug mode is now \"" + mode.getNiceName() + "\"");
                session.setDebugMode(mode);
            }
        } else {
            session.sendMessage(ChatColor.GRAY + "You now have the Debug Stick");
            player.getInventory().addItem(DEBUG_STICK);
        }
    }

    private void completions(TabContext context) {
        if (ConfigKey.MS_COMMAND_SHIFTING) context.completionAt(0, DebugModeType.getDebugModes().stream().filter(mode -> context.hasPermission(mode.getBasePermission() + ".use")).map(DebugModeType::getNiceName).toArray(String[]::new));
    }


}
