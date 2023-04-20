package com.njdaeger.enhanceddebugstick.util.highlighter.impl;

import com.njdaeger.enhanceddebugstick.util.BlockHighlighter;
import com.njdaeger.enhanceddebugstick.util.highlighter.IBlockHighlighter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Wrap the old block highlighter for backwards compatibility. This is only used on 1.19.3 and below.
 */
public class LegacyBlockHighlighter implements IBlockHighlighter {
    
    @Override
    public void removeTask(Player player) {
        BlockHighlighter.removeTask(player);
    }
    
    @Override
    public void unlightAllBlocks(Player player) {
        BlockHighlighter.unLightAllBlocks(player);
    }
    
    @Override
    public void unlightBlock(Block block, Player player) {
        BlockHighlighter.unLightBlock(block, player);
    }
    
    @Override
    public void lightBlock(Block block, Player player) {
        BlockHighlighter.lightBlock(block, player);
    }
}
