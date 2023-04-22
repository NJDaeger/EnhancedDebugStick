package com.njdaeger.enhanceddebugstick.worldguard;

import com.njdaeger.enhanceddebugstick.api.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.api.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.api.event.ValueChangeEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class WG_Impl extends JavaPlugin implements Listener {
    
    @Override
    public void onLoad() {
        
    }
    
    private RegionContainer regionContainer;
    
    @Override
    public void onEnable() {
//        config = new WG_Config(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        this.regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    private boolean canUse(Player player, Location location) {
        LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionQuery query = regionContainer.createQuery();
        boolean canBypass = WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(lp, lp.getWorld());
        return (query.testState(BukkitAdapter.adapt(location), lp, Flags.BLOCK_BREAK) && query.testState(BukkitAdapter.adapt(location), lp, Flags.BLOCK_PLACE)) || query.testState(BukkitAdapter.adapt(location), lp, Flags.BUILD) || canBypass;
    }
    
    @EventHandler
    public void onFreezeEvent(FreezeBlockEvent event) {
        if (!canUse(event.getPlayer(), event.getFrozenBlock().getLocation())) {
            event.setCancelled(true);
            event.getDebugContext().getDebugSession().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You must have 'block_break' *and* 'block_place' OR 'build' flag permissions to freeze a block here.");
        }
        
    }
    
    @EventHandler
    public void onValueChangeEvent(ValueChangeEvent event) {
        if (!canUse(event.getPlayer(), event.getLocation())) {
            event.setCancelled(true);
            event.getDebugContext().getDebugSession().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You must have 'block_break' *and* 'block_place' OR 'build' permissions to change a block's property here.");
        }
    }
    
    @EventHandler
    public void onPasteEvent(PastePropertyEvent event) {
        if (!canUse(event.getPlayer(), event.getBefore().getLocation())) {
            event.setCancelled(true);
            event.getDebugContext().getDebugSession().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You must have 'block_break' *and* 'block_place' OR 'build' flag permissions to paste properties here.");
        }
    }
}
