package com.njdaeger.enhanceddebugstick.modes.locked;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugContext;

import java.util.UUID;

public class LockedDebugContext implements DebugContext {

    private final DebugSession session;

    LockedDebugContext(DebugSession session) {
        this.session = session;
    }

    @Override
    public UUID getOwner() {
        return session.getSessionId();
    }

    @Override
    public DebugSession getDebugSession() {
        return session;
    }
}
