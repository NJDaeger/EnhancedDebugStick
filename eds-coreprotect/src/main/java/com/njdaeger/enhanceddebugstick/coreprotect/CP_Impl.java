package com.njdaeger.enhanceddebugstick.coreprotect;

import com.njdaeger.enhanceddebugstick.api.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.api.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.api.event.UnfreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.api.event.ValueChangeEvent;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CP_Impl extends JavaPlugin implements Listener {
    
    private CoreProtectAPI coreApi;
    private CP_Config config;
    
    @Override
    public void onEnable() {
        coreApi = initializeCoreProtect();
        if (coreApi == null) {
            getLogger().warning("Unable to find an installation of CoreProtect. CoreProtect integration will be disabled.");
        } else {
            this.config = new CP_Config(this);
            Bukkit.getPluginManager().registerEvents(this, this);
            getLogger().info("CoreProtect integration enabled.");
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    private CoreProtectAPI initializeCoreProtect() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");
        if (!(plugin instanceof CoreProtect)) return null;
        CoreProtect cp = (CoreProtect)plugin;
        if (!cp.getAPI().isEnabled() || cp.getAPI().APIVersion() < 6) return null;
        else return cp.getAPI();
    }
    
    @EventHandler
    public void onFreezeEvent(FreezeBlockEvent event) {
        if (config.doFreezeLogging()) {
            coreApi.logInteraction(event.getPlayer().getName(), event.getFrozenBlock().getLocation());
        }
    }
    
    @EventHandler
    public void onUnfreezeEvent(UnfreezeBlockEvent event) {
        if (config.doFreezeLogging()) {
            event.getUnfrozenBlocks().forEach(block -> {
                coreApi.logInteraction(event.getPlayer().getName(), block.getLocation());
            });
        }
    }
    
    @EventHandler
    public void onValueChangeEvent(ValueChangeEvent event) {
        if (config.doClassicLogging()) {
            coreApi.logRemoval(event.getPlayer().getName(), event.getBefore().getLocation(), event.getBefore().getType(), event.getBefore().getBlockData());
            coreApi.logPlacement(event.getPlayer().getName(), event.getBefore().getLocation(), event.getBefore().getType(), event.getAfter());
        }
    }

    @EventHandler
    public void onPasteEvent(PastePropertyEvent event) {
        if (config.doCopyLogging()) {
            coreApi.logRemoval(event.getPlayer().getName(), event.getBefore().getLocation(), event.getBefore().getType(), event.getBefore().getBlockData());
            coreApi.logPlacement(event.getPlayer().getName(), event.getBefore().getLocation(), event.getBefore().getType(), event.getAfter());
        }
    }
    
}
