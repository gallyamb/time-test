package io.github.gallyamb.time.test.plain.classpath;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.github.gallyamb.time.ClockHandler;
import io.github.gallyamb.time.ClockStateListener;
import io.github.gallyamb.time.Now;
import io.github.gallyamb.time.test.NowTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClasspathClockTest {
    private static final Instant START = Instant.parse("2031-04-12T10:30:00Z");

    @Test
    void discoversClockHandlerAndStateListenerWithoutModulePath() {
        assertFalse(Now.class.getModule().isNamed());
        assertEquals(1, ServiceLoader.load(ClockHandler.class).stream().count());
        assertEquals(1, ServiceLoader.load(ClockStateListener.class).stream().count());
    }

    @Test
    void tickAdvancesFixedClockAndRejectsUnmodifiedClock() {
        NowTest.withUtcMoment(START, () -> {
            NowTest.tick(7);
            assertEquals(Instant.parse("2031-04-12T10:30:07Z"), Now.instant());
        });
        assertThrows(IllegalStateException.class, NowTest::tick);
    }

    @Test
    void nestedFailureRestoresOuterInstantAndZone() {
        NowTest.withMoment(START, ZoneId.of("Europe/Paris"), () -> {
            assertThrows(IllegalArgumentException.class, () ->
                NowTest.withMoment(START.plusSeconds(60), ZoneId.of("Asia/Tokyo"), () -> {
                    throw new IllegalArgumentException("synthetic failure");
                }));
            assertEquals(START, Now.instant());
            assertEquals(ZoneId.of("Europe/Paris"), Now.zonedDateTime().getZone());
            NowTest.withOffset(Duration.ofMinutes(2), () ->
                assertEquals(Instant.parse("2031-04-12T10:32:00Z"), Now.instant()));
            assertEquals(START, Now.instant());
        });
    }

    @Test
    void plainClockIsVisibleAcrossThreads() {
        NowTest.withUtcMoment(START, () -> {
            try {
                assertEquals(START, CompletableFuture.supplyAsync(Now::instant).get(5, TimeUnit.SECONDS));
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        });
    }
    @Test
    void publicClockScopeRestoresStateAfterSeparateLifecycleCallbacks() {
        assertThrows(IllegalStateException.class, NowTest::tick);
        var outer = NowTest.useClock(Clock.fixed(START, ZoneId.of("Europe/Paris")));
        try {
            assertEquals(START, Now.clock().instant());
            NowTest.withClock(Clock.offset(Now.clock(), Duration.ofHours(-2)), () -> {
                assertEquals(Instant.parse("2031-04-12T08:30:00Z"), Now.instant());
                NowTest.tick(3_000_000_000L);
                assertEquals(Instant.parse("2126-05-06T13:50:00Z"), Now.instant());
            });
            assertEquals(START, Now.instant());
            assertThrows(NullPointerException.class, () -> NowTest.useClock(null));
            assertEquals(START, Now.instant());
        } finally {
            outer.close();
        }
        assertThrows(IllegalStateException.class, NowTest::tick);
        NowTest.withUtcMoment(START.plusSeconds(1), () -> {
            outer.close(); // Closing an already closed scope cannot reset a later scope.
            assertEquals(Instant.parse("2031-04-12T10:30:01Z"), Now.instant());
        });
    }

}
