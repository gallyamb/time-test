package io.github.gallyamb.time.test.junit.impl;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Optional;

import io.github.gallyamb.time.internal.InternalClockHandler;
import io.github.gallyamb.time.test.junit.FixedTime;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

public class TimeTestExecutionListener implements TestExecutionListener {
    private static Optional<FixedTime> getFixedTime(TestIdentifier testIdentifier) {
        return tryGetFixedTimeFromMethodSource(testIdentifier).or(() -> tryGetFixedTimeFromClassSource(testIdentifier));
    }

    private static Optional<FixedTime> tryGetFixedTimeFromClassSource(TestIdentifier testIdentifier) {
        return testIdentifier.getSource()
                .filter(ClassSource.class::isInstance)
                .map(ClassSource.class::cast)
                .flatMap(classSource -> findAnnotation(classSource.getJavaClass(), FixedTime.class));
    }

    private static Optional<FixedTime> tryGetFixedTimeFromMethodSource(TestIdentifier testIdentifier) {
        return testIdentifier.getSource()
                .filter(MethodSource.class::isInstance)
                .map(MethodSource.class::cast)
                .flatMap(methodSource -> findAnnotation(methodSource.getJavaMethod(), FixedTime.class)
                        .or(() -> findAnnotation(methodSource.getJavaClass(), FixedTime.class)));
    }

    private static Temporal extractOffsetDateTime(FixedTime fixedTime) {
        if (fixedTime.value().isEmpty()) {
            return OffsetDateTime.now();
        } else {
            try {
                return ZonedDateTime.parse(fixedTime.value());
            } catch (DateTimeParseException ignored) {
                try {
                    return OffsetDateTime.parse(fixedTime.value());
                } catch (DateTimeParseException ignored2) {
                    throw new DateTimeParseException(
                            "Supplied value is not valid ISO datetime: %s".formatted(fixedTime.value()),
                            fixedTime.value(),
                            0
                    );
                }
            }
        }
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (!testIdentifier.isTest()) {
            return;
        }

        getFixedTime(testIdentifier).ifPresent(fixedTime -> {
            Temporal dateTime = extractOffsetDateTime(fixedTime);

            ZoneId zoneId = dateTime.query(ZoneId::from);
            Instant instant = dateTime.query(Instant::from);

            InternalClockHandler.INSTANCE.setClock(Clock.fixed(instant, zoneId));
        });
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        getFixedTime(testIdentifier).ifPresent(fixedTime -> InternalClockHandler.INSTANCE.reset());
    }
}
