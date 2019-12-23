package com.njdaeger.enhanceddebugstick;

import com.github.intellectualsites.plotsquared.api.PlotAPI;
import com.njdaeger.bci.base.BCICommand;
import com.njdaeger.bci.defaults.CommandContext;
import com.njdaeger.bci.defaults.CommandStore;
import com.njdaeger.bci.defaults.TabContext;
import com.njdaeger.enhanceddebugstick.api.DebugStickAPI;
import com.njdaeger.enhanceddebugstick.api.Property;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.session.Preference;
import com.njdaeger.enhanceddebugstick.util.Metrics;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EnhancedDebugStick extends JavaPlugin implements DebugStickAPI {

    private static EnhancedDebugStick PLUGIN;
    ConfigurationFile configuration;
    private CoreProtectAPI coreProtectAPI;
    private PlotAPI plotAPI;
    private final CommandStore commandStore = new CommandStore(this);
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

        if (!reload) {
            Property.registerProperties();
            Preference.registerPreferences();
        }

        if (!reload) {
            new DebugStickCommand(this);
            new DebugListener(this);
        }

        if (ConfigKey.PROTECT_INTEGRATION) {
            coreProtectAPI = initializeCoreprotect();
            if (coreProtectAPI == null) getLogger().warning("CoreProtect integration was unable to be enabled. (Is CoreProtect installed?)");
            else getLogger().info("CoreProtect integration successfully enabled.");
        } else getLogger().info("CoreProtect integration disabled.");

        if (ConfigKey.PLOT_INTEGRATION) {
            plotAPI = initializePlotSquared();
            if (plotAPI == null) getLogger().warning("PlotSquaredAPI integration was unable to be enabled. (Is PlotSquared installed?)");
            else getLogger().info("PlotSquared integration successfully enabled.");
        } else getLogger().info("PlotSquared integration disabled.");

        if (ConfigKey.BSTATS_INTEGRATION) {
            new Metrics(this);
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
        this.coreProtectAPI = null;

        for (Player player : Bukkit.getOnlinePlayers()) {
            getDebugSession(player.getUniqueId()).pause();
        }
    }

    public static EnhancedDebugStick getInstance() {
        return PLUGIN;
    }

    private PlotAPI initializePlotSquared() {
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") == null) return null;
        else return new PlotAPI();
    }

    public PlotAPI getPlotAPI() {
        return plotAPI;
    }

    private CoreProtectAPI initializeCoreprotect() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");

        if (!(plugin instanceof CoreProtect)) return null;
        CoreProtectAPI coreProtect = ((CoreProtect) plugin).getAPI();

        if (!coreProtect.isEnabled() || coreProtect.APIVersion() < 6) return null;

        return coreProtect;
    }

    public CoreProtectAPI getCoreProtectAPI() {
        return coreProtectAPI;
    }

    void registerCommand(BCICommand<CommandContext, TabContext> command) {
        commandStore.registerCommand(command);
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


