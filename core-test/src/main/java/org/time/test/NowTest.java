package org.time.test;

import java.time.*;
import java.util.function.Supplier;

import org.time.internal.InternalClockHandler;
import org.time.test.impl.ClockState;

public class NowTest {
    private static Clock clock() {
        return InternalClockHandler.INSTANCE.getClock();
    }

    /**
     * Fixates current datetime and all consequent time changes will be performed only via {@link NowTest}'s method
     * calls
     *
     * @param job the job to be performed with fixed time
     */
    public static void withCurrentMoment(Runnable job) {
        withCurrentMoment(toSupplier(job));
    }

    /**
     * Fixates current datetime and all consequent time changes will be performed only via {@link NowTest}'s method
     * calls
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
     *
     * @param job the job to be performed with fixed time in UTC
     */
    public static void withUtcMoment(Instant instant, Runnable job) {
        withUtcMoment(instant, toSupplier(job));
    }

    /**
     * Sets current datetime to specified {@code instant} at UTC and all consequent time changes will be performed only
     * via {@link NowTest}'s method calls
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
     *
     * @param job the job to be performed with fixed time in current default time zone
     */
    public static void withDefaultZoneMoment(Instant instant, Runnable job) {
        withDefaultZoneMoment(instant, toSupplier(job));
    }

    /**
     * Sets current datetime to specified {@code instant} at current default time zone and all consequent time changes
     * will be performed only via {@link NowTest}'s method calls
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
     *
     * @param job the job to be performed with fixed time in specified {@code zoneId} time zone
     */
    public static void withMoment(Instant instant, ZoneId zoneId, Runnable job) {
        withMoment(instant, zoneId, toSupplier(job));
    }

    /**
     * Sets current datetime to specified {@code instant} at specified {@code zoneId} time zone and all consequent time
     * changes will be performed only via {@link NowTest}'s method calls
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
     * fixed within this method cal too
     *
     * @param job the job to be performed with time shifted to specified {@code offset}
     */
    public static void withOffset(Duration offset, Runnable job) {
        withOffset(offset, toSupplier(job));
    }

    /**
     * Increases current datetime to the specified {@code offset}. If current clock is not fixed, then it will not be
     * fixed within this method cal too
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
     * Increases current datetime by the 1 second
     * <p>
     * <b>Important:</b> this method can be called only when clock already modified
     */
    public static void tick() {
        tick(1);
    }

    /**
     * Increases current datetime by the {@code seconds} second
     * <p>
     * <b>Important:</b> this method can be called only when clock already modified
     */
    public static void tick(int seconds) {
        tick(Duration.ofSeconds(seconds));
    }

    /**
     * Increases current datetime by the specified {@code duration}
     * <p>
     * <b>Important:</b> this method can be called only when clock already modified
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
