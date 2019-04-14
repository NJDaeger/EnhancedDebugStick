package com.njdaeger.enhanceddebugstick.modes.locked;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

public class LockedDebugMode extends DebugModeType<LockedDebugMode, LockedDebugContext> {

    public LockedDebugMode() {
        super("Locked", LockedDebugMode.class);
    }

    @Override
    public String getBasePermission() {
        return "enhanceddebugstick.locked";
    }

    @Override
    public LockedDebugMode getModeType() {
        return DebugModeType.LOCKED;
    }

    @Override
    public LockedDebugContext createContext(DebugSession session) {
        return new LockedDebugContext(session);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        DebugSession session = plugin.getDebugSession(player.getUniqueId());

        if (hasSession(player.getUniqueId()) && session.isHoldingDebugStick() && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            if (!player.hasPermission(getBasePermission() + ".use")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use the Locked Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            LockedDebugContext context = session.toDebugContext(this);

            if (context == null) {
                player.sendMessage(ChatColor.RED + "Cannot bind DebugSession to LockedDebugContext.");
                Bukkit.getLogger().warning("Cannot bind DebugSession to LockedDebugContext. Please create a ticket and send your latest.log to https://github.com/NJDaeger/Bug-Reports! Sorry for this issue.");
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                //If the player is sneaking, deselect everything
                if (player.isSneaking()) {
                    context.unlockAllBlocks();
                    player.sendMessage(ChatColor.GRAY + "Deselected all locked blocks.");
                    return;
                }
                context.unlockBlock(block);

            }



        }
    }

    @Override
    public void onMove(PlayerMoveEvent event) {

    }

}
