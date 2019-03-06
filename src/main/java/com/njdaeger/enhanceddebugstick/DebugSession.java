package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import org.apache.commons.lang.Validate;

import java.util.UUID;

public final class DebugSession {

    private DebugModeType debugMode;
    private final UUID uuid;

    DebugSession(UUID uuid) {
        setDebugMode(DebugModeType.CLASSIC);
        this.uuid = uuid;
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
     * The UUID of this session. (Corresponds to the Player who owns this session)
     *
     * @return The session ID
     */
    public UUID getSessionId() {
        return uuid;
    }

}
