package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.Property;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public final class PropertyChangeEvent extends DebugStickEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Property<?, ?> oldSelection;
    private final com.njdaeger.enhanceddebugstick.api.Result result;
    private final Property<?, ?> newSelection;

    public PropertyChangeEvent(DebugSession session, Block block, Property<?, ?> oldSelection, Property<?, ?> newSelection, com.njdaeger.enhanceddebugstick.api.Result result) {
        super(session, block);
        this.oldSelection = oldSelection;
        this.newSelection = newSelection;
        this.result = result;
    }

    public Property<?, ?> getOldProperty() {
        return oldSelection;
    }

    public Property<?, ?> getNewProperty() {
        return newSelection;
    }

    public com.njdaeger.enhanceddebugstick.api.Result getResult() {
        return result;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
