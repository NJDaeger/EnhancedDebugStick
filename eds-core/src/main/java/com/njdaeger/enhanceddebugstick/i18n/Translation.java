package com.njdaeger.enhanceddebugstick.i18n;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.session.Preference;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;


public class Translation<V extends AbstractVariable> {
    
    private final String key;
    private final V translator;
    
    /**
     * @param key The translation key
     * @param translator The translator for this key
     */
    public Translation(String key, V translator) {
        this.key = key;
        this.translator = translator;
    }
    
    public Translation(String key) {
        this.key = key;
        this.translator = (V)new Var0();
    }
    
    public V get() {
        return translator;
    }
    
    String getKey() {
        return key;
    }
    
    public static void load() {
        Bukkit.getLogger().info("Loading translations...");
    }
    
    public static final Translation<Var0> CLASSIC_NO_PERM = new Translation<>("classicDebugMode.noPermission");
    public static final Translation<Var1<Block>> CLASSIC_NO_PROPS = new Translation<>("classicDebugMode.noProperties", new Var1<>((b) -> uppercaseFirst(b.getType().name()), "block"));
    public static final Translation<Var2<IProperty<?, ?>, Block>> CLASSIC_NOT_APPL = new Translation<>("classicDebugMode.notApplicable", new Var2<>(IProperty::getNiceName, (b) -> uppercaseFirst(b.getType().name()), "property", "block"));
    
    public static final Translation<Var0> COPY_NO_PERM = new Translation<>("copyDebugMode.noPermission");
    public static final Translation<Var0> COPY_EMPTY_CLIPBOARD = new Translation<>("copyDebugMode.emptyClipboard");
    public static final Translation<Var1<Block>> COPY_NO_PROPS = new Translation<>("copyDebugMode.noProperties", new Var1<>((b) -> uppercaseFirst(b.getType().name()), "block"));
    public static final Translation<Var0> COPY_CLEARED_CLIPBOARD = new Translation<>("copyDebugMode.clearedClipboard");
    public static final Translation<Var0> COPY_NO_BLOCK_TO_COPY = new Translation<>("copyDebugMode.noBlockToCopy");
    public static final Translation<Var1<Block>> COPIED_ALL_PROPS = new Translation<>("copyDebugMode.copiedAllProperties", new Var1<>((b) -> uppercaseFirst(b.getType().name()), "block"));
    public static final Translation<Var0> COPY_NO_BLOCK_TO_PASTE = new Translation<>("copyDebugMode.noBlockToPaste");
    public static final Translation<Var1<Block>> COPY_NO_APPL_PROPS = new Translation<>("copyDebugMode.noApplicableProperties", new Var1<>((b) -> uppercaseFirst(b.getType().name()), "block"));
    public static final Translation<Var1<Block>> COPY_PASTED_PROPS = new Translation<>("copyDebugMode.pastedProperties", new Var1<>((b) -> uppercaseFirst(b.getType().name()), "block"));
    
    public static final Translation<Var0> FREEZE_NO_PERM = new Translation<>("freezeDebugMode.noPermission");
    public static final Translation<Var0> FREEZE_NO_FROZEN_BLOCKS = new Translation<>("freezeDebugMode.noFrozenBlocks");
    public static final Translation<Var2<Integer, Integer>> FREEZE_UNFROZE_BLOCKS = new Translation<>("freezeDebugMode.unfrozeBlocks", new Var2<>(Object::toString, Object::toString, "unfrozen", "totalFrozen"));
    
    public static final Translation<Var0> ERROR_CANNOT_BIND_DEBUG_SESSION = new Translation<>("errors.cannotBindDebugSession");
    
    public static final Translation<Var0> PREF_SNEAK_TIMEOUT_DESC = new Translation<>("preferences.description.sneakTimeout");
    public static final Translation<Var0> PREF_CHANGE_COOLDOWN_DESC = new Translation<>("preferences.description.changeCooldown");
    public static final Translation<Var0> PREF_SNEAK_MINIMUM_DESC = new Translation<>("preferences.description.sneakMinimum");
    public static final Translation<Var0> PREF_SNEAK_MAXIMUM_DESC = new Translation<>("preferences.description.sneakMaximum");
    public static final Translation<Var0> PREF_SHIFT_MODE = new Translation<>("preferences.description.shiftMode");
    
    public static final Translation<Var1<Double>> SHIFT_DOUBLE_TIME_REMAINING = new Translation<>("shifter.double.timeRemaining", new Var1<>(Object::toString, "timeRemaining"));
    public static final Translation<Var1<Double>> SHIFT_DOUBLE_COOLDOWN = new Translation<>("shifter.double.cooldown", new Var1<>(Object::toString, "timeRemaining"));
    public static final Translation<Var1<Double>> SHIFT_HOLD_HOLD_TIME = new Translation<>("shifter.hold.holdTime", new Var1<>(Object::toString, "timeRemaining"));
    public static final Translation<Var1<Double>> SHIFT_HOLD_TIME_REMAINING = new Translation<>("shifter.hold.timeRemaining", new Var1<>(Object::toString, "timeRemaining"));
    
