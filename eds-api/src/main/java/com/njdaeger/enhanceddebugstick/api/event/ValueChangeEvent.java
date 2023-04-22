package com.njdaeger.enhanceddebugstick.api.event;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.api.mode.IClassicDebugContext;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ValueChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled;
    private Block block;
    private final Object lastValue;
    private final IProperty property;
    private Object nextValue;
    private final IClassicDebugContext context;

    public ValueChangeEvent(Player who, Block block, Object lastValue, Object nextValue, IProperty property, IClassicDebugContext context) {
        super(who);
        this.block = block;
        this.lastValue = lastValue;
        this.nextValue = nextValue;
        this.property = property;
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

    public Location getLocation() {
        return block.getLocation();
    }

    public Block getBefore() {
        return block;
    }

    public void setBlockBefore(Block block) {
        this.block = block;
    }

    public BlockData getAfter() {
        return property.getBlockData(block, nextValue);
    }
    
    public Object getLastValue() {
        return lastValue;
    }

    public <T> T getLastValueAs(Class<T> cls) {
        return cls.cast(lastValue);
    }
    
    public Object getNextValue() {
        return nextValue;
    }
    
    public <T> T getNextValueAs(Class<T> cls) {
        return cls.cast(nextValue);
    }
    
    public void setNextValue(Object nextValue) {
        this.nextValue = nextValue;
    }
    
    public boolean isValueApplicable() {
        return getProperty() != null && property.isApplicableTo(block) && property.getValueType().isInstance(nextValue);
    }
    
    public IProperty getProperty() {
        return property;
    }
    
    public IClassicDebugContext getDebugContext() {
        return context;
    }
    
}
