package com.njdaeger.enhanceddebugstick.api;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.List;
import java.util.Map;

/**
 * Represents a property
 *
 * @param <D> The blockdata type associated with this property
 * @param <V> The value type associated with this property
 */
@SuppressWarnings( "unused" )
public interface IProperty<D extends BlockData, V> {
    
    /**
     * The constant ordinal
     *
     * @return The constant ordinal
     */
    int ordinal();
    
    /**
     * Gets the nice name of this property.
     *
     * @return The property nice name
     */
    String getNiceName();
    
    /**
     * Gets the corresponding class type of the property.
     *
     * @return The class type
     */
    Class<D> getDataType();
    
    /**
     * Gets the corresponding class type of the value of this property.
     *
     * @return The value type
     */
    Class<V> getValueType();
    
    /**
     * Check if a specific BlockData object is an instance of this property's data type.
     *
     * @param block Whether the block data has this specific property
     * @return True if the block data has this property.
     */
    boolean checkBlockData(BlockData block);
    
    /**
     * Merges the value of this property from the original block data, to the property of the new block data.
     *
     * @param original The original block data to take this property from
     * @param data     The block data to apply the original value to
     * @return The modified block data. Or null if either of the provided arguments are not applicable to this property.
     */
    D mergeBlockData(BlockData original, BlockData data);
    
    /**
     * Merges the value of this property from the original block data, to the property of the new material.
     *
     * @param original The original block data to take this property from
     * @param material The material to apply the original value to
     * @return The modified block data. Or null if either of the provided arguments are not applicable to this property.
     */
    D mergeBlockData(BlockData original, Material material);
    
    /**
     * Merges the value of this property from the original block data, to the property of the new block.
     *
     * @param original The original block data to take this property from
     * @param block    The block to apply the original value to
     * @return The modified block data. Or null if either of the provided arguments are not applicable to this property.
     */
    D mergeBlockData(BlockData original, Block block);
    
    /**
     * Gets the data value currently associated with this property as a nice string.
     *
     * @param data The block data to get the property from
     * @return The current value of this property as a nice string.
     * @throws RuntimeException if the property is not applicable to the given BlockData
     */
    String getNiceCurrentValue(BlockData data);
    
    /**
     * Gets the data value currently associated with this property as a nice string.
     *
     * @param block The block to get the data from.
     * @return The current value of this property as a nice string
     * @throws RuntimeException if the property is not applicable to the given block
     */
    String getNiceCurrentValue(Block block);
    
    /**
     * Gets the current value of this property from the given block data.
     *
     * @param data The block data to get the value of the current property of
     * @return The current property value of this block data
     * @throws RuntimeException If this property is not applicable to the given BlockData
     */
    V getCurrentValue(BlockData data);
    
    /**
     * Gets the current value of this property from the given block.
     *
     * @param block The block to get the value of the current property of.
     * @return The current property value of this block.
     * @throws RuntimeException If this property is not applicable to the given block.
     */
    V getCurrentValue(Block block);
    
    /**
     * Gets the block data from a block data object to the correct block data type of this property.
     *
     * @param data THe original block data
     * @return The block data as this property's data type, or null if it is unable to be casted
     */
    D currentBlockData(BlockData data);
    
    /**
     * Gets the block data from one block to the data type of this property.
     *
     * @param block The original block.
     * @return The block data as this property's data type, or null if it is unable to be casted
     */
    D currentBlockData(Block block);
    
    /**
     * Gets the data value which is next in line for this particular property as a nice string.
     *
     * @param data The block data to get the property from
     * @return The next value of this property as a nice string.
     * @throws RuntimeException if the property is not applicable to the given BlockData
     */
    String getNiceNextValue(BlockData data);
    
    /**
     * Gets the data value which is next in line for this particular property as a nice string.
     *
     * @param block The block to get the data from.
     * @return The next value of this property as a nice string
     * @throws RuntimeException if the property is not applicable to the given block
     */
    String getNiceNextValue(Block block);
    
    /**
     * Gets the next value of this property from the given BlockData
     *
     * @param data The block data to get the next value of the current property of
     * @return The next property value of this block
     * @throws RuntimeException If this property is not applicable to the given BlockData
     */
    V getNextValue(BlockData data);
    
