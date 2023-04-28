package com.njdaeger.enhanceddebugstick.api.event;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.List;

public class MeshedPropertySendEvent extends AbstractPropertySendEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    
    private List<IProperty<?, ?>> clipboardProperties;
    
    public MeshedPropertySendEvent(Player who, Block blockBeingSent, List<IProperty<?, ?>> properties, List<IProperty<?, ?>> clipboardProperties) {
        super(who, blockBeingSent, properties);
        this.clipboardProperties = clipboardProperties;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public List<IProperty<?, ?>> getClipboardProperties() {
        return clipboardProperties;
    }
    
    public void setClipboardProperties(List<IProperty<?, ?>> clipboardProperties) {
        this.clipboardProperties = clipboardProperties;
    }
    
}
