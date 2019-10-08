package com.njdaeger.enhanceddebugstick.shifter;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.DebugSession;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class HoldShifter extends DoubleShifter {
    @Override
    public void runEnable(DebugSession session, PlayerToggleSneakEvent event) {
        //Since the selecting start is 0, we want to set it to the current time. Then when the user lets go of the shift
        //key, we check to see if the unshift time minus the shift time is > the min and less than the max hold shift time
        if (session.getSelectingStart() == 0) session.setSelectingStart(System.currentTimeMillis());
        else {
            if (session.getSelectingStart() == -1) session.setSelectingStart(0);
            if (ConfigKey.MS_START_STOP_SOUND) session.sendSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10);
            session.setSelectingMode(true);
            session.pause();
        }
    }

    @Override
    public boolean canEnable(DebugSession session, PlayerToggleSneakEvent event) {
        return session.isHoldingDebugStick() && !session.isSelectingMode() && event.isSneaking();
    }

    @Override
    public void runDisable(DebugSession session, PlayerToggleSneakEvent event) {
        long millis = (System.currentTimeMillis() - session.getSelectingStart());
        if (session.getSelectingStart() != 0 && ConfigKey.HOLD_SNEAK_MAXIMUM < millis && millis > ConfigKey.HOLD_SNEAK_MINIMUM) {
            session.setSelectingStart(-1);
            runEnable(session, event);
        } else {
            session.setSelectingStart(0);
            session.setSelectingMode(false);
            session.resume();
        }

    }

    @Override
    public boolean canDisable(DebugSession session, PlayerToggleSneakEvent event) {
        return session.isHoldingDebugStick() && session.isSelectingMode() && !event.isSneaking();
    }
}
