package com.njdaeger.enhanceddebugstick.session;

import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.types.ParsedType;

public class LongType extends ParsedType<Long> {
    @Override
    public Long parse(String input) throws BCIException {
        long parsed;
        try {
            parsed = Long.parseLong(input);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Long argument unable to be parsed. Input: " + input);
        }
        return parsed;
    }

    @Override
    public Class<Long> getType() {
        return Long.class;
    }
}
