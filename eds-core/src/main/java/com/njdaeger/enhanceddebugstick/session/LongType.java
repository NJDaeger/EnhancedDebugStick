package com.njdaeger.enhanceddebugstick.session;

import com.njdaeger.pdk.command.exception.ArgumentParseException;
import com.njdaeger.pdk.command.exception.PDKCommandException;
import com.njdaeger.pdk.types.ParsedType;

public final class LongType extends ParsedType<Long> {
    @Override
    public Long parse(String input) throws PDKCommandException {
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
