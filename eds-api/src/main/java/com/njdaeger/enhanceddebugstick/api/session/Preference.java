package com.njdaeger.enhanceddebugstick.api.session;

import com.njdaeger.pdk.types.ParsedType;

import java.util.ArrayList;
import java.util.List;

public final class Preference<T, P extends ParsedType<T>> {

    private static final List<Preference<?,?>> preferences = new ArrayList<>();
    
    private final T defVal;
    private final String key;
    private final String desc;
    private final Class<T> type;
    private final Class<P> parser;

    public Preference(String key, String description, T defVal, Class<T> type, Class<P> parser) {
        this.key = key;
        this.desc = description;
        this.type = type;
        this.defVal = defVal;
        this.parser = parser;
        preferences.add(this);
    }

    /**
     * Gets the configuration key of this preference
     * @return The configuration key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the description of this preference
     * @return The preference description
     */
    public String getDescription() {
        return desc;
    }

    /**
     * The type of data this preference holds
     * @return The data type of this preference
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * The string to data parser class type
     * @return The string to data parser
     */
    public Class<P> getParser() {
        return parser;
    }

    /**
     * The default value of this preference
     * @return The default preference value (from configuration)
     */
    public T getDefault() {
        return defVal;
    }

    /**
     * Get a list of preferences
     * @return A list of preferences
     */
    public static List<Preference<?, ?>> getPreferences() {
        return preferences;
    }

    /**
     * Get a preference from a key
     * @param key The key to match to a preference
     * @return The preference, if found, otherwise null.
     */
    public static Preference<?, ?> fromKey(String key) {
        return preferences.stream().filter(p -> p.getKey().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

}
