package com.njdaeger.enhanceddebugstick.api;

public enum ShiftMode {

    COMMAND("Command"),
    DOUBLE("Double"),
    HOLD("Hold");

    private final String name;

    ShiftMode(String modeName) {
        this.name = modeName;
    }

    public String getName() {
        return name;
    }
}
