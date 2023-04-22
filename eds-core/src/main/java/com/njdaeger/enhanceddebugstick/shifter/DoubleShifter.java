package com.njdaeger.enhanceddebugstick.shifter;

import com.njdaeger.enhanceddebugstick.api.config.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.session.DefaultPreferences;
import com.njdaeger.enhanceddebugstick.util.BossBarTimer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class DoubleShifter implements Shifter<PlayerInteractEvent, PlayerToggleSneakEvent> {

    @Override
    public void runEnable(IDebugSession iSession, PlayerToggleSneakEvent event) {
        var session = (DebugSession)iSession; //only have one implementation of IDebugSession, just cast until its a problem
        long timeout = session.getPreference(DefaultPreferences.SNEAK_TIMEOUT);
        if (session.getSelectingStart() == 0) {
            session.setSelectingStart(System.currentTimeMillis());
            if (ConfigKey.get().ALLOW_BOSSBAR_TIMERS) BossBarTimer.create(event.getPlayer(), false, timeout, 2,
                    (timer) -> ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Timeout: " + (((timer.getStartTime()+timer.getTotalTime()) - System.currentTimeMillis())/1000.), (p) -> session.isSelectingMode(), () -> {
                        if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    }).start();
        }
        else {
            if ((System.currentTimeMillis() - session.getSelectingStart()) > timeout) {
                session.setSelectingStart(0);
                runEnable(session, event);
            }
            else {
                if (ConfigKey.get().MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10);
                session.setSelectingMode(true);
                session.pause();
            }
        }
    }

    @Override
    public boolean canEnable(IDebugSession iSession, PlayerToggleSneakEvent event) {
        var session = (DebugSession)iSession; //only have one implementation of IDebugSession, just cast until its a problem
        return session.isHoldingDebugStick() && !session.isSelectingMode() && event.isSneaking() && ((System.currentTimeMillis() - session.getLastStop()) > ConfigKey.get().MS_CHANGE_COOLDOWN);
    }

    @Override
    public void runDisable(IDebugSession iSession, PlayerToggleSneakEvent event) {
        var session = (DebugSession)iSession; //only have one implementation of IDebugSession, just cast until its a problem
        long timeout = session.getPreference(DefaultPreferences.SNEAK_TIMEOUT);
        if (session.getSelectingStart() == 0) {
            session.setSelectingStart(System.currentTimeMillis());
            if (ConfigKey.get().ALLOW_BOSSBAR_TIMERS) BossBarTimer.create(event.getPlayer(), false, timeout, 2,
                    (timer) -> ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Timeout: " + (((timer.getStartTime()+timer.getTotalTime()) - System.currentTimeMillis())/1000.), (p) -> !session.isSelectingMode(), () -> {
                        if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    }).start();
        }
        else {
            if ((System.currentTimeMillis() - session.getSelectingStart()) > ConfigKey.get().DOUBLE_SNEAK_TIMEOUT) {
                session.setSelectingStart(0);
                runDisable(session, event);
            }
            else {
                if (ConfigKey.get().MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING);
                session.setSelectingMode(false);
                session.resume();
                if (ConfigKey.get().ALLOW_BOSSBAR_TIMERS) BossBarTimer.create(event.getPlayer(), false, session.getPreference(DefaultPreferences.CHANGE_COOLDOWN), 2,
                        (timer) -> ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Cooldown: " + (((timer.getStartTime()+timer.getTotalTime()) - System.currentTimeMillis())/1000.)).start();
            }
        }
    }

    @Override
    public boolean canDisable(IDebugSession iSession, PlayerToggleSneakEvent event) {
        var session = (DebugSession)iSession; //only have one implementation of IDebugSession, just cast until its a problem
        return session.isHoldingDebugStick() && !event.isSneaking() && session.isSelectingMode() && (System.currentTimeMillis() - session.getLastStart()) > 1000;
    }

    @Override
    public void runShift(IDebugSession iSession, PlayerInteractEvent event) {
        var session = (DebugSession)iSession; //only have one implementation of IDebugSession, just cast until its a problem
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        int size = DebugModeType.getDebugModes().size();
        int index = DebugModeType.getDebugModes().indexOf(session.getDebugMode());

        if (ConfigKey.get().MS_MODE_CHANGE) session.sendSound(Sound.UI_BUTTON_CLICK);

        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                session.setDebugMode(index - 1 < 0 ? DebugModeType.getDebugModes().get(size - 1) : DebugModeType.getDebugModes().get(index - 1));
                session.getDebugMode().pauseSession(session);
                session.getSelectingTask().run();
                break;
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                session.setDebugMode(index + 1 == size ? DebugModeType.getDebugModes().get(0) : DebugModeType.getDebugModes().get(index + 1));
                session.getDebugMode().pauseSession(session);
                session.getSelectingTask().run();
                break;
        }
    }

    @Override
    public boolean canShift(IDebugSession session, PlayerInteractEvent event) {
        return session.isHoldingDebugStick() && session.isSelectingMode();
    }
}
