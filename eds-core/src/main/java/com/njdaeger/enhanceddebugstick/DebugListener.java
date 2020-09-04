package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.ShiftMode;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.session.Preference;
import com.njdaeger.enhanceddebugstick.shifter.Shifter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

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
        if ((mode == ShiftMode.HOLD || mode == ShiftMode.DOUBLE) && shifter.canShift(session, event) && session.canInteract()) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            shifter.runShift(session, event);
        }
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
}
