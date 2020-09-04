package com.njdaeger.enhanceddebugstick.modes.copy;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.api.Permissions;
import com.njdaeger.enhanceddebugstick.event.CopyPropertyEvent;
import com.njdaeger.enhanceddebugstick.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
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
        return Permissions.COPY_MODE;
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
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

        if (session.isUsing(this) && session.canInteract() && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            //
            //Check if the player has the correct permission first
            //
            if (!session.hasPermission(this)) {
                session.sendMessage(ChatColor.RED + "You do not have permission to use the Copy Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            CopyDebugContext context = session.toDebugContext(this);

            //
            //If we left click air, we're clearing the clipboard.
            //
            if (event.getAction() == Action.LEFT_CLICK_AIR) {

                //
                //If the clipboard of the player is already empty, we should just tell them to stop trying to clear it.
                //
                if (!context.hasClipboard()) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "Your clipboard is clear");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.setClipboard(null);
                session.sendMessage(ChatColor.GRAY + "Clipboard Cleared");
                //todo clear clipboard event
                if (ConfigKey.get().COPY_CLEAR_SOUND) session.sendSound(Sound.ENTITY_SHULKER_BULLET_HIT);
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                CopyPropertyEvent e = new CopyPropertyEvent(event.getPlayer(), block, context);
                Bukkit.getPluginManager().callEvent(e);

                if (e.isCancelled()) return;

                if (e.getCopiedBlock() == null) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "There is no block to copy");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (!IProperty.hasProperties(e.getCopiedBlock())) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "This block has no properties");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.setClipboard(e.getCopiedBlock().getBlockData());
                context.sendMeshedPropertiesOf(e.getCopiedBlock());
                session.sendMessage(ChatColor.GRAY + "Copied all properties");
                if (ConfigKey.get().COPY_COPY_SOUND) session.sendSound(Sound.ENTITY_SHEEP_SHEAR);
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                PastePropertyEvent e = new PastePropertyEvent(event.getPlayer(), block, context);
                Bukkit.getPluginManager().callEvent(e);
                
                if (e.isCancelled()) return;

                if (e.getBefore() == null) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "There is no block to paste onto");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                //if (plot(event.getPlayer(), block)) {
                if (!context.hasClipboard()) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "Your clipboard is empty");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (!IProperty.hasProperties(e.getBefore())) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "This block has no properties");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (IProperty.getProperties(e.getBefore()).stream().noneMatch(pr -> context.getClipboardProperties().contains(pr))) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "There are no applicable properties to apply to this block");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.applyClipboardFor(e.getBefore());
                context.sendMeshedPropertiesOf(e.getBefore());
                session.sendMessage(ChatColor.GRAY + "Pasted applicable properties");
                if (ConfigKey.get().COPY_PASTE_SOUND) session.sendSound(Sound.ENTITY_TROPICAL_FISH_FLOP);
                //} else session.sendForcedBar(RED.toString() + BOLD + "You are not an owner or a member of the location pasted at.");
            }
        }
    }

    @Override
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        //
        //Check if the configuration allows data viewing
        //
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

        if (ConfigKey.get().COPY_DISPLAY_ON_LOOK && session.isUsing(this) && session.hasPermission(this)) {
            CopyDebugContext context = session.toDebugContext(this);
            if (context.getDebugSession().isHoldingDebugStick() && !isPaused(context.getDebugSession()) && context.getDebugSession().hasPermission(this)) {
                RayTraceResult hit = event.getPlayer().rayTraceBlocks(ConfigKey.get().COPY_DISPLAY_DISTANCE);
                if (hit != null && hit.getHitBlock() != null) {
                    if (context.hasClipboard()) context.sendMeshedPropertiesOf(hit.getHitBlock());
                    else context.sendPropertiesOf(hit.getHitBlock());
                }
            }
        }
    }
}
