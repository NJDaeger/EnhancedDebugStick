package com.njdaeger.enhanceddebugstick.mcversion;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

public interface PropertyLoader {
    
    static void load(String versionNumber, EnhancedDebugStickApi plugin) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<PropertyLoader> loader = (Class<PropertyLoader>)Class.forName("com.njdaeger.enhanceddebugstick.mcversion.v" + versionNumber + ".PropertyLoader_" + versionNumber);
        loader.getConstructor().newInstance().load(plugin);
    }
    
    static void loadVersion(EnhancedDebugStickApi plugin) {
        Version version = Version.getCurrentVersion();
        try {
            PropertyLoader.load(version.pkg, plugin);
            Bukkit.getLogger().info("Using property support for " + version.niceName);
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            Bukkit.getLogger().severe("Could not load property support for " + version.niceName);
            e.printStackTrace();
        }
        
    }
    
    void load(EnhancedDebugStickApi plugin);
    
}
