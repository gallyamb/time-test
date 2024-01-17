import org.time.ClockHandler;
import org.time.test.plain.PlainClock;

module org.time.test.plain {
    provides ClockHandler with PlainClock;

    requires transitive org.time.core;
}
