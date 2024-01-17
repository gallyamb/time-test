package org.time.test.junit5.test;

import java.time.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.time.Now;
import org.time.test.NowTest;
import org.time.test.junit5.FixedTime;

@FixedTime
public class TimeTestExecutionListenerTest {
    @Test
    public void testCurrentMomentFixation() throws InterruptedException {
        OffsetDateTime start = Now.offsetDateTime(ZoneOffset.MAX);
        Thread.sleep(100);
        OffsetDateTime end = Now.offsetDateTime(ZoneOffset.UTC);
        Assertions.assertTrue(start.isEqual(end));
    }

    @Test
    @FixedTime("2020-05-12T15:33:54+10:00")
    public void testSpecificMomentFixation_OffsetDateTime() {
        Assertions.assertEquals(
                OffsetDateTime.of(2020, 5, 12, 15, 33, 54, 0, ZoneOffset.ofHours(10)),
                Now.offsetDateTime()
        );
    }

    @Test
    @FixedTime("2020-05-12T15:33:54-07:00[America/Los_Angeles]")
    public void testSpecificMomentFixation_ZonedDateTime() {
        Assertions.assertEquals(
                OffsetDateTime.of(2020, 5, 12, 15, 33, 54, 0, ZoneOffset.ofHours(-7)),
                Now.offsetDateTime()
        );
        Assertions.assertEquals(
                ZonedDateTime.of(2020, 5, 12, 15, 33, 54, 0, ZoneId.of("America/Los_Angeles")),
                Now.zonedDateTime()
        );
    }

    @Test
    public void testTicks() {
        OffsetDateTime start = Now.offsetDateTime();
        Duration duration = Duration.parse("PT2H3M");
        NowTest.tick(duration);
        OffsetDateTime end = Now.offsetDateTime();
        Assertions.assertEquals(duration, Duration.between(start, end));
    }
}
