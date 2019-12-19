package com.njdaeger.enhanceddebugstick.modes.freeze;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugContext;
import com.njdaeger.enhanceddebugstick.util.BlockHighlighter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FreezeDebugContext implements DebugContext {

    private final DebugSession session;
    private final Map<Location, BlockData> frozen;

    FreezeDebugContext(DebugSession session) {
        this.session = session;
        this.frozen = new HashMap<>();
    }

    @Override
    public UUID getOwner() {
        return session.getSessionId();
    }

    @Override
    public DebugSession getDebugSession() {
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
            if (ConfigKey.FDM_OUTLINE) BlockHighlighter.lightBlock(block, Bukkit.getPlayer(session.getSessionId()));
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
        if (session.isOnline()) {
            if (ConfigKey.FDM_OUTLINE) BlockHighlighter.unLightBlock(block, Bukkit.getPlayer(session.getSessionId()));
            block.setBlockData(data, false);
        }
    }

    /**
     * Unlocks all currently selected blocks.
     */
    public void unfreezeAllBlocks() {
        if (session.isOnline()) unlightFrozen();
        frozen.clear();
    }

    /**
     * Lights any selected blocks for the player.
     */
    public void lightFrozen() {
        Player player = Bukkit.getPlayer(session.getSessionId());
        frozen.forEach((location, data) -> {
            if (ConfigKey.FDM_OUTLINE) BlockHighlighter.lightBlock(location.getBlock(), player);
            location.getBlock().setType(Material.RED_WOOL, false);
        });
    }

    /**
     * Unlights any selected blocks for the player
     */
    public void unlightFrozen() {
        Player player = Bukkit.getPlayer(session.getSessionId());
        if (ConfigKey.FDM_OUTLINE) BlockHighlighter.removeTask(player);
        frozen.forEach((location, data) -> location.getBlock().setBlockData(data, false));
    }

    /**
     * Checks if a block is selected or not
     *
     * @param block The block to check
     * @return True if the block is selected, false otherwise.
     */
    public boolean isSelected(Block block) {
        return isSelected(block.getLocation());
    }

    /**
     * Checks if a location is selected or not
     *
     * @param location The location to check
     * @return True if the location is selected, false otherwise.
     */
    public boolean isSelected(Location location) {
        return frozen.get(location) != null;
    }

}
