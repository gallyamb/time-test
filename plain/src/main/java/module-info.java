import io.github.gallyamb.time.ClockHandler;
import io.github.gallyamb.time.test.plain.PlainClock;

/**
 * This module contains an implementation of the {@link ClockHandler} using a dedicated {@link java.time.Clock}
 * <p>
 * This module can be used only when current time is obtained via {@link io.github.gallyamb.time.Now} class' methods
 *
 * @see PlainClock
 */
module io.github.gallyamb.time.test.plain {
    provides ClockHandler with PlainClock;

    requires transitive io.github.gallyamb.time.core;
}
