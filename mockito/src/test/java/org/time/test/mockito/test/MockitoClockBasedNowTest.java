package org.time.test.mockito.test;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.time.test.core.test.BaseNowTest;

public class MockitoClockBasedNowTest extends BaseNowTest {
    @Override
    protected OffsetDateTime offsetDateTime() {
        return OffsetDateTime.now();
    }

    @Override
    @Test
    public void testMomentFixation() {
        super.testMomentFixation();
    }

    @Override
    protected ZonedDateTime zonedDateTime() {
        return ZonedDateTime.now();
    }
}
