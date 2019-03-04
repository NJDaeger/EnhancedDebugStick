package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.Configuration;
import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.DebugStick;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public interface DebugStickAPI {

    /**
     * The debug stick ItemStack
     */
    DebugStick DEBUG_STICK = new DebugStick();

    /**
     * Get a debug session
     *
     * @param uuid The UUID of the user to get the debug session of.
     * @return The DebugSession, or null if the user has not joined since the start of the server.
     */
    DebugSession getDebugSession(UUID uuid);

    /**
     * Add a debug session. Sessions that are added are not removed automatically. They will only be removed when the
     * server restarts/reloads or when the {@link #removeDebugSession(UUID)} method is called.
     *
     * @param uuid The UUID of the user to add the debug session of.
     * @return True if the debug session was added, false if the session already exists
     */
    boolean addDebugSession(UUID uuid);

    /**
     * Remove a debug session.
     *
     * @param uuid The UUID of the user to remove the debug session of
     * @return True of the debug session was removed, false if the session doesnt exist
     */
    boolean removeDebugSession(UUID uuid);

    /**
     * Checks if the specific UUID has a debug session.
     * @param uuid The UUID of the user to check exists.
     * @return True if the debug session exists for the corresponding UUID, false otherwise.
     */
    boolean hasDebugSession(UUID uuid);

    /**
     * Gets a collection of all the debug sessions.
     * @return A collection of all debug sessions.
     */
    Collection<DebugSession> getDebugSessions();

    /**
     * Gets the EnhancedDebugStick configuration.
     * @return The EnhancedDebugStickConfiguration.
     */
    Configuration getDebugConfig();

    /**
     * Check whether a player has an enhanced debug stick or not in their inventory
     * @param player The player whos inventory needs checked
     * @return True if the player has an Enhanced Debug Stick in their inventory
     */
    static boolean hasDebugStick(Player player) {
        return player.getInventory().contains(DEBUG_STICK);
    }

    /**
     * Gets the current API version
     * @return The current API version
     */
    static int getApiVersion() {
        return 1;
    }

}
