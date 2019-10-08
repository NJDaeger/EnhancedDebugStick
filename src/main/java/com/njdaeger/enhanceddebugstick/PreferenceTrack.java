package com.njdaeger.enhanceddebugstick;

import com.njdaeger.bcm.types.YmlConfig;
import org.bukkit.plugin.Plugin;

public final class PreferenceTrack extends YmlConfig {

    public PreferenceTrack(Plugin plugin) {
        super(plugin, "user-preferences");
    }
}
