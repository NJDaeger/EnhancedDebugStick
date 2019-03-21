package com.njdaeger.enhanceddebugstick.api.modes.classic;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugStickEvent;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.HandlerList;

public final class PropertyChangeEvent extends DebugStickEvent {

    private static final HandlerList handlers = new HandlerList();
    private final IProperty<?, ?> oldSelection;
    private final com.njdaeger.enhanceddebugstick.api.modes.classic.Result result;
    private final IProperty<?, ?> newSelection;
    private final Block block;

    public PropertyChangeEvent(DebugSession session, Block block, IProperty<?, ?> oldSelection, IProperty<?, ?> newSelection, com.njdaeger.enhanceddebugstick.api.modes.classic.Result result) {
        super(session);
        this.oldSelection = oldSelection;
        this.newSelection = newSelection;
        this.result = result;
        this.block = block;
    }

    /**
     * Gets the block the property is being changed for.
     *
     * @return The block the property is being changed for.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Gets the previously selected property
     *
     * @return The previously selected property
     */
    public IProperty<?, ?> getOldProperty() {
        return oldSelection;
    }

    /**
     * Gets the BlockData of the previous property
     *
     * @return The previous BlockData of the previous property
     */
    public BlockData getOldBlockData() {
        return (BlockData) oldSelection.getCurrentValue(block);
    }

    /**
     * Gets the newly selected property
     *
     * @return The newly sepected property
     */
    public IProperty<?, ?> getNewProperty() {
        return newSelection;
    }

    /**
     * Gets the BlockData of the newly selected property
     *
     * @return The new BlockData of the new property
     */
    public BlockData getNewBlockData() {
        return (BlockData) newSelection.getCurrentValue(block);
    }

    /**
     * The result of the property change.
     *
     * @return A Failure if the block does not have properties, Success otherwise.
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
