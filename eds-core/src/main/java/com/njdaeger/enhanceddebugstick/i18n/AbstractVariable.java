package com.njdaeger.enhanceddebugstick.i18n;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractVariable {
    
    private Map<String, Function<Object, String>> variables;
    private String[] replacements;
    private String message;
    
    public AbstractVariable(String... replacements) {
        this.replacements = replacements;
        this.variables = new HashMap<>();
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @SafeVarargs
    protected final void mapFunctions(Function<?, String>... functions) {
        //we should find the constructor of the class that is extending this class and map the function variables in the order we find them to the replacement names in the order we find them
        
        for (int i = 0; i < functions.length; i++) {
            if (i >= replacements.length) {
                throw new IllegalArgumentException("Too many functions for the amount of replacement keys specified. Make sure the amount of functions equals the amount of replacements specified.");
            }
            variables.put(replacements[i], (Function<Object, String>)functions[i]);
        }
    }
    
    public Function<?, String> get(String key) {
        return variables.get(key);
    }
    
    protected String translate(Object... values) {
        if (values.length == 0 && replacements.length == 0) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        if (values.length != variables.size()) {
            throw new IllegalArgumentException("Too many or too few values specified. Make sure the amount of values equals the amount of functions specified.");
        }
        var msg = message + "";
        for (int i = 0; i < values.length; i++) {
            var obj = values[i];
            msg = msg.replaceAll("%" + replacements[i] + "%", variables.get(replacements[i]).apply(obj));
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
