import io.github.gallyamb.time.test.junit.impl.TimeTestExecutionListener;
import org.junit.platform.launcher.TestExecutionListener;

/**
 * This module contains an API for convenient writing of tests using JUnit5 platform
 *
 * @see io.github.gallyamb.time.test.junit.FixedTime
 */
module io.github.gallyamb.time.test.junit {
    provides TestExecutionListener with TimeTestExecutionListener;

    exports io.github.gallyamb.time.test.junit;

    requires transitive io.github.gallyamb.time.core;

    requires org.junit.platform.launcher;


}
