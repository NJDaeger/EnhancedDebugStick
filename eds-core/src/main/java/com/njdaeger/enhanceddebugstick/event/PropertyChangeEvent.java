package com.njdaeger.enhanceddebugstick.event;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.modes.classic.ClassicDebugContext;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PropertyChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled;
    private Block block;
    private final IProperty lastProperty;
    private IProperty nextProperty;
    private final ClassicDebugContext context;

    public PropertyChangeEvent(Player who, Block block, IProperty lastProperty, IProperty nextProperty, ClassicDebugContext context) {
        super(who);
        this.lastProperty = lastProperty;
        this.nextProperty = nextProperty;
        this.block = block;
        this.context = context;
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

    public IProperty getNextProperty() {
        return nextProperty;
    }

    public void setNextProperty(IProperty newProperty) {
        this.nextProperty = newProperty;
    }

    public IProperty getLastProperty() {
        return lastProperty;
    }
    
    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public boolean isPropertyApplicable() {
        return nextProperty.isApplicableTo(block);
    }
    
    public Location getLocation() {
        return block.getLocation();
    }
    
    public ClassicDebugContext getDebugContext() {
        return context;
    }
    
}
