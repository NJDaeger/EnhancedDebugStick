package com.njdaeger.enhanceddebugstick.modes.locked;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

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
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        DebugSession session = plugin.getDebugSession(player.getUniqueId());

        if (hasSession(player.getUniqueId()) && session.isHoldingDebugStick() && session.isDebugMode(this) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            if (!player.hasPermission(getBasePermission() + ".use")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use the Locked Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            LockedDebugContext context = session.toDebugContext(this);

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                context.unlockAllBlocks();
                player.sendMessage(ChatColor.GRAY + "Deselected all locked blocks.");
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (context.isSelected(block)) context.unlockBlock(block);
                else context.lockBlock(block);
            }

        }
    }

    /*

    TODO:
        - When a player leaves with blocks selected, make sure they come back and they are still shown as selected.
        - make it when another player tries to place a block near the locked block that it doesnt update physics
     */

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        LockedDebugContext context = plugin.getDebugSession(event.getPlayer().getUniqueId()).toDebugContext(this);
        Location location = event.getBlock().getLocation();
        if (context.needsManualPlacement(location)) {
            //event.setBuild(true);
            event.setCancelled(true);//wtf
            location.getBlock().setType(event.getItemInHand().getType(), false);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        LockedDebugContext context = plugin.getDebugSession(event.getPlayer().getUniqueId()).toDebugContext(this);
        Location location = event.getBlock().getLocation();
        if (context.needsManualPlacement(location)) {
            event.setCancelled(true);
            location.getBlock().setType(Material.AIR, false);
        }
    }

    @Override
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

    }

    private void lockCheck(Location location, Player player) {
        if (!player.hasPermission(getBasePermission() + ".use")) return;

        LockedDebugContext context = plugin.getDebugSession(player.getUniqueId()).toDebugContext(this);

        if (context == null) {
            player.sendMessage(ChatColor.RED + "Cannot bind DebugSession to LockedDebugContext.");
            Bukkit.getLogger().warning("Cannot bind DebugSession to LockedDebugContext. Please create a ticket and send your latest.log to https://github.com/NJDaeger/Bug-Reports! Sorry for this issue.");
            return;
        }

    }

}
