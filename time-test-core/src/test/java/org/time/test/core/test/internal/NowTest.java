package org.time.test.core.test.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.time.test.core.test.TrashRunnable;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.time.Now.offsetDateTime;
import static org.time.test.NowTest.withUtcMoment;

public class NowTest {
    public static Stream<Arguments> seconds() {
        return IntStream.range(0, 60)
                .mapToObj(Arguments::of);
    }

    @Test
    public void testMomentFixation() {
        org.time.test.NowTest.withCurrentMoment((TrashRunnable) (() -> {
            OffsetDateTime expected = offsetDateTime();
            Thread.sleep(5);
            Assertions.assertEquals(offsetDateTime(), expected);
        }));
    }

    @ParameterizedTest
    @MethodSource("seconds")
    public void testSpecificMomentFixation(int seconds) {
        String paddedSeconds = "%2s".formatted(seconds).replace(' ', '0');
        withUtcMoment(Instant.ofEpochSecond(seconds), (TrashRunnable) (() -> {
            OffsetDateTime expected = OffsetDateTime.parse("1970-01-01T00:00:%sZ".formatted(paddedSeconds));
            Assertions.assertEquals(offsetDateTime(), expected);
        }));
    }
}
