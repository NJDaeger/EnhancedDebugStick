package com.njdaeger.enhanceddebugstick.plotsquared;

import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PS_Config extends Configuration {
    
    private final List<World> worlds = new ArrayList<>();
    private final boolean allowMembers;
    private final boolean allowTrusted;
    
    public PS_Config(Plugin plugin) {
        super(plugin, ConfigType.YML, "config");
        
        addComment("affect-worlds", "Specifcy which worlds the PlotSquared implementation will affect.");
        addEntry("affect-worlds", Arrays.asList("plot_world", "some_other_world"));
        if (getList("affect-worlds") != null) {
            getList("affect-worlds").forEach(item -> {
                World world = Bukkit.getWorld(item.toString());
                if (world != null) worlds.add(world);
            });
        }
        plugin.getLogger().info("Affected plot worlds: " + Arrays.toString(worlds.stream().map(World::getName).toArray(String[]::new)));
        addComment("allowances", "What should a player need to be in order to use the EnhancedDebugStick? Can a member be allowed to use it? Can a trusted member be allowed to use it?");
        addEntry("allowances.allow-members", true);
        addEntry("allowances.allow-trusted", true);
        
        allowMembers = getBoolean("allowances.allow-members");
        allowTrusted = getBoolean("allowances.allow-trusted");
        
        save();
    }
    
    public List<World> getWorlds() {
        return worlds;
    }
    
    public boolean affectPlotWorld(World world) {
        return worlds.stream().anyMatch(wld -> wld.equals(world));
    }
    
    public boolean allowMembers() {
        return allowMembers;
    }
    
    public boolean allowTrusted() {
        return allowTrusted;
    }
    
}
