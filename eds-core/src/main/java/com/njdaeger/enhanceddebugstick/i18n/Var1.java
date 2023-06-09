package com.njdaeger.enhanceddebugstick.i18n;

import java.util.function.Function;

public class Var1<A> extends AbstractVariable {
    
    public Var1(Function<A, String> function, String replacement) {
        super(replacement);
        mapFunctions(function);
    }
    
    public String apply(A a) {
        return translate(a);
    }
    
}
