package com.njdaeger.enhanceddebugstick.blacklist;

import com.njdaeger.enhanceddebugstick.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.event.ValueChangeEvent;
import com.njdaeger.pdk.command.CommandBuilder;
import com.njdaeger.pdk.command.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BL_Impl extends JavaPlugin implements Listener {
    
    private BL_Config config;
    
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.config = new BL_Config(this);
    
        CommandBuilder.of("blacklist", "edsblacklist")
            .executor(this::reloadCommand)
            .completer((tc) -> tc.completionAt(0, "reload"))
            .permissions("enhanceddebugstick.blacklist.reload")
            .usage("/blacklist reload")
            .description("Reloads the material blacklist")
            .register(this);
    }
    
    private void reloadCommand(CommandContext context) {
        config.reloadBlacklist();
        context.send(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "EDS" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Reloaded the material blacklist");
    }
    
    @EventHandler
    public void onFreezeEvent(FreezeBlockEvent e) {
        if (config.getBlacklistedMaterials().contains(e.getFrozenBlock().getType())) {
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot freeze a blacklisted material.");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onValueChangeEvent(ValueChangeEvent e) {
        if (config.getBlacklistedMaterials().contains(e.getBefore().getType())) {
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot modify a blacklisted material.");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPasteEvent(PastePropertyEvent e) {
        if (config.getBlacklistedMaterials().contains(e.getBefore().getType()) || config.getBlacklistedMaterials().contains(e.getAfter().getMaterial())) {
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot paste a blacklisted material.");
            e.setCancelled(true);
        }
    }
    
}
