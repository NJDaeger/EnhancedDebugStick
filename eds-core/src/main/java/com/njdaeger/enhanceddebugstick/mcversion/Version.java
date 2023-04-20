package com.njdaeger.enhanceddebugstick.mcversion;

import org.bukkit.Bukkit;

import java.util.function.Function;

public enum Version {

    v1_13("113", "Minecraft 1.13.x", (s) -> s.contains("v1_13"), false, 0),
    v1_14("114", "Minecraft 1.14.x", (s) -> s.contains("v1_14"), false, 1),
    v1_15("115", "Minecraft 1.15.x", (s) -> s.contains("v1_15"), false, 2),
    v1_16("116", "Minecraft 1.16.x", (s) -> s.contains("v1_16"), false, 3),
    v1_17("117", "Minecraft 1.17.x", (s) -> s.contains("v1_17"), false, 4),
    v1_18("118", "Minecraft 1.18.x", (s) -> s.contains("v1_18"), false, 5),
    v1_19_4("119", "Minecraft 1.19.4", (s) -> s.contains("v1_19_R3"), true, 8),
    v1_19_3("119", "Minecraft 1.19.3", (s) -> s.contains("v1_19_R2"), false, 7),
    v1_19("119", "Minecraft 1.19.x", (s) -> s.contains("v1_19"), false, 6);
    protected final String pkg;
    private final boolean latest;
    protected final String niceName;
    private final Function<String, Boolean> isVersion;
    private final int number;
    
    Version(String pkg, String niceName, Function<String, Boolean> isVersion, boolean latest, int number) {
        this.pkg = pkg;
        this.latest = latest;
        this.number = number;
        this.niceName = niceName;
        this.isVersion = isVersion;
    }
    
    private static Version getLatest() {
        for (Version version : values()) {
            if (version.latest) return version;
        }
        return null;
    }
    
    public int getOrdinal() {
        return number;
    }
    
    public static Version getCurrentVersion() {
        String path = Bukkit.getServer().getClass().getPackage().getName();
        String versionString = path.substring(path.lastIndexOf('.') + 1);
    
        for (Version version : values()) {
            if (version.isVersion.apply(versionString))
                return version;
        }
        return getLatest();
    }
    
}
