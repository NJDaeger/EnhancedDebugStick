package com.njdaeger.enhanceddebugstick.mcversion;

import org.bukkit.Bukkit;

public enum Version {

    vUNKNOWN(-1, "unknown", "NOT SUPPORTED", "not supported"),
    v1_21_5(0, "1215","Minecraft 1.21.5", "1.21.5"),
    v1_21_6(1, "1216", "Minecraft 1.21.6 - 1.21.8", "1.21.6", "1.21.7", "1.21.8");

    private final String packageName;
    private final String messageString;
    private final String[] versions;
    private final int versionOrder;

    Version(int versionOrder, String packageName, String messageString, String... safeVersions) {
        this.versions = safeVersions;
        this.versionOrder = versionOrder;
        this.messageString = messageString;
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isCurrentVersion() {
        String currentVersion = Bukkit.getServer().getMinecraftVersion();
        for (String version : versions) {
            if (currentVersion.equalsIgnoreCase(version)) {
                return true;
            }
        }
        return false;
    }

    public String getMessageString() {
        return messageString;
    }

    public String[] getVersions() {
        return versions;
    }

    public int getVersionOrder() {
        return versionOrder;
    }

    public static Version getCurrentVersion() {
        String currentVersion = Bukkit.getServer().getMinecraftVersion();
        for (Version version : Version.values()) {
            for (String versionName : version.getVersions()) {
                if (currentVersion.equalsIgnoreCase(versionName)) {
                    return version;
                }
            }
        }
        return vUNKNOWN;
    }

    public static Version getVersion(String versionString) {
        for (Version version : Version.values()) {
            for (String versionName : version.getVersions()) {
                if (versionName.equalsIgnoreCase(versionString)) {
                    return version;
                }
            }
        }
        return vUNKNOWN;
    }

    public static boolean isServerVersionSupported() {
        var currentVersion = Bukkit.getServer().getMinecraftVersion();
        for (var version : Version.values()) {
            for (var versionName : version.getVersions()) {
                if (versionName.equalsIgnoreCase(currentVersion)) {
                    return true;
                }
            }
        }
        return false;
    }

}
