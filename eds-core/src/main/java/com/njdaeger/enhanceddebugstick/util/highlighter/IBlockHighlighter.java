package com.njdaeger.enhanceddebugstick.util.highlighter;

import com.njdaeger.enhanceddebugstick.mcversion.Version;
import com.njdaeger.enhanceddebugstick.util.highlighter.impl.GenericHighlightSession;
import com.njdaeger.enhanceddebugstick.util.highlighter.impl.v1194HighlightSession;
import org.bukkit.Bukkit;
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
        //when using this implementation, we will never be on any version less than 1.19.4
        if (Version.getCurrentVersion() == Version.v1_19_4) {
            return new v1194HighlightSession(player);
        } else {
            Bukkit.getLogger().warning("Block highlighting for the freeze mode may not work as intended. The entities will likely be visible to other players.");
            return new GenericHighlightSession();
        }
    }
    
    
}
