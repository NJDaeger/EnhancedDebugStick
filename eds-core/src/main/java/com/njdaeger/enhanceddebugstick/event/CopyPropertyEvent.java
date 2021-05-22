package com.njdaeger.enhanceddebugstick.event;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.modes.copy.CopyDebugContext;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public class CopyPropertyEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final CopyDebugContext debugContext;
    private boolean cancelled;
    private Block copied;

    public CopyPropertyEvent(Player who, Block copied, CopyDebugContext debugContext) {
        super(who);
        this.copied = copied;
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

    public CopyDebugContext getDebugContext() {
        return debugContext;
    }

    public List<IProperty<?,?>> getCopiedProperties() {
        return IProperty.getProperties(getCopiedBlock().getBlockData());
    }

    public Block getCopiedBlock() {
        return copied;
    }

    public void setCopiedBlock(Block copied) {
        this.copied = copied;
    }

    public Location getCopyLocation() {
        return getCopiedBlock().getLocation();
    }

}
