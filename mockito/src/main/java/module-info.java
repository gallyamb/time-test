import org.time.ClockHandler;
import org.time.test.mockito.MockitoClock;

module org.time.test.mockito {
    provides ClockHandler with MockitoClock;

    requires transitive org.time.core;

    requires org.mockito;
    requires jdk.attach;
}
