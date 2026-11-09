package example;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;
import io.github.gallyamb.time.Now;
import io.github.gallyamb.time.test.NowTest;

public final class ClockConsumer {
    public static void main(String[] args) {
        var moment = Instant.parse("2032-06-01T12:00:00Z");
        try (var outer = NowTest.useClock(Clock.fixed(moment, ZoneOffset.UTC))) {
            NowTest.withUtcMoment(moment.plusSeconds(20), () -> NowTest.tick(3));
            if (!Now.instant().equals(moment)) throw new AssertionError("nested restoration");
            if (!CompletableFuture.supplyAsync(Now::instant).join().equals(moment)) {
                throw new AssertionError("cross-thread visibility");
            }
            NowTest.tick();
            if (!Now.clock().instant().equals(Instant.parse("2032-06-01T12:00:01Z"))) {
                throw new AssertionError("clock advancement");
            }
        }
        try {
            NowTest.tick();
            throw new AssertionError("clock state was not reset");
        } catch (IllegalStateException expected) {
            System.out.println("Published classpath consumer passed");
        }
    }
}
