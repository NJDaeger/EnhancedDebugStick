package com.njdaeger.enhanceddebugstick.util.highlighter.impl;

import com.njdaeger.enhanceddebugstick.util.highlighter.IHighlightSession;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisplayBlockHighlightSession implements IHighlightSession {

    private final Map<Location, UUID> entities;
    private final Player player;
    private final Plugin plugin;

    public DisplayBlockHighlightSession(Plugin plugin, Player player) {
        this.entities = new HashMap<>();
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public void addBlock(Block block) {
        var location = block.getLocation().clone();
        location.add(0.5, 0.5, 0.5);
        var display = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
        display.setBlock(block.getBlockData());
        display.setGlowing(true);
        display.setVisibleByDefault(false);

        entities.put(block.getLocation(), display.getUniqueId());
        player.showEntity(plugin, display);
    }

    @Override
    public void removeBlock(Block block) {
        var entity = player.getWorld().getEntity(entities.get(block.getLocation()));
        if (entity == null) return;
        player.hideEntity(plugin, entity);
        entity.remove();
        entities.remove(block.getLocation());
    }

    @Override
    public void removeAllBlocks() {
        for (var entry : entities.entrySet()) {
            var entity = player.getWorld().getEntity(entry.getValue());
            if (entity == null) continue;
            player.hideEntity(plugin, entity);
            entity.remove();
        }
        entities.clear();
    }
}
