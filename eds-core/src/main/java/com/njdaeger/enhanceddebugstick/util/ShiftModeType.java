package com.njdaeger.enhanceddebugstick.util;

import com.njdaeger.enhanceddebugstick.api.mode.ShiftMode;
import com.njdaeger.pdk.command.exception.ArgumentParseException;
import com.njdaeger.pdk.command.exception.PDKCommandException;
import com.njdaeger.pdk.types.ParsedType;

public final class ShiftModeType extends ParsedType<ShiftMode> {
    @Override
    public ShiftMode parse(String input) throws PDKCommandException {
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
