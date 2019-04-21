package com.njdaeger.enhanceddebugstick;

import com.njdaeger.btu.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class DebugListener implements Listener {

    private final EnhancedDebugStick plugin;
    private final Configuration config;

    DebugListener(EnhancedDebugStick plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.config = plugin.getDebugConfig();
    }

    @EventHandler
    public void onReload(ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.RELOAD) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                plugin.addDebugSession(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        if (session == null) plugin.addDebugSession(event.getPlayer().getUniqueId());
        else session.resume();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());
        if (session != null) session.pause();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        DebugSession session = plugin.getDebugSession(player.getUniqueId());
        if (session.isHoldingDebugStick() && player.isSneaking() && event.getHand() == EquipmentSlot.HAND && player.getVelocity().length() == 0) {
            switch (event.getAction()) {
                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    System.out.println("Next Mode");
                    break;
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    System.out.println("Previous Mode");
                    break;
            }
            return;
        }
    }

    @EventHandler
    public void onLook(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        DebugSession session = plugin.getDebugSession(player.getUniqueId());
        if (session.isHoldingDebugStick() && player.isSneaking()) {
            ActionBar.of("CLASSIC    FREEZE    MERGE CLASSIC    MERGE ENHANCED    MULTI").sendTo(player);
            return;
        }
        session.getDebugMode().onMove(event);
    }
}
