package com.njdaeger.enhanceddebugstick.mcversion.v121;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.mcversion.PropertyLoader;
import com.njdaeger.enhanceddebugstick.mcversion.v120.Property_121;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PropertyLoader_121 implements PropertyLoader, Listener {
    
    
    @Override
    public void load(EnhancedDebugStickApi plugin) {
        Property_121.registerProperties();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void respawnExplosion(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        
        if (block != null && e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
            if (EnhancedDebugStickApi.DEBUG_STICK.equals(e.getPlayer().getInventory().getItemInMainHand())) e.setCancelled(true);
        }
    }
    
}
