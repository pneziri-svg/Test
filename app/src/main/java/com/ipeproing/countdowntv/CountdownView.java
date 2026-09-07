package com.ipeproing.countdowntv;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CountdownView extends LinearLayout {
    private final TextView project;
    private final TextView days;
    private final TextView time;
    private final TextView endDate;

    public CountdownView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setPadding(40, 30, 40, 30);
        setBackgroundColor(Color.rgb(8, 12, 20));

        project = label(34, Typeface.BOLD);
        days = label(128, Typeface.BOLD);
        TextView remaining = label(28, Typeface.BOLD);
        remaining.setText("DAYS REMAINING");
        time = label(54, Typeface.BOLD);
        TextView units = label(18, Typeface.NORMAL);
        units.setText("HOURS        MIN        SEC");
        endDate = label(24, Typeface.NORMAL);
        TextView brand = label(20, Typeface.BOLD);
        brand.setText("IPE-PROING");
        brand.setPadding(0, 28, 0, 0);

        addView(project);
        addView(days);
        addView(remaining);
        addView(time);
        addView(units);
        addView(endDate);
        addView(brand);
    }

    private TextView label(float sp, int style) {
        TextView v = new TextView(getContext());
        v.setTextColor(Color.WHITE);
        v.setTextSize(sp);
        v.setGravity(Gravity.CENTER);
        v.setTypeface(Typeface.create("sans", style));
        return v;
    }

    public void render(String projectName, long targetMillis) {
        long diff = Math.max(0, targetMillis - System.currentTimeMillis());
        long totalSeconds = diff / 1000;
        long d = totalSeconds / 86400;
        long h = (totalSeconds % 86400) / 3600;
        long m = (totalSeconds % 3600) / 60;
        long s = totalSeconds % 60;
        project.setText(projectName);
        days.setText(String.valueOf(d));
        time.setText(String.format(Locale.US, "%02d : %02d : %02d", h, m, s));
        String formatted = new SimpleDateFormat("dd.MM.yyyy  HH:mm", Locale.US).format(new Date(targetMillis));
        endDate.setText("PROJECT END: " + formatted);
    }
}
