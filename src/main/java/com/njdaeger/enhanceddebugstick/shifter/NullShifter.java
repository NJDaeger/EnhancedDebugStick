package com.njdaeger.enhanceddebugstick.shifter;

import com.njdaeger.enhanceddebugstick.DebugSession;
import org.bukkit.event.player.PlayerEvent;

public class NullShifter implements Shifter<PlayerEvent, PlayerEvent> {

    @Override
    public void runEnable(DebugSession session, PlayerEvent event) {
    }

    @Override
    public boolean canEnable(DebugSession session, PlayerEvent event) {
        return false;
    }

    @Override
    public void runDisable(DebugSession session, PlayerEvent event) {
    }

    @Override
    public boolean canDisable(DebugSession session, PlayerEvent event) {
        return false;
    }

    @Override
    public void runShift(DebugSession session, PlayerEvent event) {
    }

    @Override
    public boolean canShift(DebugSession session, PlayerEvent event) {
        return false;
    }
}
