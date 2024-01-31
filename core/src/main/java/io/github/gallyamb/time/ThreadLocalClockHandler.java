package io.github.gallyamb.time;

/**
 * A marker interface that's used to indicate, that certain {@link ClockHandler} is only limited to one thread
 * <p>
 * I.e. it's unable to reflect clock changes in threads other than that initiated clock change
 */
public interface ThreadLocalClockHandler extends ClockHandler {
}
