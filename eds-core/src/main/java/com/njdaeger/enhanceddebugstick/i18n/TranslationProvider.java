package com.njdaeger.enhanceddebugstick.i18n;

import com.njdaeger.pdk.config.ConfigType;
import com.njdaeger.pdk.config.SmartConfig;
import com.njdaeger.pdk.config.impl.YmlConfig;
import org.bukkit.plugin.Plugin;

public class TranslationProvider extends SmartConfig<YmlConfig> {
    
    public TranslationProvider(Plugin plugin, String lang) {
        super(plugin, ConfigType.YML, lang);
        
        Translation.load();
        var fields = Translation.class.getFields();
        for (var field : fields) {
            if (field.getType() == Translation.class) {
                try {
                    var translation = (Translation<?>)field.get(null);
                    if (translation == null) throw new RuntimeException("Translation var is null for field: " + field.getName());
                    var msg = get(translation.getKey(), "");
                    if (msg.isEmpty()) {
                        msg = translation.getKey();
                        plugin.getLogger().warning("Missing translation key: " + translation.getKey());
                    }
                    translation.get().setMessage(msg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
