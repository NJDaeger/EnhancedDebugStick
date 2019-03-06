package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.PropertyChangeEvent;
import com.njdaeger.enhanceddebugstick.api.Result;
import com.njdaeger.enhanceddebugstick.api.ValueChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.DEBUG_STICK;
import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.hasDebugStick;

public final class DebugListener implements Listener {

    private final EnhancedDebugStick plugin;
    private final Configuration config;

    DebugListener(EnhancedDebugStick plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.config = plugin.getDebugConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.getDebugSession(event.getPlayer().getUniqueId()) == null) plugin.addDebugSession(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

    }

    @EventHandler
    public void onLook(PlayerMoveEvent event) {

        //
        //Check if the configuration allows data viewing
        //
        if (config.displayDataOnLook()) {
            Player player = event.getPlayer();
            DebugSession session = plugin.getDebugSession(player.getUniqueId());
            if (hasDebugStick(player) && DEBUG_STICK.equals(player.getInventory().getItemInMainHand()) && player.hasPermission("enhanceddebugstick.debugstick.use")) {
                RayTraceResult hit = player.rayTraceBlocks(config.displayDataDistance());
                if (hit != null && hit.getHitBlock() != null) {
                    session.sendPropertiesOf(hit.getHitBlock());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryChange(PlayerItemHeldEvent event) {

        if (config.soundOnSelect()) {
            Player player = event.getPlayer();
            ItemStack oldSlot = player.getInventory().getItem(event.getPreviousSlot());
            ItemStack newSlot = player.getInventory().getItem(event.getNewSlot());

            if (DEBUG_STICK.equals(oldSlot)) {
                player.playSound(player.getLocation(), Sound.UI_TOAST_OUT, 1, 1);
            }
            if (DEBUG_STICK.equals(newSlot)) {
                player.playSound(player.getLocation(), Sound.UI_TOAST_IN, 1, 1);
            }
        }
    }

    @EventHandler
    public void onReload(ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.RELOAD) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                plugin.addDebugSession(player.getUniqueId());
            }
        }
    }

}
