package com.njdaeger.enhanceddebugstick.i18n;

import net.kyori.adventure.text.TextComponent;

import java.util.function.Function;

import static com.njdaeger.enhanceddebugstick.util.Util.LEGACY_COMPONENT_SERIALIZER;

public class Var1<A> extends AbstractVariable {
    
    public Var1(Function<A, String> function, String replacement) {
        super(replacement);
        mapFunctions(function);
    }
    
    public TextComponent apply(A a) {
        return LEGACY_COMPONENT_SERIALIZER.deserialize(translate(a));
    }
    
}
