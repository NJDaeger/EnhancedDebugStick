package com.njdaeger.enhanceddebugstick.api;

import com.njdaeger.enhanceddebugstick.shifter.DoubleShifter;
import com.njdaeger.enhanceddebugstick.shifter.HoldShifter;
import com.njdaeger.enhanceddebugstick.shifter.NullShifter;
import com.njdaeger.enhanceddebugstick.shifter.Shifter;

public enum ShiftMode {

    COMMAND("Command", new NullShifter()),
    DOUBLE("Double", new DoubleShifter()),
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