    public static final Translation<Var0> COMMAND_INFO_DESC = new Translation<>("command.info.description");
    
    public static final Translation<Var1<String>> COMMAND_ERRORS_UNKNOWN_SUBCOMMAND = new Translation<>("command.errors.unknownSubcommand", new Var1<>(String::valueOf, "subcommand"));
    public static final Translation<Var1<String>> COMMAND_ERRORS_UNKNOWN_PREFERENCE = new Translation<>("command.errors.unknownPreference", new Var1<>(String::valueOf, "preference"));
    public static final Translation<Var1<String>> COMMAND_ERRORS_UNKNOWN_DEBUG_MODE = new Translation<>("command.errors.unknownDebugMode", new Var1<>(String::valueOf, "mode"));
    public static final Translation<Var1<DebugModeType<?, ?>>> COMMAND_ERRORS_NO_MODE_PERMISSION = new Translation<>("command.errors.noModePermission", new Var1<>(DebugModeType::getNiceName, "mode"));
    
    public static final Translation<Var0> COMMAND_MESSAGES_GIVEN_DEBUG_STICK = new Translation<>("command.messages.givenDebugStick");
    public static final Translation<Var2<Preference<? ,?>, Object>> COMMAND_MESSAGES_PREFERENCE_SET = new Translation<>("command.messages.preferenceSet", new Var2<>(Preference::getKey, Object::toString, "preference", "value"));
    public static final Translation<Var0> COMMAND_MESSAGES_PLUGIN_RELOADED = new Translation<>("command.messages.pluginReloaded");
    public static final Translation<Var1<DebugModeType<?, ?>>> COMMAND_MESSAGES_MODE_SET = new Translation<>("command.messages.modeSet", new Var1<>(DebugModeType::getNiceName, "mode"));
    
    public static final Translation<Var0> COMMAND_HELP_HEADER = new Translation<>("command.messages.helpCommand.header");
    public static final Translation<Var0> COMMAND_HELP_DBS = new Translation<>("command.messages.helpCommand.dbs");
    public static final Translation<Var0> COMMAND_HELP_MODE = new Translation<>("command.messages.helpCommand.mode");
    public static final Translation<Var0> COMMAND_HELP_ABOUT = new Translation<>("command.messages.helpCommand.about");
    public static final Translation<Var0> COMMAND_HELP_RELOAD = new Translation<>("command.messages.helpCommand.reload");
    public static final Translation<Var0> COMMAND_HELP_PREF_DESC = new Translation<>("command.messages.helpCommand.preference.description");
    public static final Translation<Var0> COMMAND_HELP_PREF_PREF = new Translation<>("command.messages.helpCommand.preference.preference");
    public static final Translation<Var0> COMMAND_HELP_PREF_VALUE = new Translation<>("command.messages.helpCommand.preference.value");
    
    public static final Translation<Var0> COMMAND_ABOUT_HEADER = new Translation<>("command.messages.aboutCommand.header");
    public static final Translation<Var1<Integer>> COMMAND_ABOUT_API_VERSION = new Translation<>("command.messages.aboutCommand.apiVersion");
    public static final Translation<Var1<String>> COMMAND_ABOUT_PLUGIN_VERSION = new Translation<>("command.messages.aboutCommand.pluginVersion");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_IF_ENABLED = new Translation<>("command.messages.aboutCommand.permissions.ifEnabled");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_HEADER = new Translation<>("command.messages.aboutCommand.permissions.header");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_DBS = new Translation<>("command.messages.aboutCommand.permissions.dbs");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_ABOUT = new Translation<>("command.messages.aboutCommand.permissions.about");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_CLASSIC = new Translation<>("command.messages.aboutCommand.permissions.classic");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_COPY = new Translation<>("command.messages.aboutCommand.permissions.copy");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_FREEZE = new Translation<>("command.messages.aboutCommand.permissions.freeze");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_RELOAD = new Translation<>("command.messages.aboutCommand.permissions.reload");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_HELP = new Translation<>("command.messages.aboutCommand.permissions.help");
    public static final Translation<Var0> COMMAND_ABOUT_PERM_PREF = new Translation<>("command.messages.aboutCommand.permissions.preference");
    
    
    
    private static String uppercaseFirst(String given) {
        return given.substring(0, 1).toUpperCase() + given.substring(1).toLowerCase();
    }
    
}
