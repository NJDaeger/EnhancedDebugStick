package com.njdaeger.enhanceddebugstick.modes.copy;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.api.config.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.Permissions;
import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import com.njdaeger.enhanceddebugstick.api.event.CopyPropertyEvent;
import com.njdaeger.enhanceddebugstick.api.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.i18n.Translation;
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

    public CopyDebugMode(EnhancedDebugStickApi plugin) {
        super("Copy", CopyDebugMode.class, plugin);
    }

    @Override
    public CopyDebugContext createContext(IDebugSession session) {
        return new CopyDebugContext(session);
    }

    @Override
    public String getBasePermission() {
        return Permissions.COPY_MODE;
    }

    @Override
    public void pauseSession(IDebugSession session) {
        paused.add(session.getSessionId());
    }

    @Override
    public void resumeSession(IDebugSession session) {
        paused.remove(session.getSessionId());
    }

    @Override
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        IDebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        
        if (session.isUsing(this) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            //
            //Check if the player has the correct permission first
            //
            if (!session.hasPermission(this)) {
                session.sendMessage(Translation.COPY_NO_PERM.get().apply());
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
                    session.sendForcedBar(Translation.COPY_EMPTY_CLIPBOARD.get().apply());
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.setClipboard(null);
                session.sendMessage(Translation.COPY_CLEARED_CLIPBOARD.get().apply());
                //todo clear clipboard event
                if (ConfigKey.get().COPY_CLEAR_SOUND) session.sendSound(Sound.ENTITY_SHULKER_BULLET_HIT);
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                CopyPropertyEvent e = new CopyPropertyEvent(event.getPlayer(), block, context);
                Bukkit.getPluginManager().callEvent(e);

                if (e.isCancelled()) return;

                if (e.getCopiedBlock() == null) {
                    session.sendForcedBar(Translation.COPY_NO_BLOCK_TO_COPY.get().apply());
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (!IProperty.hasProperties(e.getCopiedBlock())) {
                    session.sendForcedBar(Translation.COPY_NO_PROPS.get().apply(e.getCopiedBlock()));
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.setClipboard(e.getCopiedBlock().getBlockData());
                context.sendMeshedPropertiesOf(e.getCopiedBlock());
                session.sendMessage(Translation.COPIED_ALL_PROPS.get().apply(e.getCopiedBlock()));
                if (ConfigKey.get().COPY_COPY_SOUND) session.sendSound(Sound.ENTITY_SHEEP_SHEAR);
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (!context.hasClipboard()) {
                    session.sendForcedBar(Translation.COPY_EMPTY_CLIPBOARD.get().apply());
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                PastePropertyEvent e = new PastePropertyEvent(event.getPlayer(), block, context);
                Bukkit.getPluginManager().callEvent(e);
                
                if (e.isCancelled()) return;

                if (e.getBefore() == null) {
                    session.sendForcedBar(Translation.COPY_NO_BLOCK_TO_PASTE.get().apply());
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (!IProperty.hasProperties(e.getBefore())) {
                    session.sendForcedBar(Translation.COPY_NO_PROPS.get().apply(e.getBefore()));
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (IProperty.getProperties(e.getBefore()).stream().noneMatch(pr -> context.getClipboardProperties().contains(pr))) {
                    session.sendForcedBar(Translation.COPY_NO_APPL_PROPS.get().apply(e.getBefore()));
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                context.applyClipboardFor(e.getBefore());
                context.sendMeshedPropertiesOf(e.getBefore());
                session.sendMessage(Translation.COPY_PASTED_PROPS.get().apply(e.getBefore()));
                if (ConfigKey.get().COPY_PASTE_SOUND) session.sendSound(Sound.ENTITY_TROPICAL_FISH_FLOP);
            }
        }
    }

    @Override
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        //
        //Check if the configuration allows data viewing
        //
        IDebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

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
