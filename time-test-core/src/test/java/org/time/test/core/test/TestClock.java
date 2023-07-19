package org.time.test.core.test;

import org.time.ClockHandler;

import java.time.Clock;

public class TestClock implements ClockHandler {
    private static final TestClock INSTANCE = new TestClock();
    private volatile Clock clock = Clock.systemDefaultZone();

    @Override
    public Clock getClock() {
        return INSTANCE.clock;
    }

    @Override
    public void setClock(Clock clock) {
        INSTANCE.clock = clock;
    }
}
