package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.ShiftMode;
import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.SmartConfig;
import com.njdaeger.pdk.config.impl.YmlConfig;
import org.bukkit.Bukkit;

public final class ConfigKey extends SmartConfig<YmlConfig> {

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

    public final boolean PLOT_INTEGRATION = get("plotsquared-integration.enable", false, b -> !b || Bukkit.getPluginManager().getPlugin("PlotSquared") != null);

    public final boolean PROTECT_INTEGRATION = get("coreprotect-integration.enable", false, b -> !b || Bukkit.getPluginManager().getPlugin("CoreProtect") != null);

    /**
     * Whether to log all changed blocks with coreprotect
     */
    public final boolean CLASSIC_LOGGING = get("coreprotect-integration.classic-debug-mode-logging", true);

    public final boolean COPY_LOGGING = get("coreprotect-integration.copy-debug-mode-logging", true);

    public final boolean FREEZE_LOGGING = get("coreprotect-integration.freeze-debug-mode-logging", true);

    ConfigKey(EnhancedDebugStick plugin) {
        super(plugin, ConfigType.YML, "config");
    }

    public static ConfigKey get() {
        if (EnhancedDebugStick.KEYS == null) throw new IllegalStateException("Configuration has not been initialized.");
        else return EnhancedDebugStick.KEYS;
    }

}
