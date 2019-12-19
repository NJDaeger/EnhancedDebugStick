package com.njdaeger.enhanceddebugstick.session;

import com.njdaeger.bcm.types.YmlConfig;
import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PreferenceTrack extends YmlConfig {

    private Map<Preference<?>, Object> preferences;

    public PreferenceTrack(UUID userId) {
        super(EnhancedDebugStick.getInstance(), "preferences" + File.separator + userId.toString());
        this.preferences = new HashMap<>();

        Preference.getPreferences().forEach(p -> {
            if (contains(p.getKey(), true)) preferences.put(p, getValueAs(p.getKey(), p.getType()));
            else preferences.put(p, p.getDefault());

            addEntry(p.getKey(), preferences.get(p) instanceof Enum ? ((Enum) preferences.get(p)).name() : preferences.get(p));
        });
/*
        this.mode = getValueAs("shiftMode", ShiftMode.class);
        if (mode == null) mode = ConfigKey.MS_DEFAULT_SHIFT_PREFERENCE;

        this.sneakTimeout = contains("sneakTimeout", true) ? getLong("sneakTimeout") : ConfigKey.DOUBLE_SNEAK_TIMEOUT;
        this.changeCooldown = contains("changeCooldown", true) ? getLong("changeCooldown") : ConfigKey.MS_CHANGE_COOLDOWN;
        this.sneakMinimum = contains("sneakMinimum", true) ? getLong("sneakMinimum") : ConfigKey.HOLD_SNEAK_MINIMUM;
        this.sneakMaximum = contains("sneakMaximum", true) ? getLong("sneakMaximum") : ConfigKey.HOLD_SNEAK_MAXIMUM;

        addEntry("shiftMode", mode);
        addEntry("sneakTimeout", sneakTimeout);
        addEntry("changeCooldown", changeCooldown);
        addEntry("sneakMinimum", sneakMinimum);
        addEntry("sneakMaximum", sneakMaximum);*/

    }

    public <T> T get(Preference<T> preference) {
        return (T) preferences.get(preference);
    }

    /*public ShiftMode getShiftMode() {
        return mode;
    }

    public long getSneakTimeout() {
        return sneakTimeout;
    }

    public long getSneakMaximum() {
        return sneakMaximum;
    }

    public long getSneakMinimum() {
        return sneakMinimum;
    }

    public long getChangeCooldown() {
        return changeCooldown;
    }*/

}
