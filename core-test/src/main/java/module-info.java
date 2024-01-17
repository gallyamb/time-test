import org.time.ClockStateListener;
import org.time.test.impl.ClockState;

module org.time.test.core {
    provides ClockStateListener with ClockState;

    exports org.time.test;

    requires transitive org.time.core;
}
