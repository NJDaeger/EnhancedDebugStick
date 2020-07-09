package com.njdaeger.enhanceddebugstick.mcversion;

import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;

import java.lang.reflect.InvocationTargetException;

public interface PropertyLoader {
    
    static void load(String versionNumber) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<PropertyLoader> loader = (Class<PropertyLoader>)Class.forName("com.njdaeger.enhanceddebugstick.mcversion.v" + versionNumber + ".PropertyLoader_" + versionNumber);
        loader.getConstructor().newInstance().load();
    }
    
    static void loadVersion() {
        Version version = Version.getCurrentVersion();
        try {
            PropertyLoader.load(version.pkg);
            EnhancedDebugStick.getInstance().getLogger().info("Using property support for " + version.niceName);
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            EnhancedDebugStick.getInstance().getLogger().severe("Could not load property support for " + version.niceName);
            e.printStackTrace();
        }
        
    }
    
    void load();
    
}
