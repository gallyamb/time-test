package io.github.gallyamb.time;

import java.time.*;

import io.github.gallyamb.time.internal.InternalClockHandler;

/**
 * This is the main class, that can provide current time in different representations
 * <p>
 * You can use methods of that class to fetch current time, or you can use static methods of the classes from
 * {@link java.time} directly. But you should be aware that direct usage of methods of classes from {@link java.time}
 * can lead to inability to manipulate current time in tests, if you're using certain implementations (such as
 * io.github.gallyamb.time.test:time-test-plain)
 * <p>
 * As a general advice: use methods of that class everywhere if you can and want, or use methods of that class only in
 * code, that, while testing, will be executed in thread different from test's original thread
 * <p>
 * Also, you should notice that currently there is no any implementation, that could manipulate the current time in code
 * that's not use this library in thread other than test's original thread
 *
 * <table>
 *     <caption>Do you able to change clock state?</caption>
 *     <tr>
 *         <th></th>
 *         <th>Current thread</th>
 *         <th>Another thread</th>
 *     </tr>
 *     <tr>
 *         <th>Your code</th>
 *         <td>+</td>
 *         <td>+</td>
 *     </tr>
 *     <tr>
 *         <th>3rd party code</th>
 *         <td>+</td>
 *         <td>-</td>
 *     </tr>
 * </table>
 */
public final class Now {
    private static Clock clock() {
        return InternalClockHandler.INSTANCE.getClock();
    }

    /**
     * @return current time in the {@link OffsetDateTime} representation with default offset
     */
    public static OffsetDateTime offsetDateTime() {
        return OffsetDateTime.now(clock());
    }

    /**
     * @param zone time zone at which {@link OffsetDateTime} will be obtained
     *
     * @return current time in the {@link OffsetDateTime} representation with offset specified by the {@code zone} time
     * zone
     */
    public static OffsetDateTime offsetDateTime(ZoneId zone) {
        return OffsetDateTime.now(clock().withZone(zone));
    }

    /**
     * @param offset zone offset at which {@link OffsetDateTime} will be obtained
     *
     * @return current time in the {@link OffsetDateTime} representation with specified {@code offset} offset
     */
    public static OffsetDateTime offsetDateTime(ZoneOffset offset) {
        return OffsetDateTime.now(clock().withZone(offset));
    }

    /**
     * @return current time in the {@link ZonedDateTime} representation with default time zone
     */
    public static ZonedDateTime zonedDateTime() {
        return ZonedDateTime.now(clock());
    }

    /**
     * @param zone time zone at which {@link ZonedDateTime} will be obtained
     *
     * @return current time in the {@link ZonedDateTime} representation with specified {@code zone} time zone
     */
    public static ZonedDateTime zonedDateTime(ZoneId zone) {
        return ZonedDateTime.now(clock().withZone(zone));
    }

    /**
     * @param offset zone offset at which {@link ZonedDateTime} will be obtained
     *
     * @return current time in the {@link ZonedDateTime} representation with time zone specified by {@code offset}
     * offset
     */
    public static ZonedDateTime zonedDateTime(ZoneOffset offset) {
        return ZonedDateTime.now(clock().withZone(offset));
    }

    /**
     * @return current local date/time at default time zone
     */
    public static LocalDateTime localDateTime() {
        return LocalDateTime.now(clock());
    }

    /**
     * @param zone time zone at which {@link LocalDateTime} will be obtained
     *
     * @return current local date/time at specified {@code zone} time zone
     */
    public static LocalDateTime localDateTime(ZoneId zone) {
        return LocalDateTime.now(clock().withZone(zone));
    }

    /**
     * @param offset zone offset at which {@link LocalDateTime} will be obtained
     *
     * @return current local date/time at specified {@code offset} offset
     */
    public static LocalDateTime localDateTime(ZoneOffset offset) {
        return LocalDateTime.now(clock().withZone(offset));
    }

    /**
     * @return current local date at default time zone
     */
    public static LocalDate localDate() {
        return LocalDate.now(clock());
    }

    /**
     * @param zone time zone at which {@link LocalDate} will be obtained
     *
     * @return current local date at specified {@code zone} time zone
     */
    public static LocalDate localDate(ZoneId zone) {
        return LocalDate.now(clock().withZone(zone));
    }

    /**
     * @param offset zone offset at which {@link LocalDate} will be obtained
     *
     * @return current local date at specified {@code offset} offset
     */
    public static LocalDate localDate(ZoneOffset offset) {
        return LocalDate.now(clock().withZone(offset));
    }

    /**
     * @return current local time at default time zone
     */
    public static LocalTime localTime() {
        return LocalTime.now(clock());
    }

    /**
     * @param zone time zone at which {@link LocalTime} will be obtained
     *
     * @return current local time at specified {@code zone} time zone
     */
    public static LocalTime localTime(ZoneId zone) {
        return LocalTime.now(clock().withZone(zone));
    }

    /**
     * @param offset zone offset at which {@link LocalTime} will be obtained
     *
     * @return current local time at specified {@code offset} offset
     */
    public static LocalTime localTime(ZoneOffset offset) {
        return LocalTime.now(clock().withZone(offset));
    }

    /**
     * @return current {@link Instant instant}
     */
    public static Instant instant() {
        return clock().instant();
    }

    /**
     * @return current time milliseconds
     *
     * @see System#currentTimeMillis()
     */
    public static long currentTimeMillis() {
        return clock().millis();
    }

    /**
     * @return current time seconds. Essentially, it's just current millis without millis part
     *
     * @see #currentTimeMillis()
     */
    public static long unixTimestamp() {
        return currentTimeMillis() / 1000;
    }
}
