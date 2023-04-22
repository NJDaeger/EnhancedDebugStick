package com.njdaeger.enhanceddebugstick.api.event;

import com.njdaeger.enhanceddebugstick.api.mode.IFreezeDebugContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * An event which is called when someone tries to freeze a block with the Freeze Debug Mode.
 */
public class FreezeBlockEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final IFreezeDebugContext debugContext;
    private boolean cancelled;
    private Block toFreeze;

    public FreezeBlockEvent(Player who, Block toFreeze, IFreezeDebugContext debugContext) {
        super(who);
        this.cancelled = false;
        this.toFreeze = toFreeze;
        this.debugContext = debugContext;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    /**
     * The block initially meant to be frozen
     *
     * @return The block which is meant to be frozen.
     */
    public Block getFrozenBlock() {
        return toFreeze;
    }

    /**
     * Sets the block which is to be frozen.
     *
     * @param toFreeze The block which is to now be frozen.
     */
    public void setFrozenBlock(Block toFreeze) {
        this.toFreeze = toFreeze;
    }

    /**
     * Gets the current Freeze Debug Context which is using the debug stick
     *
     * @return The Freeze Debug context which is using the Enhanced Debug Stick
     */
    public IFreezeDebugContext getDebugContext() {
        return debugContext;
    }

}
