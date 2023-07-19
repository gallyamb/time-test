import org.time.ClockHandler;
import org.time.ClockSource;
import org.time.test.plain.PlainClock;

module org.time.test.plain {
    requires org.time.test.core;

    provides ClockSource with PlainClock;
    provides ClockHandler with PlainClock;
}