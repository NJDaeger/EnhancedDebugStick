package com.njdaeger.enhanceddebugstick.session;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.api.config.ConfigKey;
import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import com.njdaeger.enhanceddebugstick.api.mode.IDebugContext;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import com.njdaeger.enhanceddebugstick.api.session.Preference;
import com.njdaeger.enhanceddebugstick.modes.classic.ClassicDebugMode;
import com.njdaeger.pdk.types.ParsedType;
import com.njdaeger.pdk.utils.ActionBar;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The debug session is meant to store information about the user for things like enabling the mode shifter and storing
 * the type of mode currently selected.
 */
public final class DebugSession implements IDebugSession {

    //These are used for the mode changes.
    private long lastStart;
    private long lastStop;
    private int taskNumber;
    private long sneakStart;
    private boolean selecting;

    private long lastForced;
    private final UUID uuid;
    private DebugModeType debugMode;
    private PreferenceTrack prefs;

    public DebugSession(UUID uuid, EnhancedDebugStickApi plugin) {
        this.uuid = uuid;
        if (ConfigKey.get().ENABLE_PREFERENCES) this.prefs = new PreferenceTrack(uuid, plugin);
        setDebugMode(new ClassicDebugMode(plugin));
    }

    /**
     * Get a user preference from a user
     *
     * @param preference The preference to get
     * @param <T> The data type of the preference
     * @return The preference from the user, or the default config value if preferences arent enabled.
     */
    public <T, P extends ParsedType<T>> T getPreference(Preference<T, P> preference) {
        return prefs == null ? preference.getDefault() : prefs.get(preference);
    }

    public <T, P extends ParsedType<T>> void setPreference(Preference<T, P> preference, T value) {
        if (prefs != null) prefs.set(preference, value == null ? preference.getDefault() : value);
    }

    /**
     * Checks if this session has an online player attached to it
     *
     * @return True if the player who owns this session is online, false otherwise.
     */
    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
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
     * Get the current debug mode in use
     *
     * @return The current debug mode in use
     */
    public DebugModeType<?, ?> getDebugMode() {
        return debugMode;
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
            return EnhancedDebugStickApi.hasDebugStick(player) && EnhancedDebugStickApi.DEBUG_STICK.equals(player.getInventory().getItemInMainHand());
        }
    }

    /**
     * Gets this session as a debug context for the specified debug mode.
     *
     * @param debugMode The debug mode type this session needs to be wrapped into
     * @param <C> The type of context the debug mode takes
     * @return The Debug Context of the specified debug mode type, or null if the type specified does not have this
     *         session in it yet.
     * @throws RuntimeException if the session could not be bound to a debug mode type.
     */
    public <C extends IDebugContext> C toDebugContext(DebugModeType<?, C> debugMode) {
        if (debugMode.hasSession(uuid)) return debugMode.getDebugContext(uuid);
        else {
            if (isOnline()) Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "Cannot bind DebugSession to DebugContext.");
            throw new RuntimeException("Cannot bind DebugSession to " + debugMode.getNiceName() + "'s DebugContext. Please create a ticket and send your latest.log to https://github.com/NJDaeger/Bug-Reports! Sorry for this issue.");
        }
    }

    /**
     * Quick check to see if this session has permission to do use the specified debug mode
     *
     * @param debugMode The debug mode to check
     * @return True if the player has permission, false otherwise.
     */
    public boolean hasPermission(DebugModeType<?, ?> debugMode) {
        return Bukkit.getPlayer(uuid).hasPermission(debugMode.getBasePermission());
    }

    /**
     * Quick check to see if this session is currently using the debug stick with the given mode
     *
     * @param debugMode The mode to check if the session is using
     * @return True if the user is unpaused, in the given mode, and is holding the debug stick.
     */
    public boolean isUsing(DebugModeType<?, ?> debugMode) {
        return debugMode.hasSession(uuid) && !debugMode.isPaused(this) && isHoldingDebugStick() && isDebugMode(debugMode);
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

    /**
     * Sends an action bar to this player.
     *
     * @param message The message to send.
     */
    public void sendBar(String message) {
        if (isOnline() && (System.currentTimeMillis() - lastForced) > 3000)
            ActionBar.of(message).sendTo(Bukkit.getPlayer(uuid));
    }

    /**
     * Sends a forced actionbar to this player. A forced action bar is meant to make the action bar stay on the screen
     * for at least 3 seconds before any other action bar can be sent by this plugin.
     *
     * @param message The message to send.
     */
    public void sendForcedBar(String message) {
        if (isOnline() && (System.currentTimeMillis() - lastForced) > 3000) {
            this.lastForced = System.currentTimeMillis();
            ActionBar.of(message).sendTo(Bukkit.getPlayer(uuid));
        }
    }

    /**
     * Sends a plugin message to this player
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        if (isOnline())
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + message);
    }

    /**
     * Sends a sound to this player
     *
     * @param sound The sound to send
     */
    public void sendSound(Sound sound) {
        sendSound(sound, 1);
    }

    /**
     * Sends a sound to this player
     *
     * @param sound The sound to send
     * @param pitch The pitch of the sound
     */
    public void sendSound(Sound sound, float pitch) {
        if (isOnline()) {
            Player player = Bukkit.getPlayer(uuid);
            player.playSound(player.getLocation(), sound, 1, pitch);
        }
    }

    public boolean isSelectingMode() {
        return selecting;
    }

    public void setSelectingMode(boolean enabled) {
        this.sneakStart = 0;
        this.selecting = enabled;
        if (enabled) {
            this.lastStart = System.currentTimeMillis();
            this.taskNumber = Bukkit.getScheduler().scheduleSyncRepeatingTask(EnhancedDebugStick.getPlugin(EnhancedDebugStick.class), getSelectingTask(), 0, 20);
        } else {
            this.lastStop = System.currentTimeMillis();
            Bukkit.getScheduler().cancelTask(taskNumber);
            this.taskNumber = 0;
            ActionBar.of("").sendTo(Bukkit.getPlayer(uuid));
        }
    }

    public long getLastStop() {
        return lastStop;
    }

    public long getLastStart() {
        return lastStart;
    }

    public long getSelectingStart() {
        return sneakStart;
    }

    public void setSelectingStart(long start) {
        this.sneakStart = start;
    }

    public Runnable getSelectingTask() {
        return () -> {
            Player player = Bukkit.getPlayer(uuid);
            StringBuilder builder = new StringBuilder();
            DebugModeType.getDebugModes().forEach(type -> {
                if (isDebugMode(type))
                    builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD).append(ChatColor.UNDERLINE).append(type.getNiceName()).append(ChatColor.RESET);
                else builder.append(ChatColor.DARK_GREEN).append(type.getNiceName()).append(ChatColor.RESET);
                builder.append("    ");
            });
            ActionBar.of(builder.toString().trim()).sendTo(player);
        };
    }

}
