import org.junit.platform.launcher.TestExecutionListener;
import org.time.test.junit5.impl.TimeTestExecutionListener;

module org.time.test.spring {
    provides TestExecutionListener with TimeTestExecutionListener;

    exports org.time.test.junit5;

    requires transitive org.time.core;

    requires org.junit.platform.launcher;


}
