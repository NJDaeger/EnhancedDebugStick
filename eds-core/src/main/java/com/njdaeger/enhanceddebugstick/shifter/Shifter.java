package com.njdaeger.enhanceddebugstick.shifter;

import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import org.bukkit.event.player.PlayerEvent;

/**
 * Represents a mode shifter
 */
public interface Shifter<ON_SHIFT extends PlayerEvent, ON_START_FINISH extends PlayerEvent> {
    
    /**
     * Get the shifter instance by id
     *
     * @param id The id of the shifter
     * @return The shifter instance
     */
    static Shifter<?, ?> fromId(String id) {
        return switch (id) {
            case "null" -> new NullShifter();
            case "double" -> new DoubleShifter();
            case "hold" -> new HoldShifter();
            default -> null;
        };
    }
    
    /**
     * What to run when enabling the shifter
     *
     * @param session The debug session which is starting the mode shifter
     * @param event   The event which is calling this method
     */
    void runEnable(IDebugSession session, ON_START_FINISH event);
    
    /**
     * Check if the session can/is enabling the shifter
     *
     * @param session The debug session to check
     * @param event   The event which is calling this method
     * @return True if the shifter can be enabled, false otherwise
     */
    boolean canEnable(IDebugSession session, ON_START_FINISH event);
    
    /**
     * What to run when disabling the shifter
     *
     * @param session The debug session which is closing the mode shifter
     * @param event   The event which is calling this method
     */
    void runDisable(IDebugSession session, ON_START_FINISH event);
    
    /**
     * Check if the session can/is disabling the shifter
     *
     * @param session The debug session to check
     * @param event   The event which is calling this method
     * @return True if the shifter can be disabled, false otherwise
     */
    boolean canDisable(IDebugSession session, ON_START_FINISH event);
    
    /**
     * What to run when shifting
     *
     * @param session The debug session which is shifting
     * @param event   The event which is calling this method
     */
    void runShift(IDebugSession session, ON_SHIFT event);
    
    /**
     * Check if the session can be shifting
     *
     * @param session The debug session to check
     * @param event   The event which is calling this method
     * @return True if the session can shift, false otherwise
     */
    boolean canShift(IDebugSession session, ON_SHIFT event);
}
