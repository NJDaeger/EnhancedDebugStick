package com.njdaeger.enhanceddebugstick.modes.locked;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugContext;
import com.njdaeger.enhanceddebugstick.util.BlockHighlighter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LockedDebugContext implements DebugContext {

    private final List<Block> locked;
    private final DebugSession session;

    LockedDebugContext(DebugSession session) {
        this.session = session;
        this.locked = new ArrayList<>();
    }

    @Override
    public UUID getOwner() {
        return session.getSessionId();
    }

    @Override
    public DebugSession getDebugSession() {
        return session;
    }

    public void lockBlock(Block block) {
        if (session.isOnline()) BlockHighlighter.lightBlock(block, Bukkit.getPlayer(session.getSessionId()));
        locked.add(block);
    }

    public void unlockBlock(Block block) {
        if (session.isOnline()) BlockHighlighter.unLightBlock(block, Bukkit.getPlayer(session.getSessionId()));
        locked.remove(block);
    }

    public void unlockAllBlocks() {
        if (session.isOnline()) BlockHighlighter.unLightAllBlocks(Bukkit.getPlayer(session.getSessionId()));
        locked.clear();
    }

    public boolean isSelected(Block block) {
        return locked.contains(block);
    }

    public boolean isSelected(Location location) {
        return locked.stream().anyMatch(b -> b.getLocation().equals(location));
    }

    public boolean needsManualPlacement(Location location) {
        boolean manual = false;
        for (int x = location.getBlockX()-1; x <= location.getBlockX()+1; x++) {
            for (int y = location.getBlockY()-1; y <= location.getBlockY()+1; y++) {
                for (int z = location.getBlockZ()-1; z <= location.getBlockZ()+1; z++) {
                    if (isSelected(new Location(location.getWorld(), x, y, z))) {
                        manual = true;
                        break;
                    }
                }
            }
        }
        return manual;
    }

}
