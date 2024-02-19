package com.njdaeger.enhanceddebugstick.session;

import com.njdaeger.enhanceddebugstick.api.config.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.mode.ShiftMode;
import com.njdaeger.enhanceddebugstick.api.session.Preference;
import com.njdaeger.enhanceddebugstick.i18n.Translation;
import com.njdaeger.enhanceddebugstick.util.LongType;
import com.njdaeger.enhanceddebugstick.util.ShiftModeType;

/**
 * Default user preferences for the Enhanced Debug Stick
 */
public final class DefaultPreferences {
    
    /**
     * How quickly the player needs to double shift to enable the mode shifter
     */
    public static final Preference<Long, LongType> SNEAK_TIMEOUT = new Preference<>("sneakTimeout", Translation.PREF_SNEAK_TIMEOUT_DESC.get().apply(), ConfigKey.get().DOUBLE_SNEAK_TIMEOUT, Long.class, LongType.class);
    /**
     * Delay for the double shifter to start again. (When you start the shifter, then stop it, you wont be able to start it again for THIS amount of time)
     */
    public static final Preference<Long, LongType> CHANGE_COOLDOWN = new Preference<>("changeCooldown", Translation.PREF_CHANGE_COOLDOWN_DESC.get().apply(), ConfigKey.get().MS_CHANGE_COOLDOWN, Long.class, LongType.class);
    /**
     * The minimum amount of time required to hold shift in order to start the mode shifter (HOLD shift mode)
     */
    public static final Preference<Long, LongType> SNEAK_MINIMUM = new Preference<>("sneakMinimum", Translation.PREF_SNEAK_MINIMUM_DESC.get().apply(), ConfigKey.get().HOLD_SNEAK_MINIMUM, Long.class, LongType.class);
    /**
     * The maximum amount of time allowed to hold shift in order to start the mode shifter (HOLD shift mode)
     */
    public static final Preference<Long, LongType> SNEAK_MAXIMUM = new Preference<>("sneakMaximum", Translation.PREF_SNEAK_MAXIMUM_DESC.get().apply(), ConfigKey.get().HOLD_SNEAK_MAXIMUM, Long.class, LongType.class);
    /**
     * The current shift mode enabled
     */
    public static final Preference<ShiftMode, ShiftModeType> SHIFT_MODE = new Preference<>("shiftMode", Translation.PREF_SHIFT_MODE.get().apply(), ConfigKey.get().MS_DEFAULT_SHIFT_PREFERENCE, ShiftMode.class, ShiftModeType.class);
    
    
    private DefaultPreferences() {}
    
    public static void registerPreferences() {
    }
    
}
