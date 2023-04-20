package com.njdaeger.enhanceddebugstick.util.highlighter.impl;

import com.njdaeger.enhanceddebugstick.util.highlighter.IHighlightSession;
import org.bukkit.block.Block;
import org.bukkit.entity.Shulker;

import java.util.HashMap;
import java.util.Map;

public class GenericHighlightSession implements IHighlightSession {
    
    private final Map<Block, Shulker> shulkerMap = new HashMap<>();
    
    @Override
    public void addBlock(Block block) {
        Shulker shulker = block.getWorld().spawn(block.getLocation().add(0.5, 0, 0.5), Shulker.class);
        shulker.setAI(false);
        shulker.setInvulnerable(true);
        shulker.setSilent(true);
        shulker.setCollidable(false);
        shulker.setInvisible(true);
        shulker.setGravity(false);
        shulker.setGlowing(true);
        shulkerMap.put(block, shulker);
    }
    
    @Override
    public void removeBlock(Block block) {
        shulkerMap.get(block).remove();
    }
    
    @Override
    public void removeAllBlocks() {
        shulkerMap.values().forEach(Shulker::remove);
    }
}
