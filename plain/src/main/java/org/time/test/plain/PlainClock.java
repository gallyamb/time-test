package org.time.test.plain;

import java.time.Clock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.time.ClockHandler;

/**
 * An implementation of {@link ClockHandler}, that internally uses plain {@link Clock}
 *
 * <table>
 *     <caption>Pros&amp;Cons of this approach</caption>
 *     <tr>
 *         <th>Pros</th>
 *         <th>Cons</th>
 *     </tr>
 *     <tr>
 *         <td>Current time could be changed in all threads simultaneously</td>
 *         <td>All time instances have to be obtained via {@link org.time.Now}</td>
 *     </tr>
 *     <tr>
 *         <td></td>
 *         <td>
 *             Current time is <b>not</b> changed in libraries code (because they obtain current time directly
 *             from {@link Clock})
 *         </td>
 *     </tr>
 * </table>
 */
public class PlainClock implements ClockHandler {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile Clock clock = Clock.systemDefaultZone();

    @Override
    public Clock getClock() {
        lock.readLock().lock();
        try {
            return this.clock;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void setClock(Clock clock) {
        lock.writeLock().lock();
        try {
            this.clock = clock;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void reset() {
        lock.writeLock().lock();
        try {
            this.clock = Clock.systemDefaultZone();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
