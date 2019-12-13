package com.njdaeger.enhanceddebugstick.shifter;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.util.BossBarTimer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.DARK_GRAY;

public class DoubleShifter implements Shifter<PlayerInteractEvent, PlayerToggleSneakEvent> {

    @Override
    public void runEnable(DebugSession session, PlayerToggleSneakEvent event) {
        if (session.getSelectingStart() == 0) {
            session.setSelectingStart(System.currentTimeMillis());
            BossBarTimer.create(event.getPlayer(), false, ConfigKey.DOUBLE_SNEAK_TIMEOUT, 2,
                    (timer) -> DARK_GRAY + "[" + BLUE + "EDS" + DARK_GRAY + "] " + ChatColor.GRAY + "Timeout: " + (((timer.getStartTime()+timer.getTotalTime()) - System.currentTimeMillis())/1000.), (p) -> session.isSelectingMode());
        }
        else {
            if ((System.currentTimeMillis() - session.getSelectingStart()) > ConfigKey.DOUBLE_SNEAK_TIMEOUT) session.setSelectingStart(0);
            else {
                if (ConfigKey.MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10);
                session.setSelectingMode(true);
                session.pause();
            }
        }
    }

    @Override
    public boolean canEnable(DebugSession session, PlayerToggleSneakEvent event) {
        return session.isHoldingDebugStick() && !session.isSelectingMode() && event.isSneaking() && ((System.currentTimeMillis() - session.getLastStop()) > ConfigKey.DOUBLE_CHANGE_COOLDOWN);
    }

    @Override
    public void runDisable(DebugSession session, PlayerToggleSneakEvent event) {
        if (session.getSelectingStart() == 0) {
            session.setSelectingStart(System.currentTimeMillis());
            BossBarTimer.create(event.getPlayer(), false, ConfigKey.DOUBLE_SNEAK_TIMEOUT, 2,
                    (timer) -> DARK_GRAY + "[" + BLUE + "EDS" + DARK_GRAY + "] " + ChatColor.GRAY + "Timeout: " + (((timer.getStartTime()+timer.getTotalTime()) - System.currentTimeMillis())/1000.), (p) -> !session.isSelectingMode());
        }
        else {
            if ((System.currentTimeMillis() - session.getSelectingStart()) > ConfigKey.DOUBLE_SNEAK_TIMEOUT) session.setSelectingStart(0);
            else {
                if (ConfigKey.MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING);
                session.setSelectingMode(false);
                session.resume();
                BossBarTimer.create(event.getPlayer(), false, ConfigKey.DOUBLE_CHANGE_COOLDOWN, 2,
                        (timer) -> DARK_GRAY + "[" + BLUE + "EDS" + DARK_GRAY + "] " + ChatColor.GRAY + "Cooldown: " + (((timer.getStartTime()+timer.getTotalTime()) - System.currentTimeMillis())/1000.));
            }
        }
    }

    @Override
    public boolean canDisable(DebugSession session, PlayerToggleSneakEvent event) {
        return session.isHoldingDebugStick() && !event.isSneaking() && session.isSelectingMode() && (System.currentTimeMillis() - session.getLastStart()) > 1000;
    }

    @Override
    public void runShift(DebugSession session, PlayerInteractEvent event) {
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        int size = DebugModeType.getDebugModes().size();
        int index = DebugModeType.getDebugModes().indexOf(session.getDebugMode());

        if (ConfigKey.MS_MODE_CHANGE) session.sendSound(Sound.UI_BUTTON_CLICK);

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
    }

    @Override
    public boolean canShift(DebugSession session, PlayerInteractEvent event) {
        return session.isHoldingDebugStick() && session.isSelectingMode();
    }
}
