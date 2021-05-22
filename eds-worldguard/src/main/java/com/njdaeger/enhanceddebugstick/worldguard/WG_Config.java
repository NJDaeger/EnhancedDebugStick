package com.njdaeger.enhanceddebugstick.worldguard;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.Configuration;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WG_Config extends Configuration {
    
    private final List<Material> blacklistedMaterials = new ArrayList<>();
    private final List<String> blacklistedProperties = new ArrayList<>();
    
    private final boolean ownerBypass;
    
    public WG_Config(Plugin plugin) {
        super(plugin, ConfigType.YML, "config");
        addComment("blacklist", "A list of properties and blocks that shouldn't be allowed to be changed with the debug stick. \nProperties are to be listed as [property_name] and materials are to be listed as material_name. ");
        addEntry("blacklist", Arrays.asList("[age]", "[honey_level]", "[bite_counter]", "cauldron", "farmland", "[pickles]"));
        
        addComment("owner-blacklist-bypass","Do we want to allow owners to bypass the blacklist?");
        addEntry("owner-blacklist-bypass", false);
        
        for (String item : getStringList("blacklist")) {
            if (item.startsWith("[")) { //Parse it as a property
                blacklistedProperties.add(item.substring(1, item.length()-1).toLowerCase());
            } else {
                Material mat = Material.matchMaterial(item.toLowerCase());
                if (mat == null) plugin.getLogger().warning("Unknown material: " + item + ". This will not be blacklisted!");
                else blacklistedMaterials.add(mat);
            }
        }
        
        ownerBypass = getBoolean("owner-blacklist-bypass");
        
    }
 
    public boolean ownerBypass() {
        return ownerBypass;
    }
    
    public List<String> getBlacklistedProperties() {
        return blacklistedProperties;
    }
    
    public List<Material> getBlacklistedMaterials() {
        return blacklistedMaterials;
    }
}
