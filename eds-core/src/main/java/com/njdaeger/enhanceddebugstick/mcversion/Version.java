package com.njdaeger.enhanceddebugstick.mcversion;

import org.bukkit.Bukkit;

import java.util.function.Function;

public enum Version {

    v1_13("113", "Minecraft 1.13.x", (s) -> s.contains("v1_13"), false),
    v1_14("114", "Minecraft 1.14.x", (s) -> s.contains("v1_14"), false),
    v1_15("115", "Minecraft 1.15.x", (s) -> s.contains("v1_15"), false),
    v1_16("116", "Minecraft 1.16.x", (s) -> s.contains("v1_16"), false),
    v1_17("117", "Minecraft 1.17.x", (s) -> s.contains("v1_17"), true);
//    v1_18("118", "Minecraft 1.18.x", (s) -> s.contains("v1_18"), false);
//    v1_19("119", "Minecraft 1.19.x", (s) -> s.contains("v1_19"), false);

    protected final String pkg;
    private final boolean latest;
    protected final String niceName;
    private final Function<String, Boolean> isVersion;
    
    Version(String pkg, String niceName, Function<String, Boolean> isVersion, boolean latest) {
        this.pkg = pkg;
        this.latest = latest;
        this.niceName = niceName;
        this.isVersion = isVersion;
    }
    
    private static Version getLatest() {
        for (Version version : values()) {
            if (version.latest) return version;
        }
        return null;
    }
    
    static Version getCurrentVersion() {
        String path = Bukkit.getServer().getClass().getPackage().getName();
        String versionString = path.substring(path.lastIndexOf('.') + 1);
    
        for (Version version : values()) {
            if (version.isVersion.apply(versionString))
                return version;
        }
        return getLatest();
    }
    
}
