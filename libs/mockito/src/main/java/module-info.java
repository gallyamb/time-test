import io.github.gallyamb.time.ClockHandler;
import io.github.gallyamb.time.test.mockito.MockitoClock;

/**
 * This module contains an implementation of the {@link ClockHandler} using Mockito mocking library
 *
 * @see MockitoClock
 */
module io.github.gallyamb.time.test.mockito {
    provides ClockHandler with MockitoClock;

    requires transitive io.github.gallyamb.time.core;

    requires org.mockito;
    requires jdk.attach;
}
