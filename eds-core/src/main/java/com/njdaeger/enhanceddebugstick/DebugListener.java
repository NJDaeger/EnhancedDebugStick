package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.api.ShiftMode;
import com.njdaeger.enhanceddebugstick.event.CopyPropertyEvent;
import com.njdaeger.enhanceddebugstick.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.event.PropertyChangeEvent;
import com.njdaeger.enhanceddebugstick.event.UnfreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.event.ValueChangeEvent;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.session.Preference;
import com.njdaeger.enhanceddebugstick.shifter.Shifter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.stream.Collectors;

public final class DebugListener implements Listener {

    private final EnhancedDebugStick plugin;

    DebugListener(EnhancedDebugStick plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        if (session == null) plugin.addDebugSession(event.getPlayer().getUniqueId());
        else session.resume();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        if (session != null) {
            session.pause();
            if (session.isSelectingMode()) session.setSelectingMode(false);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        DebugSession session = plugin.getDebugSession(player.getUniqueId());
        ShiftMode mode = session.getPref(Preference.SHIFT_MODE);
        Shifter shifter = mode.getShifter();
        if ((mode == ShiftMode.HOLD || mode == ShiftMode.DOUBLE) && shifter.canShift(session, event)) shifter.runShift(session, event);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        ShiftMode mode = session.getPref(Preference.SHIFT_MODE);
        Shifter shifter = mode.getShifter();
        if (mode == ShiftMode.HOLD || mode == ShiftMode.DOUBLE) {
            if (shifter.canEnable(session, event)) {
                shifter.runEnable(session, event);
                return;
            }
            if (shifter.canDisable(session, event)) shifter.runDisable(session, event);
        }
    }
    
    @EventHandler
    public void testCopyEvent(CopyPropertyEvent event) {
        System.out.println(event.getCopiedBlock().getBlockData());
        System.out.println(event.getCopiedProperties().stream().map(IProperty::getNiceName).collect(Collectors.toList()));
        System.out.println(event.getCopyLocation());
    }
    
    @EventHandler
    public void testPasteEvent(PastePropertyEvent event) {
        System.out.println(event.getBefore().getBlockData());
        System.out.println(event.getAfter());
    }
    
    @EventHandler
    public void testFreezeEvent(FreezeBlockEvent event) {
        System.out.println(event.getFrozenBlock().getBlockData());
    }
    
    @EventHandler
    public void testUnfreezeEvent(UnfreezeBlockEvent event) {
        System.out.println(event.getUnfrozenBlocks());
    }
    
    @EventHandler
    public void testPropertyEvent(PropertyChangeEvent event) {
        System.out.println(event.getLastProperty().getNiceName());
        System.out.println(event.getNextProperty().getNiceName());
        System.out.println(event.getLocation());
    }
    
    @EventHandler
    public void testValueEvent(ValueChangeEvent event) {
        System.out.println(event.getBlockBefore().getBlockData());
        System.out.println(event.getBlockDataAfter());
        System.out.println(event.getLocation());
    }
    
}
