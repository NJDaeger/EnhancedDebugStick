package com.njdaeger.enhanceddebugstick.api.event;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public abstract class AbstractPropertySendEvent extends PlayerEvent implements Cancellable {
    
    private boolean cancelled;
    private Block blockBeingSent;
    private List<IProperty<?, ?>> properties;
    
    public AbstractPropertySendEvent(Player who, Block blockBeingSent, List<IProperty<?, ?>> properties) {
        super(who);
        this.cancelled = false;
        this.blockBeingSent = blockBeingSent;
        this.properties = properties;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public Block getBlockBeingSent() {
        return blockBeingSent;
    }
    
    public List<IProperty<?, ?>> getProperties() {
        return properties;
    }

    public void setProperties(List<IProperty<?, ?>> properties) {
        this.properties = properties;
    }
}
