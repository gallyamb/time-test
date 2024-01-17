package org.time.test.junit5;

import java.lang.annotation.*;

/**
 * Allows to specify time for a test or test suite
 * <p>
 * If {@link #value()} is present, then specified datetime and timezone is used, or current datetime and timezone
 * otherwise
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedTime {
    /**
     * Either {@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME} or
     * {@link java.time.format.DateTimeFormatter#ISO_ZONED_DATE_TIME} formatted datetime
     */
    String value() default "";
}
