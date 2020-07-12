package com.njdaeger.enhanceddebugstick.event;

import com.njdaeger.enhanceddebugstick.modes.freeze.FreezeDebugContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

/**
 * An event which is called when someone unfreezes (either one at a time or all at once) blocks from the Freeze Debug
 * Mode.
 */
public class UnfreezeBlockEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final FreezeDebugContext debugContext;
    private List<Block> unfreezeList;
    private boolean cancelled;

    public UnfreezeBlockEvent(Player who, List<Block> listToUnfreeze, FreezeDebugContext debugContext) {
        super(who);
        this.cancelled = false;
        this.debugContext = debugContext;
        this.unfreezeList = listToUnfreeze;
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
     * Get the list of blocks which will be unfrozen. If this list has more than one block, it was a mass unfreeze of
     * blocks, otherwise it will return a single block which is being unfrozen.
     *
     * @return A list of block(s) which are to be unfrozen. An empty list if no blocks are being unfrozen.
     */
    public List<Block> getUnfrozenBlocks() {
        return unfreezeList;
    }

    /**
     * Set the list of blocks which are to be unfrozen.
     *
     * @param blocksToUnfreeze The list of blocks to unfreeze
     */
    public void setBlocksToUnfreeze(List<Block> blocksToUnfreeze) {
        this.unfreezeList = blocksToUnfreeze;
    }

    /**
     * Gets the current Freeze Debug Context which is using the debug stick
     *
     * @return The Freeze Debug context which is using the Enhanced Debug Stick
     */
    public FreezeDebugContext getDebugContext() {
        return debugContext;
    }
}
