package com.ipeproing.countdowntv;
import org.junit.Test;
import static org.junit.Assert.*;
public class CountdownMathTest {
    @Test public void downtimeIsDeductedFromAbsoluteDeadline() {
        long now = 1700000000000L;
        long target = now + 124L * 86400000L;
        assertEquals(124L * 86400L, CountdownMath.remainingSeconds(target, now));
        assertEquals(122L * 86400L, CountdownMath.remainingSeconds(target, now + 2L * 86400000L));
    }
    @Test public void deadlineAndPastClampToZero() {
        assertEquals(0, CountdownMath.remainingSeconds(1000,1000));
        assertEquals(0, CountdownMath.remainingSeconds(1000,2000));
    }
    @Test public void roundsPartialSecondUpAndRespondsToClockCorrection() {
        assertEquals(1, CountdownMath.remainingSeconds(1001,1000));
        assertEquals(61, CountdownMath.remainingSeconds(61001,1000));
        assertEquals(62, CountdownMath.remainingSeconds(61001,0));
    }
}
