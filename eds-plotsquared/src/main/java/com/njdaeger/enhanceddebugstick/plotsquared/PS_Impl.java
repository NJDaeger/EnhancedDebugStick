package com.njdaeger.enhanceddebugstick.plotsquared;

import com.njdaeger.enhanceddebugstick.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.event.ValueChangeEvent;
import com.plotsquared.core.api.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PS_Impl extends JavaPlugin implements Listener {
    
    private PlotAPI plotApi;
    private PS_Config config;
    
    @Override
    public void onEnable() {
        plotApi = initializePlotSquared();
        if (plotApi == null) {
            getLogger().warning("Unable to find an installation of PlotSquared. PlotSquared integration will be disabled.");
        } else {
            this.config = new PS_Config(this);
            Bukkit.getPluginManager().registerEvents(this, this);
            getLogger().info("PlotSquared integration enabled.");
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    private PlotAPI initializePlotSquared() {
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") == null) return null;
        else return new PlotAPI();
    }
    
    @EventHandler
    public void onFreezeEvent(FreezeBlockEvent event) {
        if (!canUse(event.getPlayer(), event.getFrozenBlock())) {
            event.setCancelled(true);
            event.getDebugContext().getDebugSession().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You are not an owner or a member of the location being frozen.");
        }
    }
    //The player should be able to unfreeze blocks not in their selection
    
    @EventHandler
    public void onValueChangeEvent(ValueChangeEvent event) {
        if (!canUse(event.getPlayer(), event.getBefore())) {
            event.setCancelled(true);
            event.getDebugContext().getDebugSession().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You are not an owner or a member of the location being changed.");
        }
    }
    
    @EventHandler
    public void onPasteEvent(PastePropertyEvent event) {
        if (!canUse(event.getPlayer(), event.getBefore())) {
            event.setCancelled(true);
            event.getDebugContext().getDebugSession().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You are not an owner or a member of the location pasted at.");
        }
    }
    
    private boolean canUse(Player player, Block block) {
        if (!config.affectPlotWorld(block.getWorld())) {
            return true;
        }
        Location location = new Location(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
        PlotArea area = plotApi.getPlotSquared().getApplicablePlotArea(location);
        if (area == null) {
            return false;
        }
        Plot plot = area.getOwnedPlot(location);
        if (plot == null) {
            return false;
        }
        if (plot.isDenied(player.getUniqueId())) {
            return false;
        }
        /*
        First, we check if the player is an owner of the plot- if they are, we automatically allow them to use the debug stick.
        If they are not an owner, we check the configuration to see if we allow members to use it on the plot, then we check if the player is a member- if they are, they can use the debug stick.
        Last, if they are not an owner or a member, we check if trusted members are allowed to use the debug stick- if they are, we check if the player is a trusted player.
         */
        return plot.isOwner(player.getUniqueId()) || config.allowMembers() && plot.getMembers().contains(player.getUniqueId()) || config.allowTrusted() && plot.getTrusted().contains(player.getUniqueId());
        
    }
}
