package com.njdaeger.enhanceddebugstick;

import com.njdaeger.bcm.types.YmlConfig;
import org.bukkit.plugin.Plugin;

public final class Configuration extends YmlConfig {

    private final boolean displayOnLook;
    private final int displayDistance;
    private final boolean soundOnSelect;
    private final boolean soundOnProperty;
    private final boolean soundOnValue;
    private final boolean coreprotectLogging;
    private final boolean bstatsIntegration;

    Configuration(Plugin plugin) {
        super(plugin, "config");

        addEntry("display-data-on-look", true);
        addEntry("display-data-distance", 7);
        addEntry("sound.on-select", true);
        addEntry("sound.on-next-property", true);
        addEntry("sound.on-next-value", true);
        addEntry("coreprotect-logging", false);
        addEntry("bstats-integration", true);

        this.displayOnLook = getBoolean("display-data-on-look");
        this.displayDistance = getInt("display-data-distance");
        this.soundOnSelect = getBoolean("sound.on-select");
        this.soundOnProperty = getBoolean("sound.on-next-property");
        this.soundOnValue = getBoolean("sound.on-next-value");
        this.coreprotectLogging = getBoolean("coreprotect-logging");
        this.bstatsIntegration = getBoolean("bstats-integration");
    }

    /**
     * Whether to display block properties of a block whenever a player looks at a block.
     */
    public boolean displayDataOnLook() {
        return displayOnLook;
    }

    /**
     * How far away to display the block properties to a player.
     */
    public int displayDataDistance() {
        return displayDistance;
    }

    /**
     * Whether to play a sound whenever the debug stick is selected or deselected.
     */
    public boolean soundOnSelect() {
        return soundOnSelect;
    }

    /**
     * Whether to play a sound whenever the next property is selected
     */
    public boolean soundOnNextProperty() {
        return soundOnProperty;
    }

    /**
     * Whether to play a sound whenever the next value for the current property is selected.
     */
    public boolean soundOnNextValue() {
        return soundOnValue;
    }

    /**
     * Whether coreportect logging is enabled or not.
     */
    public boolean coreprotectLogging() {
        return coreprotectLogging;
    }

    /**
     * Whether the user allows the sending of basic data to bstats.org
     */
    public boolean bstatsIntegration() {
        return bstatsIntegration;
    }

}
