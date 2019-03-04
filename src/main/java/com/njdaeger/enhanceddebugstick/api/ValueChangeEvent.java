package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.Property;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public final class ValueChangeEvent extends DebugStickEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Property<?, ?> edited;
    private final com.njdaeger.enhanceddebugstick.api.Result result;
    private final Object from;
    private final Object to;

    public ValueChangeEvent(DebugSession session, Block block, Property<?, ?> edited, Object from, Object to, com.njdaeger.enhanceddebugstick.api.Result result) {
        super(session, block);
        this.edited = edited;
        this.result = result;
        this.from = from;
        this.to = to;
    }

    public Property<?, ?> getProperty() {
        return edited;
    }

    public Object getFrom() {
        return from;
    }

    public Object getTo() {
        return to;
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
