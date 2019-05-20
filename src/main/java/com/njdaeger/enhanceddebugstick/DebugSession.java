package com.njdaeger.enhanceddebugstick;

import com.njdaeger.btu.ActionBar;
import com.njdaeger.enhanceddebugstick.api.DebugContext;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.DebugStickAPI;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

public final class DebugSession {

    private long lastStop;
    private int taskNumber;
    private boolean selecting;
    private long sneakStart;
    private DebugModeType debugMode;
    private final UUID uuid;

    DebugSession(UUID uuid) {
        this.uuid = uuid;
        setDebugMode(DebugModeType.CLASSIC);
    }

    /**
     * Checks if this session has an online player attached to it
     * @return True if the player who owns this session is online, false otherwise.
     */
    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
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
        if (debugMode != null) debugMode.pauseSession(this);
        this.debugMode = type;
        if (!type.hasSession(uuid)) type.addSession(this);
        else type.resumeSession(this);
    }

    /**
     * Quick check to see if this session is currently holding a debug stick. This will always return false if the
     * player is offline.
     *
     * @return The debug stick.
     */
    public boolean isHoldingDebugStick() {
        if (!isOnline()) return false;
        else {
            Player player = Bukkit.getPlayer(uuid);
            return DebugStickAPI.hasDebugStick(player) && DebugStickAPI.DEBUG_STICK.equals(player.getInventory().getItemInMainHand());
        }
    }

    /**
     * Gets this session as a debug context for the specified debug mode.
     *
     * @param debugMode The debug mode type this session needs to be wrapped into
     * @param <C> The type of context the debug mode takes
     * @return The Debug Context of the specified debug mode type, or null if the type specified does not have this
     *         session in it yet.
     *
     * @throws RuntimeException if the session could not be bound to a debug mode type.
     */
    public <C extends DebugContext> C toDebugContext(DebugModeType<?, C> debugMode) {
        if (debugMode.hasSession(uuid)) return debugMode.getDebugContext(uuid);
        else {
            if (isOnline()) Bukkit.getPlayer(uuid).sendMessage(RED + "Cannot bind DebugSession to DebugContext.");
            throw new RuntimeException("Cannot bind DebugSession to " + debugMode.getNiceName() + "'s DebugContext. Please create a ticket and send your latest.log to https://github.com/NJDaeger/Bug-Reports! Sorry for this issue.");
        }
    }

    /**
     * The UUID of this session. (Corresponds to the Player who owns this session)
     *
     * @return The session ID
     */
    public UUID getSessionId() {
        return uuid;
    }

    /**
     * Resumes the previously paused session
     */
    public void resume() {
        if (debugMode != null) debugMode.resumeSession(this);
    }

    /**
     * Pauses the currently resumed session
     */
    public void pause() {
        if (debugMode != null) debugMode.pauseSession(this);
    }

    boolean isSelectingMode() {
        return selecting;
    }

    void setSelectingMode(boolean enabled) {
        this.sneakStart = 0;
        this.selecting = enabled;
        if (enabled) {
            this.taskNumber = Bukkit.getScheduler().scheduleSyncRepeatingTask(EnhancedDebugStick.getPlugin(EnhancedDebugStick.class), getSelectingTask(), 0, 20);
        } else {
            this.lastStop = System.currentTimeMillis();
            Bukkit.getScheduler().cancelTask(taskNumber);
            this.taskNumber = 0;
            ActionBar.of("").sendTo(Bukkit.getPlayer(uuid));
        }
    }

    long getLastStop() {
        return lastStop;
    }

    long getSelectingStart() {
        return sneakStart;
    }

    void setSelectingStart(long start) {
        this.sneakStart = start;
    }

    Runnable getSelectingTask() {
        return () -> {
                Player player = Bukkit.getPlayer(uuid);
                StringBuilder builder = new StringBuilder();
                DebugModeType.getDebugModes().forEach(type -> {
                    if (isDebugMode(type)) builder.append(DARK_GREEN).append(BOLD).append(UNDERLINE).append(type.getNiceName()).append(RESET);
                    else builder.append(DARK_GREEN).append(type.getNiceName()).append(RESET);
                    builder.append("    ");
                });
                ActionBar.of(builder.toString().trim()).sendTo(player);
            };
    }

}
