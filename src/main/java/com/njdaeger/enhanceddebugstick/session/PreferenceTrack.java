package com.njdaeger.enhanceddebugstick.session;

import com.njdaeger.bci.types.ParsedType;
import com.njdaeger.bcm.types.YmlConfig;
import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PreferenceTrack extends YmlConfig {

    private Map<Preference<?, ?>, Object> preferences;

    public PreferenceTrack(UUID userId) {
        super(EnhancedDebugStick.getInstance(), "preferences" + File.separator + userId.toString());
        this.preferences = new HashMap<>();

        Preference.getPreferences().forEach(p -> {
            if (contains(p.getKey(), true)) preferences.put(p, getValueAs(p.getKey(), p.getType()));
            else preferences.put(p, p.getDefault());

            addEntry(p.getKey(), preferences.get(p) instanceof Enum ? ((Enum) preferences.get(p)).name() : preferences.get(p));
        });

    }

    public <T, P extends ParsedType<T>> T get(Preference<T, P> preference) {
        return (T) preferences.get(preference);
    }

    public <T, P extends ParsedType<T>> void set(Preference<T, P> pref, T val) {
        preferences.put(pref, val);
    }
}
