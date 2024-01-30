package io.github.gallyamb.time.test.junit;

import java.lang.annotation.*;

/**
 * Allows to specify time for a test or test suite. This annotation could be used both at the class level and method
 * level. The algorithm used to determine applied annotation is:
 * <ol>
 *     <li>see if {@link FixedTime annotation} is present at the method level</li>
 *     <li>if it is, apply specified datetime</li>
 *     <li>otherwise see if {@link FixedTime annotation} is present at the class level</li>
 *     <li>if it is, apply specified datetime</li>
 *     <li>otherwise do not change current clock state at all</li>
 * </ol>
 * <p>
 * The algorithm used to determine datetime from annotation is:
 * <ol>
 *     <li>if {@link #value()} is empty, then use current system default datetime (in {@link java.time.ZonedDateTime} form)</li>
 *     <li>else try to parse {@link #value()} as {@link java.time.ZonedDateTime}</li>
 *     <li>if it's parsed successfully use that datetime</li>
 *     <li>else try to parse {@link #value()} as {@link java.time.OffsetDateTime}</li>
 *     <li>if it's parsed successfully use that datetime</li>
 *     <li>otherwise throw an exception</li>
 * </ol>
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
     * {@link java.time.format.DateTimeFormatter#ISO_ZONED_DATE_TIME} formatted datetime<br/>
     * <b>or</b><br/>
     * leave empty to apply current system default datetime
     */
    String value() default "";
}
