package com.njdaeger.enhanceddebugstick.util.highlighter.impl;

import com.njdaeger.enhanceddebugstick.util.highlighter.IBlockHighlighter;
import com.njdaeger.enhanceddebugstick.util.highlighter.IHighlightSession;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenericBlockHighlighter implements IBlockHighlighter {
    
    private static final Map<UUID, IHighlightSession> sessions = new HashMap<>();
    
    @Override
    public void removeTask(Player player) {
        unlightAllBlocks(player);
        sessions.remove(player.getUniqueId());
    }
    
    @Override
    public void unlightAllBlocks(Player player) {
        if (sessions.containsKey(player.getUniqueId())) {
            IHighlightSession session = sessions.get(player.getUniqueId());
            session.removeAllBlocks();
        }
    }
    
    @Override
    public void unlightBlock(Block block, Player player) {
        if (sessions.containsKey(player.getUniqueId())) {
            IHighlightSession session = sessions.get(player.getUniqueId());
            session.removeBlock(block);
        }
    }
    
    @Override
    public void lightBlock(Block block, Player player) {
        IHighlightSession session;
        if (!sessions.containsKey(player.getUniqueId())) {
            session = createSession(player);
            sessions.put(player.getUniqueId(), session);
        } else session = sessions.get(player.getUniqueId());
        session.addBlock(block);
    }
    
}
