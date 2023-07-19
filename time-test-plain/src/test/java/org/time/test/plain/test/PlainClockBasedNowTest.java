package org.time.test.plain.test;

import org.time.Now;
import org.time.test.core.test.BaseNowTest;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

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