    /**
     * Gets the next value of this property from the given block.
     *
     * @param block The block to get the next value of the current property of.
     * @return The next property value of this block.
     * @throws RuntimeException If this property is not applicable to the given block.
     */
    V getNextValue(Block block);
    
    /**
     * Gets the next BlockData for this property
     *
     * @param data The previous data
     * @return The next data
     */
    D nextBlockData(BlockData data);
    
    /**
     * Gets the next BlockData for this property
     *
     * @param block The previous data
     * @return The next data
     */
    D nextBlockData(Block block);
    
    /**
     * Checks if a specific BlockData is applicable to this particular property
     *
     * @param data The block data to check
     * @return False if the material does not have any properties, or if its property list doesnt contain this specific
     * property.
     */
    boolean isApplicableTo(BlockData data);
    
    /**
     * Checks if a specific material has this particular property
     *
     * @param material The material to check.
     * @return False if the material does not have any properties, or if its property list doesnt contain this specific
     * property.
     */
    boolean isApplicableTo(Material material);
    
    /**
     * Checks if a specific block has this particular property
     *
     * @param block The block to check.
     * @return False if the block does not have any properties, or if its property list doesnt contain this specific
     * property.
     */
    boolean isApplicableTo(Block block);
    
    /**
     * Sets block data value for a specific block (this does not apply it to the given block)
     *
     * @param block The block to change the block data of
     * @param value The new value of the block data
     * @return The changed block data, or the old block data if the value wasn't applicable
     */
    D getBlockData(Block block, Object value);
    
    /**
     * Sets block data value to a specific block data object
     * @param data
     * @param value
     * @return
     */
    D getBlockData(BlockData data, Object value);
    
    D getBlockData(Material material, Object value);
    
    /**
     * Gets a map of all bukkit materials which have properies, and a list of their (editable) properties.
     *
     * @return A map of bukkit materials which have editable properties and a list of their editable properties.
     */
    static Map<Material, List<IProperty<?, ?>>> getMaterialProperties() {
        return AbstractProperty.getMaterialProperties();
    }
    
    /**
     * Get a list of (editable) properties this BlockData has, if any.
     *
     * @param blockData The BlockData to transform into a list of properties
     * @return The list of properties, or an empty list if the block has no (editable) properties.
     */
    static List<IProperty<?, ?>> getProperties(BlockData blockData) {
        return AbstractProperty.getProperties(blockData.getMaterial());
    }
    
    /**
     * Get a list of (editable) properties this material has, if any.
     *
     * @param material The material to get the list of properties of.
     * @return The list of properties, or an empty list if the block has no (editable) properties.
     */
    static List<IProperty<?, ?>> getProperties(Material material) {
        return AbstractProperty.getProperties(material);
    }
    
    /**
     * Gets a list of (editable) properties this block has.
     *
     * @param block The block to get the list of properties of.
     * @return The list of properties, or an empty list if this block has no (editable) properties.
     */
    static List<IProperty<?, ?>> getProperties(Block block) {
        return AbstractProperty.getProperties(block);
    }
    
    /**
     * Check whether a specific block data has any properties.
     *
     * @param data The block to check
     * @return True if the block data has (editable) properties, false otherwise.
     */
    static boolean hasProperties(BlockData data) {
        return AbstractProperty.hasProperties(data);
    }
    
    /**
     * Check whether a specific material has any properties.
     *
     * @param material The material to check
     * @return True if the material has (editable) properties, false otherwise.
     */
    static boolean hasProperties(Material material) {
        return AbstractProperty.hasProperties(material);
    }
    
    /**
     * Check whether a specific block has any properties.
     *
     * @param block The block to check
     * @return True if the block has (editable) properties, false otherwise.
     */
    static boolean hasProperties(Block block) {
        return AbstractProperty.hasProperties(block);
    }
    
    /**
     * Gets all the properties
     *
     * @return An array of all the properties
     */
    static IProperty<?, ?>[] values() {
        return AbstractProperty.values();
    }
    
}
