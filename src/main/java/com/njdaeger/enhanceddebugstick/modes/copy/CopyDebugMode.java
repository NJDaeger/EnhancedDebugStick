package com.njdaeger.enhanceddebugstick.modes.copy;

import com.njdaeger.btu.ActionBar;
import com.njdaeger.btu.Text;
import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

public class CopyDebugMode extends DebugModeType<CopyDebugMode, CopyDebugContext> {

    public CopyDebugMode() {
        super("Copy", CopyDebugMode.class);
    }

    @Override
    public CopyDebugContext createContext(DebugSession session) {
        return new CopyDebugContext(session);
    }

    @Override
    public String getBasePermission() {
        return "enhanceddebugstick.copy";
    }

    @Override
    public CopyDebugMode getModeType() {
        return DebugModeType.COPY;
    }

    @Override
    public void pauseSession(DebugSession session) {
        paused.add(session.getSessionId());
    }

    @Override
    public void resumeSession(DebugSession session) {
        paused.remove(session.getSessionId());
    }

    @Override
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        DebugSession session = plugin.getDebugSession(player.getUniqueId());

        if (hasSession(player.getUniqueId()) && !isPaused(session) && session.isHoldingDebugStick() && session.isDebugMode(this) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            if (!player.hasPermission(getBasePermission() + "use")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use the Copy Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            CopyDebugContext context = session.toDebugContext(this);

            if (event.getAction() == Action.LEFT_CLICK_AIR) {
                context.setClipboard(null);
                player.sendMessage(ChatColor.GRAY + "Clipboard Cleared");
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                if (!IProperty.hasProperties(block)) {
                    ActionBar.of(ChatColor.GRAY.toString() + ChatColor.BOLD + "This block has no properties").sendTo(player);
                    return;
                }

                context.setClipboard(block.getBlockData());
                player.sendMessage(ChatColor.GRAY + "Copied all properties.");
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (!context.hasClipboard()) {
                    ActionBar.of(ChatColor.GRAY.toString() + ChatColor.BOLD + "Your clipboard is empty").sendTo(player);
                    return;
                }

                if (!IProperty.hasProperties(block)) {
                    ActionBar.of(ChatColor.GRAY.toString() + ChatColor.BOLD + "This block has no properties").sendTo(player);
                    return;
                }

                if (IProperty.getProperties(block).stream().noneMatch(pr -> context.getClipboardProperties().contains(pr))) {
                    ActionBar.of(ChatColor.GRAY.toString() + ChatColor.BOLD + "There are no applicable properties to apply to this block").sendTo(player);
                    return;
                }

                context.applyClipboardFor(block);
                player.sendMessage(ChatColor.GRAY + "Pasted applicable properties.");
                if (ConfigKey.COPY_PASTE_SOUND) player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
            }
        }
    }

    @Override
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        //
        //Check if the configuration allows data viewing
        //
        Player player = event.getPlayer();
        if (hasSession(player.getUniqueId()) && ConfigKey.COPY_DISPLAY_ON_LOOK) {
            CopyDebugContext context = getDebugContext(player.getUniqueId());
            if (context.getDebugSession().isHoldingDebugStick() && !isPaused(context.getDebugSession()) && player.hasPermission(getBasePermission() + ".use")) {
                RayTraceResult hit = player.rayTraceBlocks(ConfigKey.COPY_DISPLAY_DISTANCE);
                if (hit != null && hit.getHitBlock() != null && context.hasClipboard()) context.sendMeshedPropertiesOf(hit.getHitBlock());
            }
        }
    }
}
