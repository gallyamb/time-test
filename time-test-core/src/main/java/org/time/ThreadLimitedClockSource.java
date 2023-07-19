package org.time;

/**
 * A marker interface that's used to indicate, that certain {@link ClockSource} is only limited to one thread
 * <p>
 * I.e. it's unable to change the clock in threads other than initiated clock change
 */
public interface ThreadLimitedClockSource extends ClockSource {
}
