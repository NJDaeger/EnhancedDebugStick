package com.njdaeger.enhanceddebugstick.i18n;

import net.kyori.adventure.text.TextComponent;

import java.util.function.Function;

import static com.njdaeger.enhanceddebugstick.util.Util.LEGACY_COMPONENT_SERIALIZER;

public class Var2<A, B> extends AbstractVariable {
    
    public Var2(Function<A, String> functionA, Function<B, String> functionB, String... replacements) {
        super(replacements);
        mapFunctions(functionA, functionB);
    }
    
    public TextComponent apply(A a, B b) {
        return LEGACY_COMPONENT_SERIALIZER.deserialize(translate(a, b));
    }
}
