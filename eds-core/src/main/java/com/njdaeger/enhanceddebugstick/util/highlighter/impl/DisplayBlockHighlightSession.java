package com.njdaeger.enhanceddebugstick.util.highlighter.impl;

import com.njdaeger.enhanceddebugstick.util.highlighter.IHighlightSession;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DisplayBlockHighlightSession implements IHighlightSession {

    private final List<UUID> entities;

    public DisplayBlockHighlightSession(Player player) {
        this.entities = new ArrayList<>();
    }

    @Override
    public void addBlock(Block block) {

    }

    @Override
    public void removeBlock(Block block) {

    }

    @Override
    public void removeAllBlocks() {

    }
}
