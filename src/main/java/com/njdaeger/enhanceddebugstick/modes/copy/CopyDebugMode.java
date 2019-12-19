package com.njdaeger.enhanceddebugstick.modes.copy;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

import static org.bukkit.ChatColor.*;

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
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

        if (session.isUsing(this) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            //
            //Check if the player has the correct permission first
            //
            if (!session.hasPermission(this)) {
                session.sendMessage(RED + "You do not have permission to use the Copy Debug Mode");
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
                    session.sendForcedBar(RED.toString() + BOLD + "Your clipboard is clear");
                    if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.setClipboard(null);
                session.sendMessage(GRAY + "Clipboard Cleared");
                if (ConfigKey.COPY_CLEAR_SOUND) session.sendSound(Sound.ENTITY_SHULKER_BULLET_HIT);
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                if (!IProperty.hasProperties(block)) {
                    session.sendForcedBar(RED.toString() + BOLD + "This block has no properties");
                    if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.setClipboard(block.getBlockData());
                context.sendMeshedPropertiesOf(block);
                session.sendMessage(GRAY + "Copied all properties");
                if (ConfigKey.COPY_COPY_SOUND) session.sendSound(Sound.ENTITY_SHEEP_SHEAR);
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (!context.hasClipboard()) {
                    session.sendForcedBar(RED.toString() + BOLD + "Your clipboard is empty");
                    if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (!IProperty.hasProperties(block)) {
                    session.sendForcedBar(RED.toString() + BOLD + "This block has no properties");
                    if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (IProperty.getProperties(block).stream().noneMatch(pr -> context.getClipboardProperties().contains(pr))) {
                    session.sendForcedBar(RED.toString() + BOLD + "There are no applicable properties to apply to this block");
                    if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.applyClipboardFor(block);
                context.sendMeshedPropertiesOf(block);
                session.sendMessage(GRAY + "Pasted applicable properties");
                if (ConfigKey.COPY_PASTE_SOUND) session.sendSound(Sound.ENTITY_TROPICAL_FISH_FLOP);
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

        if (ConfigKey.COPY_DISPLAY_ON_LOOK && session.isUsing(this) && session.hasPermission(this)) {
            CopyDebugContext context = session.toDebugContext(this);
            if (context.getDebugSession().isHoldingDebugStick() && !isPaused(context.getDebugSession()) && context.getDebugSession().hasPermission(this)) {
                RayTraceResult hit = event.getPlayer().rayTraceBlocks(ConfigKey.COPY_DISPLAY_DISTANCE);
                if (hit != null && hit.getHitBlock() != null) {
                    if (context.hasClipboard()) context.sendMeshedPropertiesOf(hit.getHitBlock());
                    else context.sendPropertiesOf(hit.getHitBlock());
                }
            }
        }
    }
}
