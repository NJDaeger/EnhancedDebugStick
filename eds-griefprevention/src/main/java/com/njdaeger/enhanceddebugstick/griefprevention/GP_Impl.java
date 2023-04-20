package com.njdaeger.enhanceddebugstick.griefprevention;

import com.njdaeger.enhanceddebugstick.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.event.ValueChangeEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class GP_Impl extends JavaPlugin implements Listener {
    
    private DataStore dataStore;
    
    @Override
    public void onEnable() {
        this.dataStore = GriefPrevention.instance.dataStore;
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void onFreezeEvent(FreezeBlockEvent e) {
        Claim claim = dataStore.getClaimAt(e.getFrozenBlock().getLocation(), false, false, null);
        if (claim != null && claim.checkPermission(e.getPlayer(), ClaimPermission.Build, e) != null) e.setCancelled(true);
    }
    
    @EventHandler
    public void onValueChangeEvent(ValueChangeEvent e) {
        Claim claim = dataStore.getClaimAt(e.getLocation(), false, false, null);
        if (claim != null && claim.checkPermission(e.getPlayer(), ClaimPermission.Build, e) != null) e.setCancelled(true);
    }
    
    @EventHandler
    public void onPageEvent(PastePropertyEvent e) {
        Claim claim = dataStore.getClaimAt(e.getBefore().getLocation(), false, false, null);
        if (claim != null && claim.checkPermission(e.getPlayer(), ClaimPermission.Build, e) != null) e.setCancelled(true);
    }
    
}
