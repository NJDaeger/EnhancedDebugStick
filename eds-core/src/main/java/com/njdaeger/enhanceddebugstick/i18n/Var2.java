package com.njdaeger.enhanceddebugstick.i18n;

import java.util.function.Function;

public class Var2<A, B> extends AbstractVariable {
    
    public Var2(Function<A, String> functionA, Function<B, String> functionB, String... replacements) {
        super(replacements);
        mapFunctions(functionA, functionB);
    }
    
    public String apply(A a, B b) {
        return translate(a, b);
    }
}
