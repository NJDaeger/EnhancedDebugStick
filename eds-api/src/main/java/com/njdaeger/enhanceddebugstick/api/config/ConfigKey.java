package com.njdaeger.enhanceddebugstick.api.config;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.api.mode.ShiftMode;
import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.SmartConfig;
import com.njdaeger.pdk.config.impl.YmlConfig;

public final class ConfigKey extends SmartConfig<YmlConfig> {

    private static EnhancedDebugStickApi instance;
    
    /**
     * Whether the user allows the sending of basic data to bstats.org
     */
    public final boolean BSTATS_INTEGRATION = get("bstats-integration", true);
    /**
     * Whether to do a sound when the stick is selected or deselected
     */
    public final boolean SOUND_ON_SELECT = get("sound-on-select", true);
    /**
     * Whether to play sounds when errors happen in game
     */
    public final boolean SOUND_ON_ERROR = get("sound-on-error", true);

    /**
     * Whether to enable users to set custom preferences
     */
    public final boolean ENABLE_PREFERENCES = get("enable-user-preferences", true);
    
    /**
     * The material for the debug stick.
     */
    public final String STICK_MATERIAL = get("stick-material", "stick");
    
    /**
     * The language file to use for all messages
     */
    public final String LANG_FILE = get("lang-file", "en_us");
    
    /**
     * Enables or disables bossbar timers
     */
    public final boolean ALLOW_BOSSBAR_TIMERS = get("allow-bossbar-timers", true);
    
    /**
     * What default mode shifting preference to use
     */
    public final ShiftMode MS_DEFAULT_SHIFT_PREFERENCE = get("mode-shift.default-mode-shift-preference", ShiftMode.DOUBLE);

    /**
     * Whether to do a sound when the mode selector is enabled or disabled.
     */
    public final boolean MS_START_STOP_SOUND = get("mode-shift.start-stop-sound", true);
    /**
     * Whether to do play a sound when the mode is changed in the mode selector.
     */
    public final boolean MS_MODE_CHANGE = get("mode-shift.mode-change-sound", true);
    /**
     * How long to wait before being able to use the mode shifter again.
     */
    public final long MS_CHANGE_COOLDOWN = get("mode-shift.mode-change-cooldown", 2000, t -> t >= 0);
    /**
     * How long to wait for a player to double sneak before the timeout is reached.
     */
    public final long DOUBLE_SNEAK_TIMEOUT = get("mode-shift.double-sneak-options.double-sneak-timeout", 750L, t -> t >= 0);
    /**
     * Minimum time to hold sneak for the HOLD preference
     */
    public final long HOLD_SNEAK_MINIMUM = get("mode-shift.hold-sneak-options.sneak-minimum", 1000, t -> t > 0);
    /**
     * Maximum time to hold sneak for the HOLD preference
     */
    public final long HOLD_SNEAK_MAXIMUM = get("mode-shift.hold-sneak-options.sneak-maximum", -1, t -> t > HOLD_SNEAK_MINIMUM);
    /**
     * Whether to display the block data when a player looks at the block
     */
    public final boolean CDM_DISPLAY_ON_LOOK = get("classic-debug-mode.display-data-on-look", true);
    /**
     * How far away to display the data of the block if display on look is enabled.
     */
    public final int CDM_DISPLAY_DISTANCE = get("classic-debug-mode.display-data-distance", 10, d -> d >= 0);
    /**
     * Whether to play a sound on property change
     */
    public final boolean CDM_PROPERTY = get("classic-debug-mode.next-property-sound", true);
    /**
     * Whether to play a sound on value change
     */
    public final boolean CDM_VALUE = get("classic-debug-mode.next-value-sound", true);
    /**
     * Whether to outline the frozen blocks with an invisible shulker
     */
    public final boolean FDM_OUTLINE = get("frozen-debug-mode.outline-selected-block", true);
    /**
     * Whether to play a sound when a block is frozen
     */
    public final boolean FDM_FREEZE = get("frozen-debug-mode.freeze-sound", true);
    /**
     * Whether to play a sound when a block is unfrozen
     */
    public final boolean FDM_UNFREEZE = get("frozen-debug-mode.unfreeze-sound", true);
    /**
     * Whether to play a sound when all currently selected blocks are unfrozen at the same time.
     */
    public final boolean FDM_UNFREEZE_ALL = get("frozen-debug-mode.mass-unfreeze-sound", true);

    public final boolean COPY_DISPLAY_ON_LOOK = get("copy-debug-mode.display-data-on-look", true);

    public final int COPY_DISPLAY_DISTANCE  = get("copy-debug-mode.display-data-distance", 10, d -> d >= 0);

    public final boolean COPY_DISPLAY_ALL = get("copy-debug-mode.display-all-properties", true);

    public final boolean COPY_COPY_SOUND = get("copy-debug-mode.copy-sound", true);

    public final boolean COPY_PASTE_SOUND = get("copy-debug-mode.paste-sound", true);

    public final boolean COPY_CLEAR_SOUND = get("copy-debug-mode.clear-sound", true);

    public ConfigKey(EnhancedDebugStickApi plugin) {
        super(plugin, ConfigType.YML, "config");
        instance = plugin;
    }

    public static ConfigKey get() {
        if (instance.getConfigKeys() == null) throw new IllegalStateException("Configuration has not been initialized.");
        else return instance.getConfigKeys();
    }

}
