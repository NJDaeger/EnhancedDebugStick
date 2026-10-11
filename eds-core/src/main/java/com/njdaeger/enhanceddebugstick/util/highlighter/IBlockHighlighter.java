package com.njdaeger.enhanceddebugstick.util.highlighter;

import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import com.njdaeger.enhanceddebugstick.util.highlighter.impl.DisplayBlockHighlightSession;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface IBlockHighlighter {
    
    /**
     * This unlights all the blocks for a specific player and removes their AbstractHighlighter instance
     * @param player The player to unlight all blocks from and remove the AbstractHighlighter instance of
     */
    void removeTask(Player player);
    
    /**
     * This unlights all the blocks for a specific player without removing their AbstractHighlighter instance.
     * @param player The player to unlight all blocks from
     */
    void unlightAllBlocks(Player player);
    
    /**
     * Unlights a single block for a player
     * @param block The block to unlight
     * @param player The player to unlight the block for
     */
    void unlightBlock(Block block, Player player);
    
    /**
     * Lights a single block for a player
     * @param block The block to light
     * @param player The player to light the block for
     */
    void lightBlock(Block block, Player player);
    
    default IHighlightSession createSession(Player player) {
        return new DisplayBlockHighlightSession(EnhancedDebugStick.getPlugin(EnhancedDebugStick.class), player);
    }
    
    
}
