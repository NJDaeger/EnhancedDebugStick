package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.Property;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

import java.util.Collection;

public abstract class DebugStickEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled = false;
    private final DebugSession session;
    private final Block block;

    public DebugStickEvent(DebugSession session, Block block) {
        super(Bukkit.getPlayer(session.getSessionId()));
        this.session = session;
        this.block = block;
    }

    public DebugSession getSession() {
        return session;
    }

    public Block getBlock() {
        return block;
    }

    public Collection<Property<?, ?>> getBlockProperties() {
        return Property.getProperties(block);
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
