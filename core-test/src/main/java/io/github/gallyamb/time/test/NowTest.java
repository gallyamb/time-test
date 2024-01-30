package io.github.gallyamb.time.test;

import java.time.*;
import java.util.function.Supplier;

import io.github.gallyamb.time.internal.InternalClockHandler;
import io.github.gallyamb.time.test.impl.ClockState;

/**
 * This is the main class, that you'll use in your tests. It allows to modify current clock state in error-prone manner,
 * made your tests code more robust and developer-friendly
 * <p>
 * Check methods' javadoc for usage info
 */
public class NowTest {
    private static Clock clock() {
        return InternalClockHandler.INSTANCE.getClock();
    }

    /**
     * Fixates current datetime and all consequent time changes will be performed only via {@link NowTest}'s method
     * calls
     *
     * <h2>Example</h2>
     * <pre>
     * NowTest.withCurrentMoment(() -> {
     *     // time here will be constantly fixed at the moment
     *     // of call to the NowTest.withCurrentMoment
     *
     *     var now = Now.offsetDateTime();
     *
     *     var then = NowTest.withOffset(Duration.ofDays(1), () -> {
     *         // time here will be constantly fixed at the moment
     *         // of call to the NowTest.withCurrentMoment + 1 day
     *         Thread.sleep(1000)
     *         return Now.offsetDateTime();
     *     });
     *
     *     Assertions.assertEquals(Duration.ofDays(1), Duration.between(now, then));
     * });
     * </pre>
     *
     * @param job the job to be performed with fixed time
     */
    public static void withCurrentMoment(Runnable job) {
        withCurrentMoment(toSupplier(job));
    }

    /**
     * Fixates current datetime and all consequent time changes will be performed only via {@link NowTest}'s method
     * calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time
     *
     * @return result of the job execution
     */
    public static <T> T withCurrentMoment(Supplier<T> job) {
        Clock clock = clock();
        return withClock(Clock.fixed(clock.instant(), clock.getZone()), job);
    }

    /**
     * Sets current datetime to specified {@code instant} at UTC and all consequent time changes will be performed only
     * via {@link NowTest}'s method calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time in UTC
     */
    public static void withUtcMoment(Instant instant, Runnable job) {
        withUtcMoment(instant, toSupplier(job));
    }

    /**
     * Sets current datetime to specified {@code instant} at UTC and all consequent time changes will be performed only
     * via {@link NowTest}'s method calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time in UTC
     *
     * @return result of the job execution
     */
    public static <T> T withUtcMoment(Instant instant, Supplier<T> job) {
        return withClock(Clock.fixed(instant, ZoneId.of("UTC")), job);
    }

    /**
     * Sets current datetime to specified {@code instant} at current default time zone and all consequent time changes
     * will be performed only via {@link NowTest}'s method calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time in current default time zone
     */
    public static void withDefaultZoneMoment(Instant instant, Runnable job) {
        withDefaultZoneMoment(instant, toSupplier(job));
    }

    /**
     * Sets current datetime to specified {@code instant} at current default time zone and all consequent time changes
     * will be performed only via {@link NowTest}'s method calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time in current default time zone
     *
     * @return result of the job execution
     */
    public static <T> T withDefaultZoneMoment(Instant instant, Supplier<T> job) {
        return withClock(Clock.fixed(instant, clock().getZone()), job);
    }

    /**
     * Sets current datetime to specified {@code instant} at specified {@code zoneId} time zone and all consequent time
     * changes will be performed only via {@link NowTest}'s method calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time in specified {@code zoneId} time zone
     */
    public static void withMoment(Instant instant, ZoneId zoneId, Runnable job) {
        withMoment(instant, zoneId, toSupplier(job));
    }

    /**
     * Sets current datetime to specified {@code instant} at specified {@code zoneId} time zone and all consequent time
     * changes will be performed only via {@link NowTest}'s method calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time in specified {@code zoneId} time zone
     *
     * @return result of the job execution
     */
    public static <T> T withMoment(Instant instant, ZoneId zoneId, Supplier<T> job) {
        return withClock(Clock.fixed(instant, zoneId), job);
    }

    /**
     * Sets current datetime to specified {@code dateTime}. Time zone will be detected via supplied
     * {@link OffsetDateTime}'s value. All consequent time changes will be performed only via {@link NowTest}'s method
     * calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time equal to supplied {@code dateTime}
     */
    public static void withMoment(OffsetDateTime dateTime, Runnable job) {
        withMoment(dateTime, toSupplier(job));
    }

    /**
     * Sets current datetime to specified {@code dateTime}. Time zone will be detected via supplied
     * {@link OffsetDateTime}'s value. All consequent time changes will be performed only via {@link NowTest}'s method
     * calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time equal to supplied {@code dateTime}
     *
     * @return result of the job execution
     */
    public static <T> T withMoment(OffsetDateTime dateTime, Supplier<T> job) {
        return withMoment(dateTime.toInstant(), dateTime.getOffset(), job);
    }

    /**
     * Sets current datetime to specified {@code dateTime}. Time zone will be detected via supplied
     * {@link ZonedDateTime}'s value. All consequent time changes will be performed only via {@link NowTest}'s method
     * calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time equal to supplied {@code dateTime}
     */
    public static void withMoment(ZonedDateTime dateTime, Runnable job) {
        withMoment(dateTime, toSupplier(job));
    }

    /**
     * Sets current datetime to specified {@code dateTime}. Time zone will be detected via supplied
     * {@link ZonedDateTime}'s value. All consequent time changes will be performed only via {@link NowTest}'s method
     * calls
     * <p>
     * For example see {@link #withCurrentMoment(Runnable)}
     *
     * @param job the job to be performed with fixed time equal to supplied {@code dateTime}
     *
     * @return result of the job execution
     */
    public static <T> T withMoment(ZonedDateTime dateTime, Supplier<T> job) {
        return withMoment(dateTime.toInstant(), dateTime.getZone(), job);
    }

