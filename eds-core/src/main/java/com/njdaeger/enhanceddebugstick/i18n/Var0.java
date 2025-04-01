package com.njdaeger.enhanceddebugstick.i18n;

import net.kyori.adventure.text.TextComponent;

import static com.njdaeger.enhanceddebugstick.util.Util.LEGACY_COMPONENT_SERIALIZER;

public class Var0 extends AbstractVariable {
    
    public Var0() {
        super();
    }
    
    public TextComponent apply() {
        return LEGACY_COMPONENT_SERIALIZER.deserialize(translate());
    }
    
}
