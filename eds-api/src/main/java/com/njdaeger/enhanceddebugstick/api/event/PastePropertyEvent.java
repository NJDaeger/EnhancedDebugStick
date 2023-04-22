package com.njdaeger.enhanceddebugstick.api.event;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.api.mode.ICopyDebugContext;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PastePropertyEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ICopyDebugContext debugContext;
    private Block before;
    private boolean cancelled;

    public PastePropertyEvent(Player who, Block before, ICopyDebugContext debugContext) {
        super(who);
        this.before = before;
        this.cancelled = false;
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

    public ICopyDebugContext getDebugContext() {
        return debugContext;
    }

    public Block getBefore() {
        return before;
    }

    public void setBefore(Block block) {
        this.before = block;
    }

    public BlockData getAfter() {
        BlockData prev = before.getBlockData().clone();
        for (IProperty<?, ?> prop : debugContext.getClipboardProperties()) {
            if (prop.isApplicableTo(prev)) prev =  prop.mergeBlockData(debugContext.getClipboard(), prev);
        }
        return prev;
    }
}
