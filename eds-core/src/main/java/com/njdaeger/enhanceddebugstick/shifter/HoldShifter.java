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

public class HoldShifter implements Shifter<PlayerInteractEvent, PlayerToggleSneakEvent> {

    @Override
    public void runEnable(IDebugSession iSession, PlayerToggleSneakEvent event) {
        //Since the selecting start is 0, we want to set it to the current time. Then when the user lets go of the shift
        //key, we check to see if the unshift time minus the shift time is > the min and less than the max hold shift time

        var session = (DebugSession) iSession;
        
        if (session.getSelectingStart() == 0 && !session.getDebugMode().isPaused(session)) {
            session.setSelectingStart(System.currentTimeMillis());
            long max = session.getPreference(DefaultPreferences.SNEAK_MAXIMUM);
            if (ConfigKey.get().ALLOW_BOSSBAR_TIMERS) BossBarTimer.create(event.getPlayer(), false, session.getPreference(DefaultPreferences.SNEAK_MINIMUM), 2,
                    (timer) -> ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Hold Time: " + (((timer.getStartTime()+timer.getTotalTime()) - System.currentTimeMillis())/1000.),
                    (p) -> !p.isSneaking(), max <= 0 ? null :
                    () -> BossBarTimer.create(event.getPlayer(), true, max, 2,
                            (timer) -> ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Time Remaining: " + (((timer.getStartTime()+timer.getTotalTime()) - System.currentTimeMillis())/1000.),
                            (p) -> !p.isSneaking(), () -> {
                                if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                            }).start()).start();
        } else {
            if (session.getSelectingStart() == -1) session.setSelectingStart(0);
            if (ConfigKey.get().MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10);
            session.setSelectingMode(true);
            session.pause();
        }
    }

    @Override
    public boolean canEnable(IDebugSession iSession, PlayerToggleSneakEvent event) {
        var session = (DebugSession) iSession;
        return session.isHoldingDebugStick() && !session.isSelectingMode() && !event.getPlayer().isSneaking() && session.getSelectingStart() == 0;
    }

    @Override
    public void runDisable(IDebugSession iSession, PlayerToggleSneakEvent event) {
        var session = (DebugSession) iSession;
        long millis = (System.currentTimeMillis() - session.getSelectingStart());
        long min = session.getPreference(DefaultPreferences.SNEAK_MINIMUM);
        long max = session.getPreference(DefaultPreferences.SNEAK_MAXIMUM);//We need to offset for the amount of time which it was held for
        if (!session.isSelectingMode() && millis > min && (max <= 0 || millis < max + min)) {
            session.setSelectingStart(-1);
            runEnable(session, event);
        } else {
            session.setSelectingStart(0);
            session.setSelectingMode(false);
            session.resume();
        }

    }

    @Override
    public boolean canDisable(IDebugSession session, PlayerToggleSneakEvent event) {
        return session.isHoldingDebugStick() && event.getPlayer().isSneaking();
    }

    @Override
    public void runShift(IDebugSession iSession, PlayerInteractEvent event) {
        var session = (DebugSession) iSession;
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
