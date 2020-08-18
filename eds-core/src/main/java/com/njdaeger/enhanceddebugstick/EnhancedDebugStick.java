package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.DebugStickAPI;
import com.njdaeger.enhanceddebugstick.mcversion.PropertyLoader;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.session.Preference;
import com.njdaeger.enhanceddebugstick.util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public final class EnhancedDebugStick extends JavaPlugin implements DebugStickAPI {

    private static EnhancedDebugStick PLUGIN;
    static ConfigKey KEYS;
    private ConfigurationFile configuration;
    private final Map<UUID, DebugSession> debugSessions = new HashMap<>();

    @Override
    public void onEnable() {
        boolean reload = false;

        if (PLUGIN != null) {
            reload = true;
            getLogger().info("Reloading...");
        } else PLUGIN = this;

        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveResource("config.yml", false);
            getLogger().info("Configuration file does not exist. Creating it.");
        }
        this.configuration = new ConfigurationFile(this);
        KEYS = new ConfigKey(this);

        if (!reload) {
            PropertyLoader.loadVersion();
            Preference.registerPreferences();
        }

        if (!reload) {
            new DebugStickCommand(this);
            new DebugListener(this);
        }

        if (KEYS.BSTATS_INTEGRATION) {//Need to wait for custom bar charts
            /*Metrics metrics = */new Metrics(this);
            /*metrics.addCustomChart(new Metrics.SimpleBarChart("used_addons", () -> {
                Map<String, Integer> values = new HashMap<>();
                values.put("CoreProtect", Bukkit.getPluginManager().getPlugin("EnhancedDebugStick-CoreProtect") != null ? 1 : 0);
                values.put("PlotSquared", Bukkit.getPluginManager().getPlugin("EnhancedDebugStick-PlotSquared") != null ? 1 : 0);
                return values;
            }));*/
            getLogger().info("BStats Metrics enabled.");
        } else getLogger().info("BStats Metrics disabled.");

        for (Player player : Bukkit.getOnlinePlayers()) {
            addDebugSession(player.getUniqueId());
        }

        if (reload) getLogger().info("Reload complete!");

    }

    @Override
    public void onDisable() {
        this.configuration = null;

        for (Player player : Bukkit.getOnlinePlayers()) {
            getDebugSession(player.getUniqueId()).pause();
        }
    }

    public static EnhancedDebugStick getInstance() {
        return PLUGIN;
    }
    
    @Override
    public DebugSession getDebugSession(UUID uuid) {
        return debugSessions.get(uuid);
    }

    @Override
    public boolean addDebugSession(UUID uuid) {
        if (hasDebugSession(uuid)) {
            getDebugSession(uuid).resume();
            return false;
        }
        debugSessions.put(uuid, new DebugSession(uuid));
        return true;
    }

    @Override
    public boolean removeDebugSession(UUID uuid) {
        if (!hasDebugSession(uuid)) return false;
        debugSessions.remove(uuid);
        return true;
    }

    @Override
    public boolean hasDebugSession(UUID uuid) {
        return debugSessions.containsKey(uuid);
    }

    @Override
    public Collection<DebugSession> getDebugSessions() {
        return debugSessions.values();
    }

    @Override
    public ConfigurationFile getDebugConfig() {
        return configuration;
    }
}


