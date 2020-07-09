package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.session.DebugSession;

import java.util.UUID;

/**
 * A debug context is a session which stores information about a player per debug mode. For example, when a player first
 * joins the world they are automatically set on the "Classic" debug mode- so they are given a ClassicDebugContext to
 * store information about that given debug mode. Other contexts include FreezeDebugContext and CopyDebugContext
 */
public interface DebugContext {

    /**
     * Gets the owning session ID
     *
     * @return The owning session ID
     */
    UUID getOwner();

    /**
     * Gets the owning debug session
     *
     * @return The owning debug session.
     */
    DebugSession getDebugSession();

}
