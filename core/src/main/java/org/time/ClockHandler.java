package org.time;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * This interface is responsible for handling clock state changes and reflecting them via {@link #getClock()}. When
 * clock state is changed, an instance of this interface will be notified via {@link #setClock(Clock)} method
 * invocation
 */
public interface ClockHandler {
    /**
     * @return a {@link Clock} that represents current state of clock in current thread
     */
    Clock getClock();

    /**
     * This method is invoked when user wants to change the clock state. After this method is returned, the caller
     * expect, that clock, returned via {@link #getClock()} method, will reflect changes
     * <p>
     * In addition, implementation also may modify the runtime, so the calls to the {@link Clock#system(ZoneId)},
     * {@link Clock#systemDefaultZone()} and {@link Clock#systemUTC()} also will reflect desired changes. This will
     * allow to use {@link OffsetDateTime#now()} and similar method within production codebase, but at the same time to
     * have a testable environment
     *
     * @param clock the desired state of the clock
     */
    void setClock(Clock clock);

    /**
     * This method is invoked when user wants to reset clock state. After this method is returned, the caller expect,
     * that clock, returned via {@link #getClock()} method, will return original system clock
     */
    void reset();
}
