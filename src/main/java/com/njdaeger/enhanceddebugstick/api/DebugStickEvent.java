package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.DebugSession;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

public abstract class DebugStickEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled = false;
    private final DebugSession session;

    public DebugStickEvent(DebugSession session) {
        super(Bukkit.getPlayer(session.getSessionId()));
        this.session = session;
    }

    public DebugSession getSession() {
        return session;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
