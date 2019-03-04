package com.njdaeger.enhanceddebugstick;

import com.njdaeger.bci.base.BCICommand;
import com.njdaeger.bci.defaults.CommandContext;
import com.njdaeger.bci.defaults.CommandStore;
import com.njdaeger.bci.defaults.TabContext;
import com.njdaeger.enhanceddebugstick.api.DebugStickAPI;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EnhancedDebugStick extends JavaPlugin implements DebugStickAPI {

    private Configuration configuration;
    private CoreProtectAPI coreProtectAPI;
    private final CommandStore commandStore = new CommandStore(this);
    private final Map<UUID, DebugSession> debugSessions = new HashMap<>();

    @Override
    public void onEnable() {
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) saveResource("config.yml", false);
        this.configuration = new Configuration(this);
        new DebugStickCommand(this);
        new DebugListener(this);

        if (configuration.coreprotectLogging()) {
            coreProtectAPI = initializeCoreprotect();
            if (coreProtectAPI == null) getLogger().warning("CoreProtectAPI was unable to be hooked. (Is CoreProtect installed?)");
            else getLogger().info("CoreProtectAPI was successfully hooked.");
        }
    }

    @Override
    public void onDisable() {
    }

    private CoreProtectAPI initializeCoreprotect() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");

        if (!(plugin instanceof CoreProtect)) return null;
        CoreProtectAPI coreProtect = ((CoreProtect) plugin).getAPI();

        if (!coreProtect.isEnabled() || coreProtect.APIVersion() < 6) return null;

        return coreProtect;
    }

    CoreProtectAPI getCoreProtectAPI() {
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
        if (hasDebugSession(uuid)) return false;
        debugSessions.put(uuid, new DebugSession(this, uuid));
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
    public Configuration getDebugConfig() {
        return configuration;
    }
}


