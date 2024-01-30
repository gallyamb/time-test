package io.github.gallyamb.time.test.plain.test;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import io.github.gallyamb.time.Now;
import io.github.gallyamb.time.test.core.test.BaseNowTest;

public class PlainClockBasedNowTest extends BaseNowTest {
    @Override
    protected OffsetDateTime offsetDateTime() {
        return Now.offsetDateTime();
    }

    @Override
    protected ZonedDateTime zonedDateTime() {
        return Now.zonedDateTime();
    }
}
