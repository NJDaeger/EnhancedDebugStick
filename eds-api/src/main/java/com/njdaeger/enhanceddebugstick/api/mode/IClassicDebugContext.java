package com.njdaeger.enhanceddebugstick.api.mode;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.Material;
import org.bukkit.block.Block;

public interface IClassicDebugContext extends IDebugContext {

    /**
     * Gets the current property selected for the specific block.
     *
     * @param block The block to get the current property of.
     * @return The property selected for the block, or null if the block has no properties.
     */
    IProperty<?, ?> getCurrentProperty(Block block);

    /**
     * Gets the current property selected for the specific material.
     *
     * @param material The material to get the current property of
     * @return The property selected for the material, or null if the material has no properties.
     */
    IProperty<?, ?> getCurrentProperty(Material material);

    /**
     * Sets the current property selected for the specific block.
     *
     * @param block    The block to set the current property of.
     * @param property The property to set the block to.
     */
    void setCurrentProperty(Block block, IProperty<?, ?> property);

    /**
     * Sets the current property selected for the specific material.
     *
     * @param material The material to set the current property of.
     * @param property The property to set the material to.
     */
    void setCurrentProperty(Material material, IProperty<?, ?> property);

    /**
     * Gets the next property for the specific block.
     *
     * @param block The block to get the next property of.
     * @return The next property for the block, or null if the block has no properties.
     */
    IProperty<?, ?> getNextProperty(Block block);

    /**
     * Gets the next property for the specific material.
     *
     * @param material The material to get the next property of.
     * @return The next property for the material, or null if the material has no properties.
     */
    IProperty<?, ?> getNextProperty(Material material);
    
    /**
     * Shifts the selected property to the next property for the specific block. Does nothing if the block has no
     * properties.
     *
     * @param block The block to shift to the next property for.
     */
    void applyNextPropertyFor(Block block);
    
    /**
     * Shifts the selected value (of the block's current selected property) to the next value, then sets the blockdata
     * in the world. Does nothing if the block has no properties.
     *
     * @param block The block to shift to the next property value for.
     */
    void applyNextValueFor(Block block);
    
    /**
     * Sets the current property of the specified block to the given value.
     *
     * @param block The block to set the property of
     * @param value The value of the new property value
     */
    void setValue(Block block, Object value);
    
    /**
     * Sends the player information about the properties of a specific block via ActionBar. If this block has no
     * properties, or if the block is null, the message sent will be empty.
     *
     * @param block The block to get and send the properties of.
     */
    void sendPropertiesOf(Block block);
    
    
}
