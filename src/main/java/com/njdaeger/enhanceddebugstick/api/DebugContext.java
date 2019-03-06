package com.njdaeger.enhanceddebugstick.api;

import java.util.UUID;

public interface DebugContext {

    /**
     * Gets the owning session ID
     * @return The owning session ID
     */
    UUID getOwner();

}
