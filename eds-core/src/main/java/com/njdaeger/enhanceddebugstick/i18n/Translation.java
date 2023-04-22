package com.njdaeger.enhanceddebugstick.i18n;

import com.njdaeger.pdk.utils.Pair;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @param key The translation key
 * @param placeholders The placeholders to replace in the translation paired with their description
 */
public record Translation(String key, Pair<String, String>... placeholders) {

    public static final Translation TEST = new Translation("test", Pair.of("%player%", "The player to test"));
    
    /*
    
    defintion example:
    
    public static final Translation UNFREEZE_MESSAGE = new Translation("unfreeze-message",
        Pair.of("%player%", "The player to unfreeze"));
    
    UNFREEZE_MESSAGE
    
     */
    
}
