package com.ipeproing.countdowntv;
import android.content.Context;
import android.content.SharedPreferences;
final class CountdownSettings {
    static SharedPreferences open(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("countdown", Context.MODE_PRIVATE);
        if (!prefs.contains("target")) {
            if (!prefs.edit().putLong("target", System.currentTimeMillis() + 100L * 86400000L)
                    .putString("project", "KASTRATI").commit()) {
                throw new IllegalStateException("Unable to persist initial countdown");
            }
        }
        return prefs;
    }
}
