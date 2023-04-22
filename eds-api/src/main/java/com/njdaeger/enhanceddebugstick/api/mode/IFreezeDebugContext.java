package com.njdaeger.enhanceddebugstick.api.mode;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public interface IFreezeDebugContext extends IDebugContext {
    
    /**
     * Checks whether this player has any frozen blocks
     *
     * @return True if the player has frozen blocks, false otherwise.
     */
    boolean hasFrozenBlocks();
    
    /**
     * Freezes the specified block for the player, replacing it with a red wool block and highlighting it if theyre
     * online
     *
     * @param block The block to lock
     */
    void freezeBlock(Block block);
    
    /**
     * Unfreezes the specified block for the player, replacing it with the original block data and removing the
     * highlight if theyre online
     *
     * @param block The block to unlock
     */
    void unfreezeBlock(Block block);
    
    /**
     * Unfreezes all blocks for the player, replacing them with the original block data and removing the highlight if
     * theyre online
     */
    void unfreezeAllBlocks();
    
    /**
     * Lights any frozen blocks for the player.
     */
    void lightFrozen();
    
    /**
     * Unlights any frozen blocks for the player
     */
    void unlightFrozen();
    
    /**
     * Checks if a block is selected or not
     *
     * @param block The block to check
     * @return True if the block is selected, false otherwise.
     */
    boolean isFrozen(Block block);
    
    /**
     * Checks if a location is selected or not
     *
     * @param location The location to check
     * @return True if the location is selected, false otherwise.
     */
    boolean isFrozen(Location location);
    
    /**
     * Gets the list of currently frozen blocks.
     *
     * @return The list of frozen blocks.
     */
    List<Block> getFrozen();
    
}
