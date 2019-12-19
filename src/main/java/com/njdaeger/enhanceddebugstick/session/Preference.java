package com.njdaeger.enhanceddebugstick.session;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.ShiftMode;

import java.util.ArrayList;
import java.util.List;

public final class Preference<T> {

    private static final List<Preference<?>> preferences = new ArrayList<>();

    public static final Preference<Long> SNEAK_TIMEOUT = new Preference<>("sneakTimeout", "How quickly you need to double sneak.", ConfigKey.DOUBLE_SNEAK_TIMEOUT, Long.class);
    public static final Preference<Long> CHANGE_COOLDOWN = new Preference<>("changeCooldown", "How long you must wait to change modes again.", ConfigKey.MS_CHANGE_COOLDOWN, Long.class);
    public static final Preference<Long> SNEAK_MINIMUM = new Preference<>("sneakMinimum", "The least amount of time sneak needs to be held until the mode shifter can turn on.", ConfigKey.HOLD_SNEAK_MINIMUM,Long.class);
    public static final Preference<Long> SNEAK_MAXIMUM = new Preference<>("sneakMaximum", "The most amount of time sneak can be held for the mode shifter to turn on.", ConfigKey.HOLD_SNEAK_MAXIMUM,Long.class);
    public static final Preference<ShiftMode> SHIFT_MODE = new Preference<>("shiftMode", "How to enable the debug mode shifter.", ConfigKey.MS_DEFAULT_SHIFT_PREFERENCE,ShiftMode.class);

    private final T defVal;
    private final String key;
    private final String desc;
    private final Class<T> type;

    Preference(String key, String description, T defVal, Class<T> type) {
        this.key = key;
        this.desc = description;
        this.type = type;
        this.defVal = defVal;
        preferences.add(this);
    }

    public static void registerPreferences() {
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return desc;
    }

    public Class<T> getType() {
        return type;
    }

    public T getDefault() {
        return defVal;
    }

    public static List<Preference<?>> getPreferences() {
        return preferences;
    }

    public static Preference<?> fromKey(String key) {
        return preferences.stream().filter(p -> p.getKey().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

}
