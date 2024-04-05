package io.github.gallyamb.time.impl;

import java.time.Clock;

import io.github.gallyamb.time.ClockHandler;

public class SystemClockHandler implements ClockHandler {
    @Override
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public void reset() {

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
