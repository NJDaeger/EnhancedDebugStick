package com.njdaeger.enhanceddebugstick.modes.locked;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

public class FreezeDebugMode extends DebugModeType<FreezeDebugMode, FreezeDebugContext> {

    public FreezeDebugMode() {
        super("Freeze", FreezeDebugMode.class);
    }

    @Override
    public String getBasePermission() {
        return "enhanceddebugstick.freeze";
    }

    @Override
    public FreezeDebugMode getModeType() {
        return DebugModeType.FREEZE;
    }

    @Override
    public void pauseSession(DebugSession session) {
        session.toDebugContext(this).unlightFrozen();
        paused.add(session.getSessionId());
    }

    @Override
    public void resumeSession(DebugSession session) {
        session.toDebugContext(this).lightFrozen();
        paused.add(session.getSessionId());
    }

    @Override
    public FreezeDebugContext createContext(DebugSession session) {
        return new FreezeDebugContext(session);
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
                player.sendMessage(ChatColor.RED + "You do not have permission to use the Freeze Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            FreezeDebugContext context = session.toDebugContext(this);

            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                context.unfreezeAllBlocks();
                player.sendMessage(ChatColor.GRAY + "Unfroze all frozen blocks.");
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (context.isSelected(block)) context.unfreezeBlock(block);
                else context.freezeBlock(block);
            }

        }
    }

    @Override
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

    }

}
