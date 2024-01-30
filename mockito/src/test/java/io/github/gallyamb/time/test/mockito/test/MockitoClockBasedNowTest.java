package io.github.gallyamb.time.test.mockito.test;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import io.github.gallyamb.time.test.core.test.BaseNowTest;

public class MockitoClockBasedNowTest extends BaseNowTest {
    @Override
    protected OffsetDateTime offsetDateTime() {
        return OffsetDateTime.now();
    }

    @Override
    protected ZonedDateTime zonedDateTime() {
        return ZonedDateTime.now();
    }
}
