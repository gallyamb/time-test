package org.time.internal;

import java.util.List;
import java.util.ServiceLoader;

import org.time.ClockHandler;
import org.time.impl.CompositeClockHandler;
import org.time.impl.SystemClockHandler;

/**
 * <b>Important notice!</b> If you are unlucky and cannot use JPMS to prevent you from seeing this message, I'm the
 * last guard between you and undefined behaviour. Please, do not use this class in your code. It's solely designed to
 * be used within known libraries only
 * <p>
 * If you'll use an {@link #INSTANCE instance} of {@link ClockHandler} even after my alert, then be sure you understand
 * what are you doing
 */
public class InternalClockHandler {
    public static final ClockHandler INSTANCE;

    static {
        List<ClockHandler> clockHandlers = ServiceLoader.load(ClockHandler.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();
        if (clockHandlers.isEmpty()) {
            INSTANCE = new SystemClockHandler();
        } else {
            INSTANCE = new CompositeClockHandler(clockHandlers);
        }
    }
}
