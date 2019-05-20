package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import org.bukkit.Sound;
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
            if (session.isSelectingMode()) {
                session.setSelectingMode(false);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        DebugSession session = plugin.getDebugSession(player.getUniqueId());
        if (session.isHoldingDebugStick() && session.isSelectingMode()) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            int size = DebugModeType.getDebugModes().size();
            int index = DebugModeType.getDebugModes().indexOf(session.getDebugMode());

            if (ConfigKey.MS_MODE_CHANGE) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

            switch (event.getAction()) {
                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    session.setDebugMode(index + 1 == size ? DebugModeType.getDebugModes().get(0) : DebugModeType.getDebugModes().get(index + 1));
                    session.getDebugMode().pauseSession(session);
                    session.getSelectingTask().run();
                    break;
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    session.setDebugMode(index - 1 < 0 ? DebugModeType.getDebugModes().get(size - 1) : DebugModeType.getDebugModes().get(index - 1));
                    session.getDebugMode().pauseSession(session);
                    session.getSelectingTask().run();
                    break;
            }
            return;
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        if (session.isHoldingDebugStick() && !session.isSelectingMode() && event.isSneaking() && ((System.currentTimeMillis() - session.getLastStop()) > 1000)) {
            if (session.getSelectingStart() == 0) session.setSelectingStart(System.currentTimeMillis());
            else {
                if ((System.currentTimeMillis() - session.getSelectingStart()) > ConfigKey.MS_SNEAK_TIMEOUT) session.setSelectingStart(0);
                else {
                    if (ConfigKey.MS_START_STOP_SOUND) event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 10);
                    session.setSelectingMode(true);
                    session.pause();
                }
            }
        }
        if (session.isHoldingDebugStick() && !event.isSneaking() && session.isSelectingMode()) {
            if (session.getSelectingStart() == 0) session.setSelectingStart(System.currentTimeMillis());
            else {
                if ((System.currentTimeMillis() - session.getSelectingStart()) > ConfigKey.MS_SNEAK_TIMEOUT) session.setSelectingStart(0);
                else {
                    if (ConfigKey.MS_START_STOP_SOUND) event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    session.setSelectingMode(false);
                    session.resume();
                }
            }
        }
    }
}
