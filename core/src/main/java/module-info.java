/**
 * This module contains core API, that is used to retrieve current time in different representations
 * <p>
 * Also, there is an API ({@link io.github.gallyamb.time.ClockHandler}) that can be implemented in order to provide
 * ability to change clock state within tests
 */
module io.github.gallyamb.time.core {
    uses io.github.gallyamb.time.ClockHandler;
    uses io.github.gallyamb.time.ClockStateListener;

    exports io.github.gallyamb.time.internal to io.github.gallyamb.time.test.junit, io.github.gallyamb.time.test.core;
    exports io.github.gallyamb.time;
}
