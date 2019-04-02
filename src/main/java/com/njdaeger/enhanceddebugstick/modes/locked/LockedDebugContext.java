package com.njdaeger.enhanceddebugstick.modes.locked;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugContext;
import jdk.nashorn.internal.ir.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LockedDebugContext implements DebugContext {

    private final List<Block> locked;
    private final DebugSession session;

    LockedDebugContext(DebugSession session) {
        this.session = session;
        this.locked = new ArrayList<>();
    }

    @Override
    public UUID getOwner() {
        return session.getSessionId();
    }

    @Override
    public DebugSession getDebugSession() {
        return session;
    }

    public void lockBlock(Block block) {

    }

    public void unlockBlock(Block block) {

    }

    public void unlockAllBlocks() {

    }



}