    /**
     * Increases current datetime to the specified {@code offset}. If current clock is not fixed, then it will not be
     * fixed within this method call too
     *
     * <h2>Examples</h2>
     *
     * <h3>Example with fixed clock</h3>
     * <pre>
     * NowTest.withMoment(OffsetDateTime.parse("2020-01-01T00:00:30Z"), () -> {
     *     // time here will be constantly fixed at the 2020-01-01T00:00:30Z
     *
     *     var now = Now.offsetDateTime(); // equal to 2020-01-01T00:00:30Z and won't change with wall clock change
     *
     *     var then = NowTest.withOffset(Duration.ofDays(1), () -> {
     *         // time here will be constantly fixed at the 2020-01-02T00:00:30Z
     *         Thread.sleep(1000)
     *         return Now.offsetDateTime(); // equals to 2020-01-02T00:00:30Z even after 1 second thread sleep
     *     });
     *
     *     Assertions.assertEquals(Duration.ofDays(1), Duration.between(now, then));
     * });
     * </pre>
     *
     * <h3>Example with non-fixed clock</h3>
     * <pre>
     * var now = Now.offsetDateTime(); // equal to current datetime
     *
     * var then = NowTest.withOffset(Duration.ofDays(1), () -> {
     *     // time here will NOT be constantly fixed at any datetime
     *     Thread.sleep(1000)
     *     return Now.offsetDateTime(); // equals to current datetime + 1 day + 1 second + some wall clock elapsed between method calls
     * });
     *
     * // this assertion will be flaky, because not always execution time between
     * // two call to Now.offsetDateTime() will take under real 1 second
     * Assertions.assertEquals(
     *     Duration.ofDays(1).plusSeconds(1),
     *     Duration.between(now, then).truncatedTo(ChronoUnit.SECONDS)
     * );
     * </pre>
     *
     * @param job the job to be performed with time shifted to specified {@code offset}
     */
    public static void withOffset(Duration offset, Runnable job) {
        withOffset(offset, toSupplier(job));
    }

    /**
     * Increases current datetime to the specified {@code offset}. If current clock is not fixed, then it will not be
     * fixed within this method cal too
     * <p>
     * For example see {@link #withOffset(Duration, Runnable)}
     *
     * @param job the job to be performed with time shifted to specified {@code offset}
     *
     * @return result of the job execution
     */
    public static <T> T withOffset(Duration offset, Supplier<T> job) {
        if (offset.isNegative()) {
            throw new IllegalArgumentException("Negative durations are not allowed: %s".formatted(offset));
        }

        return withClock(Clock.offset(clock(), offset), job);
    }

    /**
     * Increases current datetime by 1 second. Allow to write less nested tests code and take more attention on your
     * intent
     * <p>
     * <b>Important:</b> this method can be called only when clock already modified
     *
     * <h2>Example</h2>
     * <pre>
     * NowTest.withCurrentMoment(() -> {
     *     var start = Now.offsetDateTime();
     *     NowTest.tick();
     *     var end = Now.offsetDateTime();
     *
     *     Assertions.assertEquals(Duration.ofSeconds(1), Duration.between(start, end));
     * });
     * </pre>
     */
    public static void tick() {
        tick(1);
    }

    /**
     * Increases current datetime by the {@code seconds} second. Allow to write less nested tests code and take more
     * attention on your intent
     * <p>
     * <b>Important:</b> this method can be called only when clock already modified
     *
     * <h2>Example</h2>
     * <pre>
     * NowTest.withCurrentMoment(() -> {
     *     var start = Now.offsetDateTime();
     *     NowTest.tick(1000);
     *     var end = Now.offsetDateTime();
     *
     *     Assertions.assertEquals(Duration.ofSeconds(1000), Duration.between(start, end));
     * });
     * </pre>
     */
    public static void tick(int seconds) {
        tick(Duration.ofSeconds(seconds));
    }

    /**
     * Increases current datetime by the specified {@code duration}. Allow to write less nested tests code and take more
     * attention on your intent
     * <p>
     * <b>Important:</b> this method can be called only when clock already modified
     *
     * <h2>Example</h2>
     * <pre>
     * NowTest.withCurrentMoment(() -> {
     *     var start = Now.offsetDateTime();
     *     NowTest.tick(Duration.ofDays(300));
     *     var end = Now.offsetDateTime();
     *
     *     // notice, that this test does not require to be ran for 300 days :)
     *     Assertions.assertEquals(Duration.ofDays(300), Duration.between(start, end));
     * });
     * </pre>
     */
    public static void tick(Duration duration) {
        if (!ClockState.changed()) {
            throw new IllegalStateException("This method have to be called only when clock is in modified state");
        }

        if (duration.isNegative()) {
            throw new IllegalArgumentException("Negative durations are not allowed: %s".formatted(duration));
        }

        if (duration.isZero()) {
            return;
        }

        InternalClockHandler.INSTANCE.setClock(Clock.offset(clock(), duration));
    }

    private static <T> T withClock(Clock newClock, Supplier<T> job) {
        boolean clockIsChanged = ClockState.changed();
        Clock oldClock = clock();
        InternalClockHandler.INSTANCE.setClock(newClock);

        try {
            return job.get();
        } finally {
            if (clockIsChanged) {
                InternalClockHandler.INSTANCE.setClock(oldClock);
            } else {
                InternalClockHandler.INSTANCE.reset();
            }
        }
    }

    private static Supplier<Object> toSupplier(Runnable job) {
        return () -> {
            job.run();
            return null;
        };
    }
}
