package com.njdaeger.enhanceddebugstick.api.event;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.api.mode.ICopyDebugContext;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public class PastePropertyEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private BlockData currentClipboard;
    private List<IProperty<?, ?>> propertiesToPaste;
    private final ICopyDebugContext debugContext;
    private Block before;
    private boolean cancelled;

    public PastePropertyEvent(Player who, Block before, ICopyDebugContext debugContext) {
        super(who);
        this.before = before;
        this.cancelled = false;
        this.debugContext = debugContext;
        this.propertiesToPaste = debugContext.getClipboardProperties();
        this.currentClipboard = debugContext.getClipboard().clone();
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
     * Gets the debug context for this event
     * @return The debug context
     */
    public ICopyDebugContext getDebugContext() {
        return debugContext;
    }
    
    /**
     * Gets the block before the paste event
     * @return The block before the paste event
     */
    public Block getBefore() {
        return before;
    }
    
    /**
     * Sets the block before the paste event
     * @param block The block before the paste event
     */
    public void setBefore(Block block) {
        this.before = block;
    }
    
    /**
     * Gets the properties that are to be pasted on the block
     * @return The properties to be pasted
     */
    public List<IProperty<?, ?>> getClipboardProperties() {
        return propertiesToPaste;
    }
    
    /**
     * Sets the properties that are to be pasted on the block. Note that this does not change the properties in the {@link ICopyDebugContext}
     * @param properties The properties to be pasted
     */
    public void setPastedProperties(List<IProperty<?, ?>> properties) {
        this.propertiesToPaste = properties;
    }
    
    /**
     * Gets the current blockdata in the clipboard
     * @return The current blockdata in the clipboard
     */
    public BlockData getCurrentClipboard() {
        return currentClipboard;
    }
    
    /**
     * Sets the current blockdata in the clipboard for the duration of this event. Note that this does not change the blockdata in the {@link ICopyDebugContext}
     * @param currentClipboard The blockdata that is to be merged with the pasted properties
     */
    public void setCurrentClipboard(BlockData currentClipboard) {
        this.currentClipboard = currentClipboard;
    }
    
    /**
     * Gets the resulting blockdata after the paste event
     * @return The resulting blockdata after the paste event
     */
    public BlockData getAfter() {
        BlockData prev = before.getBlockData().clone();
        for (IProperty<?, ?> prop : propertiesToPaste) {
            if (prop.isApplicableTo(prev)) prev =  prop.mergeBlockData(debugContext.getClipboard(), prev);
        }
        return prev;
    }
}
