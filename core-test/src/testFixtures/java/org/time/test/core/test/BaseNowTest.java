package org.time.test.core.test;

import java.time.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.time.Now;
import org.time.test.NowTest;

/**
 * Base test class that should be used in other modules with specific {@link org.time.ClockHandler} implementations
 * to test their correctness
 */
public abstract class BaseNowTest {

    public static Stream<Arguments> seconds() {
        return IntStream.range(0, 60)
                .mapToObj(Arguments::of);
    }

    protected abstract ZonedDateTime zonedDateTime();

    protected abstract OffsetDateTime offsetDateTime();

    @Test
    public void testMomentFixation() {
        NowTest.withCurrentMoment((TrashRunnable) (() -> {
            OffsetDateTime expected = offsetDateTime();
            Thread.sleep(5);
            Assertions.assertEquals(expected, offsetDateTime());
        }));
    }

    @ParameterizedTest
    @MethodSource("seconds")
    public void testSpecificMomentFixation(int seconds) {
        String paddedSeconds = "%2s".formatted(seconds).replace(' ', '0');
        NowTest.withUtcMoment(Instant.ofEpochSecond(seconds), (TrashRunnable) (() -> {
            OffsetDateTime expected = OffsetDateTime.parse("1970-01-01T00:00:%sZ".formatted(paddedSeconds));
            Assertions.assertEquals(expected, offsetDateTime());
        }));
    }

    @ParameterizedTest
    @CsvSource({
            "2049-09-12T08:44:37+05:00",
            "1969-04-01T01:01:33-11:00",
            "1976-09-22T01:11:04+07:00",
            "1911-12-07T06:08:11+06:00",
            "2038-08-28T05:29:48+01:00",
            "1785-01-15T23:14:43-04:00",
            "2015-01-17T18:06:58-10:00",
            "1825-07-07T07:01:38+11:00",
            "2090-06-30T18:38:13-03:00",
    })
    public void testSpecificMomentFixation_OffsetDateTime(OffsetDateTime dateTime) {
        NowTest.withMoment(dateTime, (TrashRunnable) (() -> {
            Assertions.assertEquals(dateTime, offsetDateTime());
            Assertions.assertTrue(Now.offsetDateTime(ZoneOffset.UTC).isEqual(dateTime));
        }));
    }

    @ParameterizedTest
    @CsvSource({
            "2049-09-12T08:44:37+05:00[Asia/Yekaterinburg]",
            "1969-04-01T01:01:33-11:00[Pacific/Palau]",
            "1976-09-22T01:11:04+07:00[Antarctica/Davis]",
            "1911-12-07T06:08:11+06:00[Asia/Almaty]",
            "2038-08-28T05:29:48+01:00[Europe/Gibraltar]",
            "1785-01-15T23:14:43-04:00[America/Boa_Vista]",
            "2015-01-17T18:06:58-10:00[Pacific/Honolulu]",
            "1825-07-07T07:01:38+11:00[Pacific/Bougainville]",
            "2090-06-30T18:38:13-03:00[Antarctica/Rothera]",
    })
    public void testSpecificMomentFixation_ZonedDateTime(ZonedDateTime dateTime) {
        NowTest.withMoment(dateTime, (TrashRunnable) (() -> {
            Assertions.assertEquals(dateTime, zonedDateTime());
            Assertions.assertTrue(Now.zonedDateTime(ZoneId.of("UTC")).isEqual(dateTime));
        }));
    }

    @ParameterizedTest
    @CsvSource({
            "PT1s",
            "PT3m1s",
            "PT5h43m14s",
    })
    public void testWithOffset(String durationPattern) {
        Duration expected = Duration.parse(durationPattern);
        NowTest.withCurrentMoment(() -> {
            OffsetDateTime now = offsetDateTime();
            OffsetDateTime future = NowTest.withOffset(expected, () -> Now.offsetDateTime());

            Assertions.assertEquals(expected, Duration.between(now, future));
        });
    }

    @Test
    public void testTickIsAvailableOnlyWithinChangedContext() {
        Assertions.assertThrows(IllegalStateException.class, NowTest::tick);
    }

    @Test
    public void testNegativeDurationTickIsNotAvailable() {
        NowTest.withCurrentMoment(() -> {
            Assertions.assertThrows(IllegalArgumentException.class, () -> NowTest.tick(Duration.ofSeconds(-1)));
        });
    }

    @Test
    public void testSingleTick() {
        System.out.println("Java version");
        System.out.println(System.getProperty("java.version"));
        NowTest.withCurrentMoment(() -> {
            OffsetDateTime now = offsetDateTime();
            NowTest.tick();
            Assertions.assertEquals(now.plusSeconds(1), offsetDateTime());
        });
    }

    @ParameterizedTest
    @CsvSource({"2", "5", "10"})
    public void testNTick_viaSingleTick(int ticksCount) {
        NowTest.withCurrentMoment(() -> {
            OffsetDateTime now = offsetDateTime();
            for (int i = 0; i < ticksCount; i++) {
                NowTest.tick();
            }
            Assertions.assertEquals(now.plusSeconds(ticksCount), offsetDateTime());
        });
    }

    @ParameterizedTest
    @CsvSource({
            "2", "5", "10"
    })
    public void testNTick(int ticksCount) {
        NowTest.withCurrentMoment(() -> {
            OffsetDateTime now = offsetDateTime();
            NowTest.tick(ticksCount);
            Assertions.assertEquals(now.plusSeconds(ticksCount), offsetDateTime());
        });
    }

    @ParameterizedTest
    @CsvSource({
            "PT2S", "PT5S", "PT10S", "P2DT10H"
    })
    public void testDurationTick(Duration duration) {
        NowTest.withCurrentMoment(() -> {
            OffsetDateTime now = offsetDateTime();
            NowTest.tick(duration);
            Assertions.assertEquals(now.plus(duration), offsetDateTime());
        });
    }
}
