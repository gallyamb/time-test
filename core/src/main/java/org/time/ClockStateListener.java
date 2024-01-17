package org.time;

/**
 * A services implementing this interface will be notified about clock state
 */
public interface ClockStateListener {
    /**
     * Called when clock is changed from JVM default one
     */
    void notifyClockChanged();

    /**
     * Called when clock is reset to the JVM default
     */
    void notifyClockReset();
}
