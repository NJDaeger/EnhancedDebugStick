package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.DebugSession;

import java.util.UUID;

public interface DebugContext {

    /**
     * Gets the owning session ID
     * @return The owning session ID
     */
    UUID getOwner();

    /**
     * Gets the owning debug session
     * @return The owning debug session.
     */
    DebugSession getDebugSession();

}
