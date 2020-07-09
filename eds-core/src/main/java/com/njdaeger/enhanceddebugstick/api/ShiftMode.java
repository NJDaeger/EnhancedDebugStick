package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.shifter.DoubleShifter;
import com.njdaeger.enhanceddebugstick.shifter.HoldShifter;
import com.njdaeger.enhanceddebugstick.shifter.NullShifter;
import com.njdaeger.enhanceddebugstick.shifter.Shifter;

public enum ShiftMode {

    /**
     * The command shift mode
     */
    COMMAND("Command", new NullShifter()),
    /**
     * The double shift mode
     */
    DOUBLE("Double", new DoubleShifter()),
    /**
     * The hold shift mode
     */
    HOLD("Hold", new HoldShifter());

    private final String name;
    private final Shifter<?, ?> shifter;

    ShiftMode(String modeName, Shifter<?, ?> shifter) {
        this.name = modeName;
        this.shifter = shifter;
    }

    public Shifter<?, ?> getShifter() {
        return shifter;
    }

    public String getName() {
        return name;
    }
}
