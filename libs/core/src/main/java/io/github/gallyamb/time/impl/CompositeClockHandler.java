package io.github.gallyamb.time.impl;

import java.time.Clock;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

import io.github.gallyamb.time.ClockHandler;
import io.github.gallyamb.time.ClockStateListener;
import io.github.gallyamb.time.ThreadLocalClockHandler;

public class CompositeClockHandler implements ClockHandler {
    public static final Comparator<ClockHandler> THREAD_LOCAL_HANDLERS_LAST = (a, b) -> {
        boolean aIsLimited = a instanceof ThreadLocalClockHandler;
        boolean bIsLimited = b instanceof ThreadLocalClockHandler;
        if (aIsLimited == bIsLimited) {
            return 0;
        }
        return aIsLimited ? 1 : -1;
    };
    private final List<ClockStateListener> clockStateListeners = ServiceLoader.load(ClockStateListener.class)
            .stream()
            .map(ServiceLoader.Provider::get)
            .toList();
    private final Collection<ClockHandler> delegates;

    public CompositeClockHandler(Collection<ClockHandler> delegates) {
        this.delegates = delegates.stream()
                .sorted(THREAD_LOCAL_HANDLERS_LAST)
                .toList();
    }

    @Override
    public Clock getClock() {
        return delegates.iterator().next().getClock();
    }

    @Override
    public void setClock(Clock clock) {
        this.delegates.forEach(handler -> handler.setClock(clock));
        clockStateListeners.forEach(ClockStateListener::notifyClockChanged);
    }

    @Override
    public void reset() {
        this.delegates.forEach(ClockHandler::reset);
        clockStateListeners.forEach(ClockStateListener::notifyClockReset);
    }
}
