package com.njdaeger.enhanceddebugstick.modes.freeze;

import com.njdaeger.enhanceddebugstick.api.config.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.mode.IFreezeDebugContext;
import com.njdaeger.enhanceddebugstick.api.session.IDebugSession;
import com.njdaeger.enhanceddebugstick.util.highlighter.IBlockHighlighter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class FreezeDebugContext implements IFreezeDebugContext {
    
    private final IDebugSession session;
    private final Map<Location, BlockData> frozen;
    private final IBlockHighlighter highlighter;
    
    FreezeDebugContext(IDebugSession session, IBlockHighlighter highlighter) {
        this.session = session;
        this.frozen = new HashMap<>();
        this.highlighter = highlighter;
    }
    
    @Override
    public UUID getOwner() {
        return session.getSessionId();
    }
    
    @Override
    public IDebugSession getDebugSession() {
        return session;
    }
    
    /**
     * Checks whether this player has any frozen blocks
     *
     * @return True if the player has frozen blocks, false otherwise.
     */
    public boolean hasFrozenBlocks() {
        return !frozen.isEmpty();
    }
    
    /**
     * Freezes the specified block for the player, replacing it with a red wool block and highlighting it if theyre
     * online
     *
     * @param block The block to lock
     */
    public void freezeBlock(Block block) {
        frozen.put(block.getLocation(), block.getBlockData());
        if (session.isOnline()) {
            Player player = Bukkit.getPlayer(session.getSessionId());
            if (ConfigKey.get().FDM_OUTLINE) {
                highlighter.lightBlock(block, player);
            }
            block.setType(Material.RED_WOOL, false);
        }
    }
    
    /**
     * Unfreezes the specified block for the player, putting it  back to its original state.
     *
     * @param block The block to unlock
     */
    public void unfreezeBlock(Block block) {
        BlockData data = frozen.remove(block.getLocation());
        if (session.isOnline() && data != null) {
            Player player = Bukkit.getPlayer(session.getSessionId());
            if (ConfigKey.get().FDM_OUTLINE) {
                highlighter.unlightBlock(block, player);
            }
            block.setBlockData(data, false);
        }
    }
    
    /**
     * Attempts to unfreeze a list of blocks. If the block is not frozen, nothing will be done.
     * @param blocks The blocks to attempt to unfreeze.
     */
    public void unfreezeBlocks(List<Block> blocks) {
        blocks.forEach(this::unfreezeBlock);
    }
    
    /**
     * Unlocks all currently frozen blocks.
     */
    public void unfreezeAllBlocks() {
        if (session.isOnline()) {
            unlightFrozen();
        }
        Collection<Location> locations = new ArrayList<>(frozen.keySet());
        locations.forEach(loc -> unfreezeBlock(loc.getBlock()));
    }
    
    /**
     * Lights any frozen blocks for the player.
     */
    public void lightFrozen() {
        Player player = Bukkit.getPlayer(session.getSessionId());
        frozen.forEach((location, data) -> {
            if (ConfigKey.get().FDM_OUTLINE) {
                highlighter.lightBlock(location.getBlock(), player);
            }
            location.getBlock().setType(Material.RED_WOOL, false);
        });
    }
    
    /**
     * Unlights any frozen blocks for the player
     */
    public void unlightFrozen() {
        Player player = Bukkit.getPlayer(session.getSessionId());
        if (ConfigKey.get().FDM_OUTLINE) {
            highlighter.removeTask(player);
        }
        frozen.forEach((location, data) -> location.getBlock().setBlockData(data, false));
    }
    
    /**
     * Checks if a block is selected or not
     *
     * @param block The block to check
     * @return True if the block is selected, false otherwise.
     */
    public boolean isFrozen(Block block) {
        return isFrozen(block.getLocation());
    }
    
    /**
     * Checks if a location is selected or not
     *
     * @param location The location to check
     * @return True if the location is selected, false otherwise.
     */
    public boolean isFrozen(Location location) {
        return frozen.get(location) != null;
    }
    
    /**
     * Gets the list of currently frozen blocks.
     *
     * @return The list of frozen blocks.
     */
    public List<Block> getFrozen() {
        return frozen.keySet().stream().map(Location::getBlock).collect(Collectors.toList());
    }
    
}
