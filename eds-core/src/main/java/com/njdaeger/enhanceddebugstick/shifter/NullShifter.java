package com.njdaeger.enhanceddebugstick.shifter;

import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import org.bukkit.event.player.PlayerEvent;

public class NullShifter implements Shifter<PlayerEvent, PlayerEvent> {

    @Override
    public void runEnable(IDebugSession session, PlayerEvent event) {
    }

    @Override
    public boolean canEnable(IDebugSession session, PlayerEvent event) {
        return false;
    }

    @Override
    public void runDisable(IDebugSession session, PlayerEvent event) {
    }

    @Override
    public boolean canDisable(IDebugSession session, PlayerEvent event) {
        return false;
    }

    @Override
    public void runShift(IDebugSession session, PlayerEvent event) {
    }

    @Override
    public boolean canShift(IDebugSession session, PlayerEvent event) {
        return false;
    }
}
