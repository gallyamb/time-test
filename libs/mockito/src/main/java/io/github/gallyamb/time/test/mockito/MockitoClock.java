package io.github.gallyamb.time.test.mockito;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;

import io.github.gallyamb.time.ClockHandler;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

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
    private MockedStatic<Clock> clock;

    @Override
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public void setClock(Clock newClock) {
        if (clock == null) {
            clock = Mockito.mockStatic(Clock.class, InvocationOnMock::callRealMethod);
        } else {
            clock.reset();
        }

        clock.when(Clock::systemUTC)
                .thenReturn(newClock.withZone(ZoneOffset.UTC));
        clock.when(Clock::systemDefaultZone).thenReturn(newClock);
        clock.when(() -> Clock.system(Mockito.any(ZoneId.class)))
                .thenAnswer(invocation -> {
                    ZoneId zoneId = invocation.getArgument(0, ZoneId.class);
                    return newClock.withZone(zoneId);
                });
    }

    @Override
    public void reset() {
        if (clock != null) {
            clock.close();
            clock = null;
        }
    }
}
