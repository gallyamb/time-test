package org.time.impl;

import org.time.ClockHandler;

import java.time.Clock;

public class SystemClockSource implements ClockHandler {
    @Override
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public void setClock(Clock clock) {
        throw new IllegalAccessError("""
                Changing the clock state in production is not allowed!
                            
                Either you tried to change clock in production code,
                or you trying to run tests and does not include any implementation library (like time-test-plain)")
                """);
    }
}
