package com.njdaeger.enhanceddebugstick.modes.freeze;

import com.njdaeger.enhanceddebugstick.Permissions;
import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.api.config.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import com.njdaeger.enhanceddebugstick.api.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.api.event.UnfreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.i18n.Translation;
import com.njdaeger.enhanceddebugstick.mcversion.Version;
import com.njdaeger.enhanceddebugstick.util.highlighter.IBlockHighlighter;
import com.njdaeger.enhanceddebugstick.util.highlighter.impl.GenericBlockHighlighter;
import com.njdaeger.enhanceddebugstick.util.highlighter.impl.LegacyBlockHighlighter;
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

    private static final IBlockHighlighter highlighter;
    
    static {
        int lastGenericImpl = Version.v1_19_3.getOrdinal();
        if (Version.getCurrentVersion().getOrdinal() > lastGenericImpl) {
            highlighter = new GenericBlockHighlighter();
        }
        else {
            highlighter = new LegacyBlockHighlighter();
        }
    }
    
    public FreezeDebugMode(EnhancedDebugStickApi plugin) {
        super("Freeze", FreezeDebugMode.class, plugin);
    }

    @Override
    public String getBasePermission() {
        return Permissions.FREEZE_MODE;
    }

    @Override
    public void pauseSession(IDebugSession session) {
        session.toDebugContext(this).unlightFrozen();
        paused.add(session.getSessionId());
    }

    @Override
    public void resumeSession(IDebugSession session) {
        session.toDebugContext(this).lightFrozen();
        paused.remove(session.getSessionId());
    }

    @Override
    public FreezeDebugContext createContext(IDebugSession session) {
        return new FreezeDebugContext(session, highlighter);
    }

    @Override
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        IDebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

        if (session.isUsing(this) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            if (!session.hasPermission(this)) {
                session.sendMessage(Translation.FREEZE_NO_PERM.get().apply());
                return;
            }

            Block block = event.getClickedBlock();
            FreezeDebugContext context = session.toDebugContext(this);

            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
    
                UnfreezeBlockEvent unfreezeEvent = new UnfreezeBlockEvent(event.getPlayer(), context.getFrozen(), context);
                Bukkit.getPluginManager().callEvent(unfreezeEvent);
                
                if (unfreezeEvent.isCancelled()) return;
                
                if (!context.hasFrozenBlocks()) {
                    session.sendForcedBar(Translation.FREEZE_NO_FROZEN_BLOCKS.get().apply());
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }
                if (ConfigKey.get().FDM_UNFREEZE_ALL) session.sendSound(Sound.ITEM_TRIDENT_RETURN);
                
                int unfreezeAmount = (int) unfreezeEvent.getUnfrozenBlocks().stream().filter(context::isFrozen).count();
                int currentlyFrozen = context.getFrozen().size();
                
                context.unfreezeBlocks(unfreezeEvent.getUnfrozenBlocks());
                session.sendMessage(Translation.FREEZE_UNFROZE_BLOCKS.get().apply(unfreezeAmount, currentlyFrozen));
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
