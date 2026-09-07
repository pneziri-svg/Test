package com.ipeproing.countdowntv;

import android.service.dreams.DreamService;
import android.content.SharedPreferences;
import android.os.Handler;

public class CountdownDreamService extends DreamService {
    private final Handler handler = new Handler();
    private CountdownView view;
    private SharedPreferences prefs;

    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            String name = prefs.getString("project", "PROJECT COUNTDOWN");
            long fallback = System.currentTimeMillis() + 100L * 24 * 60 * 60 * 1000;
            long target = prefs.getLong("target", fallback);
            view.render(name, target);
            handler.postDelayed(this, 1000);
        }
    };

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
        setScreenBright(true);
        prefs = CountdownSettings.open(this);
        view = new CountdownView(this);
        view.setShowHint(false);
        setContentView(view);
        handler.post(ticker);
    }

    @Override public void onDetachedFromWindow() {
        handler.removeCallbacks(ticker);
        super.onDetachedFromWindow();
    }
}
