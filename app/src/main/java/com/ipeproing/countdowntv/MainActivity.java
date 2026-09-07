package com.ipeproing.countdowntv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.Calendar;

public class MainActivity extends Activity {
    private static final String PREFS = "countdown";
    private final Handler handler = new Handler();
    private CountdownView countdownView;
    private SharedPreferences prefs;
    private long targetMillis;
    private String projectName;

    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            countdownView.render(projectName, targetMillis);
            handler.postDelayed(this, 1000);
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        prefs = CountdownSettings.open(this);
        loadSettings();
        countdownView = new CountdownView(this);
        countdownView.setFocusable(true);
        countdownView.setOnClickListener(v -> showSettings());
        countdownView.setOnLongClickListener(v -> { showSettings(); return true; });
        setContentView(countdownView);
        countdownView.requestFocus();
    }

    private void loadSettings() {
        projectName = prefs.getString("project", "KASTRATI");
        long defaultTarget = System.currentTimeMillis() + 100L * 24 * 60 * 60 * 1000;
        targetMillis = prefs.getLong("target", defaultTarget);
    }

    private void showSettings() {
        final EditText name = new EditText(this);
        name.setHint("Emri i projektit");
        name.setText(projectName);
        name.setSingleLine(true);
        name.setFilters(new android.text.InputFilter[] { new android.text.InputFilter.LengthFilter(48) });

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        int p = 32;
        box.setPadding(p, p, p, p);
        box.addView(name);

        new AlertDialog.Builder(this)
                .setTitle("Konfigurimi")
                .setView(box)
                .setMessage("Shkruaj emrin, pastaj zgjidh datën dhe orën e përfundimit.")
                .setNegativeButton("Anulo", null)
                .setPositiveButton("Zgjidh datën", (d, w) -> {
                    String draftName = name.getText().toString().trim();
                    if (draftName.isEmpty()) draftName = "KASTRATI";
                    chooseDateTime(draftName);
                }).show();
    }

    private void chooseDateTime(final String draftName) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(targetMillis);
        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar chosen = Calendar.getInstance();
            chosen.setTimeInMillis(targetMillis);
            chosen.set(year, month, day);
            new TimePickerDialog(this, (timeView, hour, minute) -> {
                chosen.set(Calendar.HOUR_OF_DAY, hour);
                chosen.set(Calendar.MINUTE, minute);
                chosen.set(Calendar.SECOND, 0);
                chosen.set(Calendar.MILLISECOND, 0);
                long draftTarget = chosen.getTimeInMillis();
                if (!prefs.edit().putString("project", draftName).putLong("target", draftTarget).commit()) {
                    Toast.makeText(this, "Ruajtja dështoi. Provo përsëri.", Toast.LENGTH_LONG).show();
                    return;
                }
                projectName = draftName;
                targetMillis = draftTarget;
                countdownView.render(projectName, targetMillis);
                Toast.makeText(this, "Konfigurimi u ruajt", Toast.LENGTH_SHORT).show();
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_MENU) {
            showSettings();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    @Override protected void onResume() { super.onResume(); loadSettings(); handler.removeCallbacks(ticker); handler.post(ticker); }
    @Override protected void onPause() { handler.removeCallbacks(ticker); super.onPause(); }
}
