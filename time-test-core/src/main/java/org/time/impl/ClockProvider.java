package org.time.impl;

import org.time.ClockSource;
import org.time.ThreadLimitedClockSource;

import java.time.Clock;
import java.util.ServiceLoader;

public class ClockProvider {
    private static final ClockSource CLOCK_SOURCE;

    static {
        Module module = ClockSource.class.getModule();
        ModuleLayer layer = module.getLayer();
        CLOCK_SOURCE = ServiceLoader.load(layer, ClockSource.class)
                .stream()
                .min((a, b) -> {
                    boolean aIsLimited = a instanceof ThreadLimitedClockSource;
                    boolean bIsLimited = b instanceof ThreadLimitedClockSource;
                    if (aIsLimited == bIsLimited) {
                        return 0;
                    }

                    return aIsLimited ? 1 : -1;

                })
                .map(ServiceLoader.Provider::get)
                .orElseGet(SystemClockSource::new);
    }

    public static Clock clock() {
        return CLOCK_SOURCE.getClock();
    }
}
