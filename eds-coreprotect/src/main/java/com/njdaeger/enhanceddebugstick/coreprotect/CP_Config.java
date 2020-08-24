package com.njdaeger.enhanceddebugstick.coreprotect;

import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.Configuration;
import org.bukkit.plugin.Plugin;

public class CP_Config extends Configuration {
    
    private boolean classicLogging;
    private boolean copyLogging;
    private boolean freezeLogging;
    
    public CP_Config(Plugin plugin) {
        super(plugin, ConfigType.YML, "config");
        
        addComment("classic-debug-mode-logging", "This will log any changes done with the classic debug mode to CoreProtect.");
        addEntry("classic-debug-mode-logging", true);
        addComment("copy-debug-mode-logging", "This will log any changes done with the copy debug mode to CoreProtect.");
        addEntry("copy-debug-mode-logging", true);
        addComment("freeze-debug-mode-logging", "This will log any changes done with the freeze debug mode to CoreProtect.");
        addEntry("freeze-debug-mode-logging", true);
        
        this.classicLogging = getBoolean("classic-debug-mode-logging");
        this.copyLogging = getBoolean("copy-debug-mode-logging");
        this.freezeLogging = getBoolean("freeze-debug-mode-logging");
        
        save();
        
    }
    
    public boolean doClassicLogging() {
        return classicLogging;
    }
    
    public boolean doCopyLogging() {
        return copyLogging;
    }
    
    public boolean doFreezeLogging() {
        return freezeLogging;
    }
    
}
