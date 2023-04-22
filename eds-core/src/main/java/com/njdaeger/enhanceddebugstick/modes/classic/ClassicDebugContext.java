package com.njdaeger.enhanceddebugstick.modes.classic;

import com.njdaeger.enhanceddebugstick.api.mode.IClassicDebugContext;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.njdaeger.enhanceddebugstick.api.IProperty.getProperties;
import static com.njdaeger.enhanceddebugstick.api.IProperty.hasProperties;
import static com.njdaeger.enhanceddebugstick.util.Util.format;

public final class ClassicDebugContext implements IClassicDebugContext {
    
    private final Map<Material, Integer> currentProperty;
    private final IDebugSession session;
    
    ClassicDebugContext(IDebugSession session) {
        this.currentProperty = new HashMap<>();
        this.session = session;
        
        for (Material mat : IProperty.getMaterialProperties().keySet()) {
            currentProperty.put(mat, 0);
        }
    }
    
    @Override
    public UUID getOwner() {
        return session.getSessionId();
    }
    
    @Override
    public IDebugSession getDebugSession() {
        return session;
    }
    
    /**
     * Gets the current property selected for the specific block.
     *
     * @param block The block to get the current property of.
     * @return The property selected for the block, or null if the block has no properties.
     */
    public IProperty<?, ?> getCurrentProperty(Block block) {
        return getCurrentProperty(block.getType());
    }
    
    /**
     * Gets the current property selected for the specific material.
     *
     * @param material The material to get the current property of
     * @return The property selected for the material, or null if the material has no properties.
     */
    public IProperty<?, ?> getCurrentProperty(Material material) {
        if (!hasProperties(material)) {
            return null;
        } else {
            return getProperties(material).get(currentProperty.get(material));
        }
    }
    
    /**
     * Gets the next property to be selected for the specific block. (This will not change the current selection of the
     * block, it will only find the next property which will be selected by the {@link #applyNextPropertyFor(Block)}
     * method)
     *
     * @param block The block to get the next property of
     * @return The next property to be selected for the block, or null if the block has no properties.
     */
    public IProperty<?, ?> getNextProperty(Block block) {
        return getNextProperty(block.getType());
    }
    
    /**
     * Gets the next property to be selected for the specific material. (This will not change the current selection of
     * the material, it will only find the next property which will be selected by the {@link
     * #applyNextPropertyFor(Block)} method)
     *
     * @param material The material to get the next property of
     * @return The next property to be selected for the material, or null if the material has no properties.
     */
    public IProperty<?, ?> getNextProperty(Material material) {
        if (!hasProperties(material)) {
            return null;
        }
        int current = currentProperty.get(material);
        if (current == getProperties(material).size() - 1) {
            current = 0;
        } else {
            current++;
        }
        return getProperties(material).get(current);
    }
    
    /**
     * Sets the current property to be selected for the specific block
     *
     * @param block    The block to change the current property of.
     * @param property The property to change the block's selection to.
     * @throws RuntimeException if the property given is not applicable to the specified block.
     */
    public void setCurrentProperty(Block block, IProperty<?, ?> property) {
        setCurrentProperty(block.getType(), property);
    }
    
    /**
     * Sets the current property to be selected for the specific material
     *
     * @param material The material to change the current property of.
     * @param property The property to change the material's selection to.
     * @throws RuntimeException if the property given is not applicable to the specified material.
     */
    public void setCurrentProperty(Material material, IProperty<?, ?> property) {
        if (!property.isApplicableTo(material)) {
            throw new RuntimeException("Property \"" + property.getNiceName() + "\" is not applicable for material " + material.name());
        }
        currentProperty.replace(material, getProperties(material).indexOf(property));
    }
    
    /**
     * Shifts the selected property to the next property for the specific block. Does nothing if the block has no
     * properties.
     *
     * @param block The block to shift to the next property for.
     */
    public void applyNextPropertyFor(Block block) {
        if (!hasProperties(block)) {
            return;
        }
        int current = currentProperty.get(block.getType());
        if (current == getProperties(block).size() - 1) {
            current = 0;
        } else {
            current++;
        }
        currentProperty.replace(block.getType(), current);
    }
    
    /**
     * Sets the current property of the specified block to the given value.
     *
     * @param block The block to set the property of
     * @param value The value of the new property value
     */
    public void setValue(Block block, Object value) {
        if (!hasProperties(block) || !getCurrentProperty(block).getValueType().isInstance(value)) {
            return;
        }
        block.setBlockData(getCurrentProperty(block).getBlockData(block, value), false);
    }
    
    /**
     * Shifts the selected value (of the block's current selected property) to the next value, then sets the blockdata
     * in the world. Does nothing if the block has no properties.
     *
     * @param block The block to shift to the next property value for.
     */
    @SuppressWarnings( "all" )
    public void applyNextValueFor(Block block) {
        if (!hasProperties(block)) {
            return;
        }
        block.setBlockData(getCurrentProperty(block).nextBlockData(block), false);
    }
    
    /**
     * Sends the player information about the properties of a specific block via ActionBar. If this block has no
     * properties, or if the block is null, the message sent will be empty.
     *
     * @param block The block to get and send the properties of.
     */
    public void sendPropertiesOf(Block block) {
        StringBuilder builder = new StringBuilder();
        if (block != null) {
            for (IProperty<?, ?> property : getProperties(block)) {
                if (getCurrentProperty(block) == property) {
                    builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD).append(ChatColor.UNDERLINE).append(property.getNiceName()).append(": ");
                    builder.append(ChatColor.GRAY).append(ChatColor.BOLD).append(ChatColor.UNDERLINE).append(format(property.getNiceCurrentValue(block)));
                } else {
                    builder.append(ChatColor.DARK_GREEN).append(property.getNiceName()).append(": ");
                    builder.append(ChatColor.GRAY).append(format(property.getNiceCurrentValue(block)));
                }
                builder.append(ChatColor.RESET).append("    ");
            }
        }
        session.sendBar(builder.toString().trim());
    }
}
