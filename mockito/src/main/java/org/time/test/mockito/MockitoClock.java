package org.time.test.mockito;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.time.ClockHandler;

/**
 * An implementation of {@link ClockHandler}, that internally uses {@link Mockito}
 *
 * <table>
 *     <caption>Pros&amp;Cons of this approach</caption>
 *     <tr>
 *         <th>Pros</th>
 *         <th>Cons</th>
 *     </tr>
 *     <tr>
 *         <td>All time instances can be obtained directly via {@link Clock}</td>
 *         <td>The clock is changed only in thread, that initiated clock change</td>
 *     </tr>
 *     <tr>
 *         <td>Can be used with existing codebase without immediate changes</td>
 *         <td>Does partial clock change, because {@link System#currentTimeMillis()} cannot be mocked via Mockito</td>
 *     </tr>
 *     <tr>
 *         <td>Current time <b>is</b> changed (partially, see cons) in libraries code</td>
 *         <td></td>
 *     </tr>
 * </table>
 */
public class MockitoClock implements ClockHandler {
    /**
     * Dummy way to implement singleton. The {@link java.util.ServiceLoader} always obtains the instance of the service
     * via constructor invocation (an exception is made only for a services loaded via JPMS module layer, but we do not
     * use it, because we want to be available for wide range developers)
     */
    private static volatile MockedStatic<Clock> CLOCK;

    @Override
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public void setClock(Clock newClock) {
        if (CLOCK == null) {
            CLOCK = Mockito.mockStatic(Clock.class, InvocationOnMock::callRealMethod);
        } else {
            CLOCK.reset();
        }

        CLOCK.when(Clock::systemUTC)
                .thenReturn(newClock.withZone(ZoneOffset.UTC));
        CLOCK.when(Clock::systemDefaultZone).thenReturn(newClock);
        CLOCK.when(() -> Clock.system(Mockito.any(ZoneId.class)))
                .thenAnswer(invocation -> {
                    ZoneId zoneId = invocation.getArgument(0, ZoneId.class);
                    return newClock.withZone(zoneId);
                });
    }

    @Override
    public void reset() {
        if (CLOCK != null) {
            CLOCK.close();
            CLOCK = null;
        }
    }
}
