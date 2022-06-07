package com.njdaeger.enhanceddebugstick.mcversion.v118;

import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import com.njdaeger.enhanceddebugstick.api.DebugStickAPI;
import com.njdaeger.enhanceddebugstick.mcversion.PropertyLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class PropertyLoader_118 implements PropertyLoader, Listener {
    
    
    @Override
    public void load() {
        Property_118.registerProperties();
        Bukkit.getPluginManager().registerEvents(this, EnhancedDebugStick.getInstance());
    }
    
    @EventHandler
    public void respawnExplosion(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        
        if (block != null && e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
            if (DebugStickAPI.DEBUG_STICK.equals(e.getPlayer().getInventory().getItemInMainHand())) e.setCancelled(true);
        }
    }
}
