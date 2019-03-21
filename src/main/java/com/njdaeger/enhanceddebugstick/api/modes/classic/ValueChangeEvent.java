package com.njdaeger.enhanceddebugstick.api.modes.classic;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugStickEvent;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public final class ValueChangeEvent extends DebugStickEvent {

    private static final HandlerList handlers = new HandlerList();
    private final IProperty<?, ?> edited;
    private final com.njdaeger.enhanceddebugstick.api.modes.classic.Result result;
    private final Object from;
    private final Object to;
    private final Block block;

    public ValueChangeEvent(DebugSession session, Block block, IProperty<?, ?> edited, Object from, Object to, com.njdaeger.enhanceddebugstick.api.modes.classic.Result result) {
        super(session);
        this.edited = edited;
        this.result = result;
        this.block = block;
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the block the value is being changed for.
     *
     * @return The block the value is being changed for
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Gets the property currently being edited.
     *
     * @return The property currently being edited
     */
    public IProperty<?, ?> getProperty() {
        return edited;
    }

    /**
     * Gets the old value of the property
     *
     * @return The old value of the property
     */
    public Object getFrom() {
        return from;
    }

    /**
     * Gets the new value of the property
     *
     * @return The new value of the property
     */
    public Object getTo() {
        return to;
    }

    /**
     * The result of this value change
     *
     * @return A Failure if the block does not have any properties, Success otherwise.
     */
    public com.njdaeger.enhanceddebugstick.api.modes.classic.Result getResult() {
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
