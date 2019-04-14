package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.DebugContext;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.DebugStickAPI;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class DebugSession {

    private DebugModeType debugMode;
    private final UUID uuid;

    DebugSession(UUID uuid) {
        this.uuid = uuid;
        setDebugMode(DebugModeType.CLASSIC);
    }

    /**
     * Get the current debug mode in use
     *
     * @return The current debug mode in use
     */
    public DebugModeType<?, ?> getDebugMode() {
        return debugMode;
    }

    /**
     * Check if the current debug matches the given debug mode
     *
     * @param type The mode to check
     * @return True if the current debug mode matches the given, false otherwise.
     */
    public boolean isDebugMode(DebugModeType type) {
        return debugMode.equals(type);
    }

    /**
     * Sets the current debug mode in use.
     *
     * @param type The type of debug mode to use. Cannot be null.
     */
    public void setDebugMode(DebugModeType type) {
        Validate.notNull(type, "New DebugModeType cannot be null");
        this.debugMode = type;
        type.addSession(this);
    }

    /**
     * Quick check to see if this session is currently holding a debug stick. This will always return false if the
     * player is offline.
     *
     * @return The debug stick.
     */
    public boolean isHoldingDebugStick() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        else
            return DebugStickAPI.hasDebugStick(player) && DebugStickAPI.DEBUG_STICK.equals(player.getInventory().getItemInMainHand());
    }

    /**
     * Gets this session as a debug context for the specified debug mode.
     *
     * @param debugMode The debug mode type this session needs to be wrapped into
     * @param <C> The type of context the debug mode takes
     * @return The Debug Context of the specified debug mode type, or null if the type specified does not have this
     *         session in it yet.
     */
    public <C extends DebugContext> C toDebugContext(DebugModeType<?, C> debugMode) {
        if (debugMode.hasSession(uuid)) return debugMode.getDebugContext(uuid);
        else return null;
    }

    /**
     * The UUID of this session. (Corresponds to the Player who owns this session)
     *
     * @return The session ID
     */
    public UUID getSessionId() {
        return uuid;
    }

}
