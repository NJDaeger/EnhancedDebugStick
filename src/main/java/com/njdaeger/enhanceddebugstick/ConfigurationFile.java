package com.njdaeger.enhanceddebugstick;

import com.njdaeger.bcm.types.YmlConfig;
import org.bukkit.plugin.Plugin;

public final class ConfigurationFile extends YmlConfig {

    ConfigurationFile(Plugin plugin) {
        super(plugin, "config");
    }

}
