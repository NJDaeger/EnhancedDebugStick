package com.njdaeger.enhanceddebugstick.api.mode;

public enum ShiftMode {

    /**
     * The command shift mode
     */
    COMMAND("Command", "null"),
    /**
     * The double shift mode
     */
    DOUBLE("Double", "double"),
    /**
     * The hold shift mode
     */
    HOLD("Hold", "hold");

    private final String name;
    private final String shifterId;

    ShiftMode(String modeName, String shifterId) {
        this.name = modeName;
        this.shifterId = shifterId;
    }

    public String getShifterId() {
        return shifterId;
    }

    public String getName() {
        return name;
    }
}
