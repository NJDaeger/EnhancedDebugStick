package com.njdaeger.enhanceddebugstick.modes.freeze;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.Permissions;
import com.njdaeger.enhanceddebugstick.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.event.UnfreezeBlockEvent;
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

import java.util.Collections;

public class FreezeDebugMode extends DebugModeType<FreezeDebugMode, FreezeDebugContext> {

    public FreezeDebugMode() {
        super("Freeze", FreezeDebugMode.class);
    }

    @Override
    public String getBasePermission() {
        return Permissions.COPY_MODE;
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
        paused.remove(session.getSessionId());
    }

    @Override
    public FreezeDebugContext createContext(DebugSession session) {
        return new FreezeDebugContext(session);
    }

    @Override
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

        if (session.isUsing(this) && session.canInteract() && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            if (!session.hasPermission(this)) {
                session.sendMessage(ChatColor.RED + "You do not have permission to use the Freeze Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            FreezeDebugContext context = session.toDebugContext(this);

            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
    
                UnfreezeBlockEvent unfreezeEvent = new UnfreezeBlockEvent(event.getPlayer(), context.getFrozen(), context);
                Bukkit.getPluginManager().callEvent(unfreezeEvent);
                
                if (unfreezeEvent.isCancelled()) return;
                
                if (!context.hasFrozenBlocks()) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "You do not have any frozen blocks");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }
                if (ConfigKey.get().FDM_UNFREEZE_ALL) session.sendSound(Sound.ITEM_TRIDENT_RETURN);
                
                long unfreezeAmount = unfreezeEvent.getUnfrozenBlocks().stream().filter(context::isFrozen).count();
                int currentlyFrozen = context.getFrozen().size();
                
                context.unfreezeBlocks(unfreezeEvent.getUnfrozenBlocks());
                session.sendMessage(String.format(ChatColor.GRAY + "Unfroze %d/%d blocks.", unfreezeAmount, currentlyFrozen));
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (context.isFrozen(block)) {
                    UnfreezeBlockEvent unfreezeEvent = new UnfreezeBlockEvent(event.getPlayer(), Collections.singletonList(block), context);
                    Bukkit.getPluginManager().callEvent(unfreezeEvent);
    
                    if (unfreezeEvent.isCancelled()) return;
                    
                    if (ConfigKey.get().FDM_UNFREEZE) session.sendSound(Sound.ITEM_AXE_STRIP);
                    context.unfreezeBlocks(unfreezeEvent.getUnfrozenBlocks());
                }
                else {
                    FreezeBlockEvent freezeEvent = new FreezeBlockEvent(event.getPlayer(), block, context);
                    Bukkit.getPluginManager().callEvent(freezeEvent);
                    
                    if (freezeEvent.isCancelled()) return;
                    
                    if (ConfigKey.get().FDM_FREEZE) session.sendSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP);
                    context.freezeBlock(freezeEvent.getFrozenBlock());
                }
            }

        }
    }

    @Override
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

    }
}
