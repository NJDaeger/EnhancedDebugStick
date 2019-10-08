package com.njdaeger.enhanceddebugstick.shifter;

import com.njdaeger.enhanceddebugstick.DebugSession;
import org.bukkit.event.player.PlayerEvent;

/**
 * Represents a mode shifter
 */
public interface Shifter<ON_SHIFT extends PlayerEvent, ON_START_FINISH extends PlayerEvent> {

    /**
     * What to run when enabling the shifter
     *
     * @param session The debug session which is starting the mode shifter
     * @param event The event which is calling this method
     */
    void runEnable(DebugSession session, ON_START_FINISH event);

    /**
     * Check if the session can/is enabling the shifter
     *
     * @param session The debug session to check
     * @param event The event which is calling this method
     * @return True if the shifter can be enabled, false otherwise
     */
    boolean canEnable(DebugSession session, ON_START_FINISH event);

    /**
     * What to run when disabling the shifter
     *
     * @param session The debug session which is closing the mode shifter
     * @param event The event which is calling this method
     */
    void runDisable(DebugSession session, ON_START_FINISH event);

    /**
     * Check if the session can/is disabling the shifter
     *
     * @param session The debug session to check
     * @param event The event which is calling this method
     * @return True if the shifter can be disabled, false otherwise
     */
    boolean canDisable(DebugSession session, ON_START_FINISH event);

    /**
     * What to run when shifting
     *
     * @param session The debug session which is shifting
     * @param event The event which is calling this method
     */
    void runShift(DebugSession session, ON_SHIFT event);

    /**
     * Check if the session can be shifting
     *
     * @param session The debug session to check
     * @param event The event which is calling this method
     * @return True if the session can shift, false otherwise
     */
    boolean canShift(DebugSession session, ON_SHIFT event);
}
