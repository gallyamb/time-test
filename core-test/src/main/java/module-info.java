import io.github.gallyamb.time.ClockStateListener;
import io.github.gallyamb.time.test.impl.ClockState;

/**
 * This module contains API to be used in tests to manipulate with current time
 */
module io.github.gallyamb.time.test.core {
    provides ClockStateListener with ClockState;

    exports io.github.gallyamb.time.test;

    requires transitive io.github.gallyamb.time.core;
}
