package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.mode.ShiftMode;
import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.session.DefaultPreferences;
import com.njdaeger.enhanceddebugstick.shifter.Shifter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class DebugListener implements Listener {

    private final EnhancedDebugStick plugin;

    DebugListener(EnhancedDebugStick plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        IDebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        if (session == null) plugin.addDebugSession(event.getPlayer().getUniqueId());
        else session.resume();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        IDebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        if (session != null) {
            session.pause();
            if (session.isSelectingMode()) session.setSelectingMode(false);
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        IDebugSession session = plugin.getDebugSession(player.getUniqueId());
        if (session == null) return;
        ShiftMode mode = session.getPreference(DefaultPreferences.SHIFT_MODE);
        Shifter shifter = Shifter.fromId(mode.getShifterId());
        if ((mode == ShiftMode.HOLD || mode == ShiftMode.DOUBLE) && shifter.canShift(session, event) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            shifter.runShift(session, event);
        }
    }
    
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        IDebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        if (session == null) return;
        ShiftMode mode = session.getPreference(DefaultPreferences.SHIFT_MODE);
        Shifter shifter = Shifter.fromId(mode.getShifterId());
        if (mode == ShiftMode.HOLD || mode == ShiftMode.DOUBLE) {
            if (shifter.canEnable(session, event)) {
                shifter.runEnable(session, event);
                return;
            }
            if (shifter.canDisable(session, event)) shifter.runDisable(session, event);
        }
    }
}
