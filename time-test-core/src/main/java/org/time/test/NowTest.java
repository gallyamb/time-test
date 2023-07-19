package org.time.test;

import org.time.ClockHandler;
import org.time.impl.SystemClockSource;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.time.impl.ClockProvider.clock;

public class NowTest {
    private static final AtomicBoolean CLOCK_IS_CHANGED = new AtomicBoolean(false);
    private static final List<ClockHandler> NOW_HANDLERS = Optional.of(
                    ServiceLoader.load(ClockHandler.class)
                            .stream()
                            .map(ServiceLoader.Provider::get)
                            .toList()
            )
            .filter(l -> !l.isEmpty())
            .orElseGet(() -> List.of(new SystemClockSource()));

    public static void withCurrentMoment(Runnable job) {
        withCurrentMoment(toSupplier(job));
    }

    public static <T> T withCurrentMoment(Supplier<T> job) {
        return withClock(Clock.fixed(clock().instant(), clock().getZone()), job);
    }

    public static void withUtcMoment(Instant instant, Runnable job) {
        withUtcMoment(instant, toSupplier(job));
    }

    public static <T> T withUtcMoment(Instant instant, Supplier<T> job) {
        return withClock(Clock.fixed(instant, ZoneId.of("UTC")), job);
    }

    public static void withDefaultZoneMoment(Instant instant, Runnable job) {
        withDefaultZoneMoment(instant, toSupplier(job));
    }

    public static <T> T withDefaultZoneMoment(Instant instant, Supplier<T> job) {
        return withClock(Clock.fixed(instant, clock().getZone()), job);
    }

    public static void withMoment(Instant instant, ZoneId zoneId, Runnable job) {
        withMoment(instant, zoneId, toSupplier(job));
    }

    public static <T> T withMoment(Instant instant, ZoneId zoneId, Supplier<T> job) {
        return withClock(Clock.fixed(instant, zoneId), job);
    }

    public static void withMoment(OffsetDateTime dateTime, Runnable job) {
        withMoment(dateTime, toSupplier(job));
    }

    public static <T> T withMoment(OffsetDateTime dateTime, Supplier<T> job) {
        return withMoment(dateTime.toInstant(), dateTime.getOffset(), job);
    }

    public static void withMoment(ZonedDateTime dateTime, Runnable job) {
        withMoment(dateTime, toSupplier(job));
    }

    public static <T> T withMoment(ZonedDateTime dateTime, Supplier<T> job) {
        return withMoment(dateTime.toInstant(), dateTime.getZone(), job);
    }

    public static void withOffset(Duration offset, Runnable job) {
        withOffset(offset, toSupplier(job));
    }

    public static <T> T withOffset(Duration offset, Supplier<T> job) {
        return withClock(Clock.offset(clock(), offset), job);
    }

    public static void tick() {
        tick(1);
    }

    public static void tick(int seconds) {
        tick(Duration.ofSeconds(seconds));
    }

    public static void tick(Duration duration) {
        if (!CLOCK_IS_CHANGED.get()) {
            throw new IllegalStateException();
        }

        if (duration.isNegative()) {
            throw new IllegalArgumentException("Negative durations are not allowed: %s".formatted(duration));
        }

        if (duration.isZero()) {
            return;
        }

        NOW_HANDLERS.forEach(h -> h.setClock(Clock.offset(clock(), duration)));
    }

    static void withClock(Clock newClock, Runnable job) {
        withClock(newClock, toSupplier(job));
    }

    static <T> T withClock(Clock newClock, Supplier<T> job) {
        boolean clockIsChanged = CLOCK_IS_CHANGED.get();
        CLOCK_IS_CHANGED.set(true);
        Clock oldClock = clock();
        NOW_HANDLERS.forEach(h -> h.setClock(newClock));

        try {
            return job.get();
        } finally {
            NOW_HANDLERS.forEach(h -> h.setClock(oldClock));
            if (!CLOCK_IS_CHANGED.compareAndSet(true, clockIsChanged)) {
                throw new IllegalStateException();
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
