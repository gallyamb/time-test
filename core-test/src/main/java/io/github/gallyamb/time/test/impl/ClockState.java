package io.github.gallyamb.time.test.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import io.github.gallyamb.time.ClockStateListener;

public class ClockState implements ClockStateListener {
    private static final AtomicBoolean CLOCK_IS_CHANGED = new AtomicBoolean(false);

    public static boolean changed() {
        return CLOCK_IS_CHANGED.get();
    }

    @Override
    public void notifyClockChanged() {
        CLOCK_IS_CHANGED.set(true);
    }

    @Override
    public void notifyClockReset() {
        CLOCK_IS_CHANGED.set(false);
    }
}
