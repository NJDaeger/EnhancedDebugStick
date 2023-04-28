package com.njdaeger.enhanceddebugstick.api.event;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PropertySendEvent extends AbstractPropertySendEvent {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    public PropertySendEvent(Player who, Block blockBeingSent, List<IProperty<?, ?>> properties) {
        super(who, blockBeingSent, properties);
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
