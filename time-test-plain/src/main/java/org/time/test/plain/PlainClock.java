package org.time.test.plain;

import org.time.ClockHandler;
import org.time.ClockSource;

import java.time.Clock;

public class PlainClock implements ClockHandler, ClockSource {
    private static final PlainClock INSTANCE = new PlainClock();
    private volatile Clock clock = Clock.systemDefaultZone();

    public static PlainClock provider() {
        return INSTANCE;
    }

    @Override
    public Clock getClock() {
        return provider().clock;
    }

    @Override
    public void setClock(Clock clock) {
        provider().clock = clock;
    }
}
