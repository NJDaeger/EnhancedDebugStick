package com.njdaeger.enhanceddebugstick;

import com.njdaeger.bci.defaults.BCIBuilder;
import com.njdaeger.bci.defaults.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.DEBUG_STICK;

final class DebugStickCommand {

    DebugStickCommand(EnhancedDebugStick plugin) {

        plugin.registerCommand(BCIBuilder.create("debugstick")
                .executor(this::debugStick)
                .permissions("enhanceddebugstick.command")
                .maxArgs(0)
                .locatableSenders()
                .usage("/debugstick")
                .description("Gives you a usable debug stick.")
                .aliases("dbstick", "dbs")
                .build());

    }

    private void debugStick(CommandContext context) {
        Player player = context.asPlayer();
        context.pluginMessage(ChatColor.GRAY + "You now have the Debug Stick");
        player.getInventory().addItem(DEBUG_STICK);

    }

}
