package org.time;

import java.time.Clock;

/**
 * A source of the {@link Clock}. Provides a {@link Clock} that will be used to create classes under {@link java.time}
 * package
 */
public interface ClockSource {
    /**
     * @return a {@link Clock} that will be used to create classes under {@link java.time} package
     */
    Clock getClock();
}
