package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import com.njdaeger.enhanceddebugstick.modes.classic.ClassicDebugMode;
import com.njdaeger.enhanceddebugstick.modes.locked.LockedDebugMode;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DebugModeType<T extends DebugModeType<T, C>, C extends DebugContext> {

    public static final ClassicDebugMode CLASSIC = new ClassicDebugMode();
    public static final LockedDebugMode LOCKED = new LockedDebugMode();

    protected final EnhancedDebugStick plugin;
    protected final Map<UUID, C> contextTrack;
    private final String niceName;
    private final Class<T> type;

    public DebugModeType(String niceName, Class<T> type) {
        this.plugin = EnhancedDebugStick.getPlugin(EnhancedDebugStick.class);
        this.contextTrack = new HashMap<>();
        this.niceName = niceName;
        this.type = type;
    }

    public String getNiceName() {
        return niceName;
    }

    public boolean hasSession(UUID uuid) {
        return contextTrack.containsKey(uuid);
    }

    public boolean removeSession(UUID uuid) {
        if (hasSession(uuid)) {
            contextTrack.remove(uuid);
            return true;
        }
        return false;
    }

    public C getDebugContext(UUID uuid) {
        return contextTrack.get(uuid);
    }

    public abstract String getPermission();

    public abstract DebugModeType getModeType();

    public abstract boolean addSession(DebugSession session);

    public abstract void onInteract(PlayerInteractEvent event);

    public abstract void onMove(PlayerMoveEvent event);

    public abstract void onHeld(PlayerItemHeldEvent event);

    public abstract void onEntityInteract(PlayerInteractEntityEvent event);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DebugModeType) {
            return ((DebugModeType)obj).getClass().equals(type);
        } else return false;
    }
}
