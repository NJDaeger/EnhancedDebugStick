package com.njdaeger.enhanceddebugstick.api.session;

import com.njdaeger.enhanceddebugstick.api.mode.IDebugContext;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.pdk.types.ParsedType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.UUID;

/**
 * Represents a debug session.
 */
public interface IDebugSession {
    
    /**
     * Get a user preference.
     *
     * @param preference The preference to get.
     * @param <T>        The type of the preference.
     * @param <P>        The parsed type of the preference.
     * @return The value of the preference. Or the default value if preferences are not enabled.
     */
    <T, P extends ParsedType<T>> T getPreference(Preference<T, P> preference);
    
    /**
     * Set a user preference.
     *
     * @param preference The preference to set.
     * @param value      The value to set the preference to.
     * @param <T>        The type of the preference.
     * @param <P>        The parsed type of the preference.
     */
    <T, P extends ParsedType<T>> void setPreference(Preference<T, P> preference, T value);
    
    /**
     * The UUID of this session. (Corresponds to the Player who owns this session)
     *
     * @return The session ID
     */
    UUID getSessionId();
    
    /**
     * Check if this session has an online player attached to it.
     * @return True if the player who owns this session is online, false otherwise.
     */
    default boolean isOnline() {
        return Bukkit.getPlayer(getSessionId()) != null;
    }
    
    /**
     * Check if the current debug matches the given debug mode
     *
     * @param type The mode to check
     * @return True if the current debug mode matches the given, false otherwise.
     */
    boolean isDebugMode(DebugModeType type);
    
    /**
     * Get the current debug mode in use
     *
     * @return The current debug mode in use
     */
    DebugModeType<?, ?> getDebugMode();
    
    /**
     * Set the current debug mode in use
     *
     * @param type The debug mode to set
     */
    void setDebugMode(DebugModeType<?, ?> type);
    
    /**
     * Check if the player is holding a debug stick
     *
     * @return True if the player is holding a debug stick, false otherwise.
     */
    boolean isHoldingDebugStick();
    
    /**
     * Gets this session as a debug context for the specified debug mode.
     *
     * @param debugMode The debug mode type this session needs to be wrapped into
     * @param <C> The type of context the debug mode takes
     * @return The Debug Context of the specified debug mode type, or null if the type specified does not have this
     *         session in it yet.
     * @throws RuntimeException if the session could not be bound to a debug mode type.
     */
    <C extends IDebugContext> C toDebugContext(DebugModeType<?, C> debugMode);
    
    /**
     * Quick check to see if this session has permission to do use the specified debug mode
     *
     * @param debugMode The debug mode to check
     * @return True if the player has permission, false otherwise.
     */
    boolean hasPermission(DebugModeType<?, ?> debugMode);
    
    /**
     * Quick check to see if this session is currently using the debug stick with the given mode
     *
     * @param debugMode The mode to check if the session is using
     * @return True if the user is unpaused, in the given mode, and is holding the debug stick.
     */
    boolean isUsing(DebugModeType<?, ?> debugMode);
    
    /**
     * Check if this session is currently selecting a debug mode
     * @return True if the user is selecting a debug mode, false otherwise.
     */
    boolean isSelectingMode();
    
    /**
     * Set if this session is currently selecting a debug mode
     * @param isSelectingMode True to enable the mode selector, false to disable it. NOTE: This will only work if the user's shift mode is ShiftMode.HOLD or ShiftMode.DOUBLE
     */
    void setSelectingMode(boolean isSelectingMode);
    
    /**
     * Resumes the currently selected debug mode
     */
    void resume();
    
    /**
     * Pauses the currently selected debug mode
     */
    void pause();
    
    /**
     * This will send an actionbar to the player
     * @param message The message to send to the player
     */
    void sendBar(String message);
    
    /**
     * This will send a forced actionbar to the player
     * @param message The message to send to the player
     */
    void sendForcedBar(String message);
    
    /**
     * This will send an [EDS] prefixed message to the player in chat
     * @param message The message to send to the player
     */
    void sendMessage(String message);
    
    /**
     * This will send a sound to the player
     * @param sound The sound to send to the player
     */
    void sendSound(Sound sound);
    
    /**
     * This will send a sound to the player
     * @param sound The sound to send to the player
     * @param pitch The pitch of the sound
     */
    void sendSound(Sound sound, float pitch);
    
}
