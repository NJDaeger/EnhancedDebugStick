package com.njdaeger.enhanceddebugstick.blacklist;

import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.Configuration;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class BL_Config extends Configuration {
    
    private List<Material> blacklist;
    
    public BL_Config(Plugin plugin) {
        super(plugin, ConfigType.YML, "config");
        
        addEntry("material-blacklist", Arrays.asList("potatoes", "carrots"));
        
        save();
    
        List<String> materialList = getStringList("material-blacklist");
        blacklist = materialList.stream().map(possible -> {
            var mat = Material.matchMaterial(possible);
            if (mat == null) return Material.AIR;
            else return mat;
        }).filter(m -> !m.isAir()).toList();
    }
    
    public List<Material> getBlacklistedMaterials() {
        return blacklist;
    }
    
    public void reloadBlacklist() {
        
        blacklist = getStringList("material-blacklist").stream().map(possible -> {
            var mat = Material.matchMaterial(possible);
            if (mat == null) return Material.AIR;
            else return mat;
        }).filter(m -> !m.isAir()).toList();
    }
    
}
