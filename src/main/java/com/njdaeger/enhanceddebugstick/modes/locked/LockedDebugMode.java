package com.njdaeger.enhanceddebugstick.modes.locked;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class LockedDebugMode extends DebugModeType<LockedDebugMode, LockedDebugContext> {

    public LockedDebugMode() {
        super("Locked", LockedDebugMode.class);
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public DebugModeType getModeType() {
        return null;
    }

    @Override
    public boolean addSession(DebugSession session) {
        return false;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onMove(PlayerMoveEvent event) {

    }

    @Override
    public void onHeld(PlayerItemHeldEvent event) {

    }

    @Override
    public void onEntityInteract(PlayerInteractEntityEvent event) {

    }
}
