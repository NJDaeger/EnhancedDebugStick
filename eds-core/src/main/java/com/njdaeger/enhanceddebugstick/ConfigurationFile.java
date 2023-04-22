package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.mode.ShiftMode;
import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.Configuration;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public final class ConfigurationFile extends Configuration {

    ConfigurationFile(Plugin plugin) {
        super(plugin, ConfigType.YML, "config");

        addEntry("bstats-integration", true);
        addComment("bstats-integration",
                "Enable this if you want EnhancedDebugStick to send basic information to bstats.org.\n" +
                        "Data is viewable here: https://bstats.org/plugin/bukkit/EnhancedDebugStick");

        addEntry("sound-on-select", true);
        addComment("sound-on-select",
                "\nWhen the debug stick is selected or deselected in the players hotbar, a light sound will be played to the player indicating the debug stick is being held or not.");

        addEntry("sound-on-error", true);
        addComment("sound-on-error",
                "\nWhen the player doesn't do something correct with the plugin in game, they will be notified with a sound and a message telling them what they did wrong. If this is disabled, the message will still send, just not the sound.");

        addEntry("enable-user-preferences", true);
        addComment("enable-user-preferences",
                "\nAllow users to set personal settings for themselves without changing this settings file. They would be allowed to modify the following:\n" +
                        "  Mode Shifter             - Set a custom shift mode other than the default preference\n" +
                        "  *Double Sneak timeout    - How fast the user must be able to shift to enable/disable the mode shifter\n" +
                        "  Mode Change Cooldown     - How long the user must wait to change modes again\n" +
                        "  **Sneak Minimum          - Least amount of time the user can sneak to enable/disable the mode shifter\n" +
                        "  **Sneak Maximum          - Most amount of time the user can sneak to still enable/disable the mode shifter\n\n" +
                        "* The DOUBLE mode shifter must be selected to be an option\n" +
                        "** The HOLD mode shifter must be selected to be an option");

        addEntry("stick-material", Material.STICK.name().toLowerCase());
        addComment("stick-material", "\nWhat material should the debug stick be? Defaults to a minecraft stick, but can be changed in case of plugin clashes.");
        
        addEntry("allow-bossbar-timers", true);
        addComment("allow-bossbar-timers", "\nBossbars are used to show how quickly you need to shift to move to the next mode. With them enabled, it may override and affect some other custom bossbars that are currently in use. When this is set to true, the Enhanced Debug Stick WILL show bossbars that show the time left to do certain actions. Otherwise, no bossbar will be shown.");
        
        addEntry("mode-shift.default-mode-shift-preference", ShiftMode.DOUBLE);
        addComment("mode-shift", "\n\nSettings for when a player wants to change between debug modes.\n\n");
        addComment("mode-shift.default-mode-shift-preference",
                "\nBy default, how should users be changing modes? Possible options are:\n" +
                        "  COMMAND  - If set to command, the user will need to do /dbs <newMode> to change modes\n" +
                        "  DOUBLE   - If set to double, the user will need to double sneak to enable/disable their mode shifter\n" +
                        "  HOLD     - If set to hold, the user will need to be holding sneak and not moving to enable/disable the shifter\n" +
                        "\nNote: users can set their preferences if enabled (see 'enable-user-preferences')\n" +
                        "If this node doesn't exist, or if the value set is invalid (misspelled/nonexistent), the default will be DOUBLE");

        addEntry("mode-shift.start-stop-sound", true);
        addComment("mode-shift.start-stop-sound", "\nWhen enabling and disabling the mode shifter, should a sound be played? (only affects DOUBLE and HOLD shifters)");

        addEntry("mode-shift.mode-change-sound", true);
        addComment("mode-shift.mode-change-sound", "\nWhen going to the next mode, should a sound be played? (only affects DOUBLE and HOLD shifters)");

        addEntry("mode-shift.mode-change-cooldown", 2000);
        addComment("mode-shift.mode-change-cooldown", "\nHow long (in milliseconds... 1000 milliseconds = 1 second) should the user wait before being able to change to a" +
                "different debug mode? In a nutshell, after you disable the mode shifter, by default, 2 seconds can go by where you can spam shift and the mode shifter will not pop up.");

        addEntry("mode-shift.double-sneak-options.double-sneak-timeout", 750);
        addComment("mode-shift.double-sneak-options", "\nThese options only take effect if the user's (or server if user preferences are disabled) mode shift preference is DOUBLE.");
        addComment("mode-shift.double-sneak-options.double-sneak-timeout",
                "\nWhen shifting between modes, how quickly (in milliseconds... 1000 milliseconds = 1 second) must a player double sneak in order for it to be detected as them " +
                        "wanting to change debug modes? Typically anything lower than 500ms is unusable.");

        addEntry("mode-shift.hold-sneak-options.sneak-minimum", 1000);
        addComment("mode-shift.hold-sneak-options", "\nThese options only take effect if the user's (or server if the user preferences are disabled) mode shift preference is HOLD.\n");
        addComment("mode-shift.hold-sneak-options.sneak-minimum",
                "\nWhen attempting to switch modes, what is the minimum amount of time (in milliseconds... 1000 milliseconds = 1 second) a player needs to hold sneak in order to enable" +
                        "/disable the mode shifter? Default is 1000. If this is less than or equal to 0 the value will go to the default");

        addEntry("mode-shift.hold-sneak-options.sneak-maximum", 2000);
        addComment("mode-shift.hold-sneak-options.sneak-maximum",
                "\nWhen attempting to switch modes, what is the maximum amount of time (in milliseconds... 1000 milliseconds = 1 second) a player can hold sneak and still enable/disable " +
                        "the mode shifter? Default is -1 for no time maximum. If this is less than or equal to the shift-minimum value, there will then be no maximum set. So, if the player were to " +
                        "hold shift any amount of time after the shift-minimum time requirement is met- the mode shifter will be enabled/disabled.");

        addEntry("classic-debug-mode.display-data-on-look", true);
        addComment("classic-debug-mode", "\nSettings for the classic debug mode. This mode acts like the standard debug stick\n");
        addComment("classic-debug-mode.display-data-on-look", "\nWhen looking at a block within a specified distance, should the block properties be displayed to the player?");

        addEntry("classic-debug-mode.display-data-distance", 7);
        addComment("classic-debug-mode.display-data-distance",
                "\nIf display-data-on-look is enabled, this will limit how far away block data can be seen.\n" +
                "If this number is set to 0, the data will not show (equivalent to setting the display-data-on-look to false). If it is set to -1, the limit will default to 10 blocks.");

        addEntry("classic-debug-mode.next-property-sound", true);
        addComment("classic-debug-mode.next-property-sound",
                "\nWhen the next property is selected (left click) a click will be played to the player indicating the next property has been chosen.");

        addEntry("classic-debug-mode.next-value-sound", true);
        addComment("classic-debug-mode.next-value-sound",
                "\nWhen the next value is selected (right click) a click will be played to the player indicating the next value for the selected property has been chosen.");

        addEntry("frozen-debug-mode.outline-selected-block", true);
        addComment("frozen-debug-mode",
                "\nSettings for the frozen debug mode. This mode allows the locking of blocks so their properties wont update when a block is placed nearby\n");
        addComment("frozen-debug-mode.outline-selected-block",
                "\nOutlines the selected block to furthermore express that the block is frozen. This outline will only be shown to whoever is locking the specific block");

        addEntry("frozen-debug-mode.freeze-sound", true);
        addComment("frozen-debug-mode.freeze-sound", "\nWhen a block is frozen, play a sound to indicate it being frozen");

        addEntry("frozen-debug-mode.unfreeze-sound", true);
        addComment("frozen-debug-mode.unfreeze-sound", "\nWhen a block is unfrozen, play a sound to indicate it being unfrozen.");

        addEntry("frozen-debug-mode.mass-unfreeze-sound", true);
        addComment("frozen-debug-mode.mass-unfreeze-sound", "\nWhen all frozen blocks are unfrozen at the same time, play a sound to indicate it.");

        addEntry("copy-debug-mode.display-data-on-look", true);
        addComment("copy-debug-mode",
                "\nSettings for the copy debug mode. This mode allows the copying and pasting of blockstates (not blocks!) so the common properties of the current copied " +
                        "block can be applied to the common properties (which have mismatching values) of another block. For example, you have a slab: \"stone_slab[type=upper,wate" +
                        "rlogged=true]\" and you want to copy the properties of that block and paste them on another block of similar properties. You copy the slab, and then paste " +
                        "it on a stair block which is NOT waterlogged- in this case, the stair block would become waterlogged and nothing else about it would change.\n");
        addComment("copy-debug-mode.display-data-on-look", "\nWhen looking at a block within a specified distance, should the block properties be displayed to the player?");

        addEntry("copy-debug-mode.display-data-distance", 7);
        addComment("copy-debug-mode.display-data-distance",
                "\nIf display-data-on-look is enabled, this will limit how far away block data can be seen. If this number is set to 0, the data will not show (equivalent to " +
                        "setting the display-data-on-look to false). If it is set to -1, the limit will default to 10 blocks.");

        addEntry("copy-debug-mode.display-all-properties", true);
        addComment("copy-debug-mode.display-all-properties",
                "\nWhen looking at a block to paste properties on, should we always display all the properties of that block or should we only display the applicable properties?");

        addEntry("copy-debug-mode.copy-sound", true);
        addComment("copy-debug-mode.copy-sound", "\nWhen BlockData is copied (left click) a sound will be played to the player indicating the data was copied.");

        addEntry("copy-debug-mode.paste-sound", true);
        addComment("copy-debug-mode.paste-sound", "\nWhen BlockData is pasted (right click) a sound will be played to the player indicating the data was pasted.");

        addEntry("copy-debug-mode.clear-sound", true);
        addComment("copy-debug-mode.clear-sound", "\nWhen the clipblard is cleared (left click air) a sound will be played to the player indicating the clipboard was cleared.");

        save();

    }

}
