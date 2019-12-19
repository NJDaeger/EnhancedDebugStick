package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import com.njdaeger.enhanceddebugstick.modes.classic.ClassicDebugMode;
import com.njdaeger.enhanceddebugstick.modes.copy.CopyDebugMode;
import com.njdaeger.enhanceddebugstick.modes.freeze.FreezeDebugMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.DEBUG_STICK;

public abstract class DebugModeType<T extends DebugModeType<T, C>, C extends DebugContext> implements Listener {

    private static final List<DebugModeType<?, ?>> modeTypes = new ArrayList<>();
    public static final ClassicDebugMode CLASSIC = new ClassicDebugMode();
    public static final FreezeDebugMode FREEZE = new FreezeDebugMode();
    public static final CopyDebugMode COPY = new CopyDebugMode();

    protected final EnhancedDebugStick plugin;
    protected final Map<UUID, C> contextTrack;
    protected final Set<UUID> paused;
    private final String niceName;
    private final Class<T> type;

    /**
     * Creates a new DebugModeType
     * @param niceName The nice name of this Debug Mode type
     * @param type The class type of this debug mode
     */
    public DebugModeType(String niceName, Class<T> type) {
        this.plugin = EnhancedDebugStick.getPlugin(EnhancedDebugStick.class);
        this.contextTrack = new HashMap<>();
        this.paused = new HashSet<>();
        this.niceName = niceName;
        this.type = type;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        modeTypes.add(this);
    }

    /**
     * Get a debug mode by name
     * @param name The name of the debug mode
     * @return The debug mode if found, or null if not found.
     */
    public static DebugModeType<?, ?> getDebugMode(String name) {
        return modeTypes.stream().filter(type -> type.getNiceName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Get a list of all the debug modes
     * @return All the debug modes which exist.
     */
    public static List<DebugModeType<?, ?>> getDebugModes() {
        return modeTypes;
    }

    /**
     * Gets the nice name of this debug mode
     * @return This debug modes nice name
     */
    public String getNiceName() {
        return niceName;
    }

    /**
     * Checks if this debug mode has a specific session
     * @param uuid The session to lookup
     * @return True if the session exists in this mode, false otherwise.
     */
    public boolean hasSession(UUID uuid) {
        return contextTrack.containsKey(uuid);
    }

    /**
     * Removes the specified session from this mode
     * @param uuid The uuid of the session to remove
     * @return True if the session was successfully removed, false otherwise
     */
    public boolean removeSession(UUID uuid) {
        if (hasSession(uuid)) {
            contextTrack.remove(uuid);
            return true;
        }
        return false;
    }

    /**
     * Gets the mode context for the specified player
     * @param uuid The uuid of the player to get the mode context from
     * @return The mode context, or null if the player does not exist in this mode
     */
    public C getDebugContext(UUID uuid) {
        return contextTrack.get(uuid);
    }

    /**
     * Creates a new mode context for the specified debug session
     * @param session The session to create the new context for
     * @return The newly created context.
     */
    public abstract C createContext(DebugSession session);

    /**
     * Gets the base permission of this mode.
     * @return The mode's base permission
     */
    public abstract String getBasePermission();

    /**
     * Gets the debug mode type of this debug mode
     * @return The debug mode type
     */
    public abstract T getModeType();

    /**
     * Adds a DebugSession to this mode
     * @param session The session to add to this mode
     * @return True if the session was successfully added, or false if it already exists/couldnt be added.
     */
    public boolean addSession(DebugSession session) {
        if (!hasSession(session.getSessionId())) {
            contextTrack.put(session.getSessionId(), createContext(session));
            return true;
        }
        return false;
    }

    /**
     * This will be used to cleanup any existing highlighted/selected blocks in the case of a quit event or a mode swap
     * @param session The session to pause this type for
     */
    public abstract void pauseSession(DebugSession session);

    /**
     * This will be used to re-highlight and reselect any once selected blocks in case the player rejoins the server or switches to this mode
     * @param session The session to resume
     */
    public abstract void resumeSession(DebugSession session);

    /**
     * Check whether the given session is paused in this mode
     * @param session The session to check
     * @return True if the session is paused in this mode, false otherwise.
     */
    public boolean isPaused(DebugSession session) {
        return paused.contains(session.getSessionId());
    }

    /**
     * Fires when a player interacts with something.
     */
    public abstract void onInteract(PlayerInteractEvent event);

    /**
     * Fires when a player moves
     */
    public abstract void onMove(PlayerMoveEvent event);

    /**
     * Fires when a player changes the item in their hand
     */
    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        if (ConfigKey.SOUND_ON_SELECT) {
            Player player = event.getPlayer();
            ItemStack oldSlot = player.getInventory().getItem(event.getPreviousSlot());
            ItemStack newSlot = player.getInventory().getItem(event.getNewSlot());

            if (DEBUG_STICK.equals(oldSlot)) {
                player.playSound(player.getLocation(), Sound.UI_TOAST_OUT, 1, 1);
            }
            if (DEBUG_STICK.equals(newSlot)) {
                player.playSound(player.getLocation(), Sound.UI_TOAST_IN, 1, 1);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DebugModeType) {
            return ((DebugModeType)obj).getClass().equals(type);
        } else return false;
    }
}
