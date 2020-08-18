package com.njdaeger.enhanceddebugstick.plotsquared;

import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.Configuration;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PS_Config extends Configuration {
    
    private final List<World> worlds = new ArrayList<>();
    
    public PS_Config(Plugin plugin) {
        super(plugin, ConfigType.YML, "config");
        
        addComment("affect-worlds", "Specifcy which worlds the PlotSquared implementation will affect.");
        addEntry("affect-worlds", Arrays.asList("plot_world", "some_other_world"));
    }
    
    public List<World> getWorlds() {
        return worlds;
    }
    
    public boolean affectPlotWorld(World world) {
        return worlds.stream().anyMatch(wld -> wld.equals(world));
    }
    
}
