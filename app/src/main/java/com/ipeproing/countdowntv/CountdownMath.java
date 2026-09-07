package com.ipeproing.countdowntv;
final class CountdownMath {
    static long remainingSeconds(long target, long now) {
        return target <= now ? 0 : (target - now + 999) / 1000;
    }
}
