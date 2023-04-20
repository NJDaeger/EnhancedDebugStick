package com.njdaeger.enhanceddebugstick.util.highlighter;

import org.bukkit.block.Block;

public interface IHighlightSession {
    
    void addBlock(Block block);
    
    void removeBlock(Block block);
    
    void removeAllBlocks();
    
}
