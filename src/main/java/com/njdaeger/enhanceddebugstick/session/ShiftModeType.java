package com.njdaeger.enhanceddebugstick.session;

import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.types.ParsedType;
import com.njdaeger.enhanceddebugstick.api.ShiftMode;

public final class ShiftModeType extends ParsedType<ShiftMode> {
    @Override
    public ShiftMode parse(String input) throws BCIException {
        ShiftMode mode;
        try {
            mode = ShiftMode.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ArgumentParseException("Shift Mode argument unable to be parsed. Input: " + input);
        }
        return mode;
    }

    @Override
    public Class<ShiftMode> getType() {
        return ShiftMode.class;
    }
}
