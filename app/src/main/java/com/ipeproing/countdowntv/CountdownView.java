package com.ipeproing.countdowntv;
import android.content.Context;
import android.graphics.*;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Resolution-independent composition with TV-safe insets. */
public class CountdownView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Bitmap background;
    private final Bitmap logo;
    private String project = "KASTRATI";
    private long target;
    private boolean hint = true;
    public CountdownView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.project_background);
        logo = BitmapFactory.decodeResource(getResources(), R.drawable.kastrati_logo);
        setContentDescription("Kastrati TV countdown");
    }
    public void setShowHint(boolean show) { hint = show; }
    public void render(String name, long targetMillis) {
        project = name; target = targetMillis; invalidate();
    }
    private void text(Canvas c, String value, float x, float y, float size, int color, boolean bold, float maxWidth) {
        paint.setColor(color);
        paint.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        paint.setTextSize(size);
        float width = paint.measureText(value);
        if (width > maxWidth) paint.setTextSize(size * maxWidth / width);
        c.drawText(value, x, y, paint);
    }
    @Override protected void onDraw(Canvas c) {
        super.onDraw(c);
        float fill = Math.max(getWidth() / (float) background.getWidth(), getHeight() / (float) background.getHeight());
        float bw = background.getWidth() * fill, bh = background.getHeight() * fill;
        paint.setColor(Color.WHITE);
        c.drawBitmap(background, null, new RectF((getWidth()-bw)/2, (getHeight()-bh)/2,
                (getWidth()+bw)/2, (getHeight()+bh)/2), paint);
        c.drawColor(0x32040B15);
        float scale = Math.min(getWidth()/1920f, getHeight()/1080f);
        c.save();
        c.translate((getWidth()-1920*scale)/2, (getHeight()-1080*scale)/2);
        c.scale(scale, scale);
        paint.setColor(0xD90A1420);
        c.drawRoundRect(new RectF(96, 96, 890, 984), 28, 28, paint);
        paint.setColor(Color.WHITE);
        c.drawBitmap(logo, null, new RectF(140, 135, 218, 213), paint);
        text(c, "KASTRATI TV", 242, 188, 36, Color.WHITE, true, 570);
        text(c, project, 140, 294, 48, Color.WHITE, true, 700);
        paint.setColor(0xFFFF681D);
        c.drawRect(140, 325, 232, 331, paint);
        long seconds = CountdownMath.remainingSeconds(target, System.currentTimeMillis());
        text(c, String.valueOf(seconds/86400), 130, 543, 188, Color.WHITE, true, 704);
        text(c, "DITË TË MBETURA", 140, 597, 32, 0xFFFF843C, true, 700);
        text(c, String.format(Locale.US, "%02d : %02d : %02d", seconds/3600%24, seconds/60%60, seconds%60),
                140, 724, 82, Color.WHITE, true, 700);
        text(c, "ORË          MINUTA       SEKONDA", 140, 770, 25, 0xFFDCE2EA, false, 700);
        String date = new SimpleDateFormat("dd.MM.yyyy  HH:mm z", Locale.getDefault()).format(new Date(target));
        text(c, seconds == 0 ? "AFATI PËRFUNDOI" : "PËRFUNDIMI I PROJEKTIT", 140, 843, 25, 0xFFDCE2EA, true, 700);
        text(c, date, 140, 889, 34, Color.WHITE, false, 700);
        if (hint) text(c, "OK / MENU  •  Konfigurimi", 140, 947, 23, 0xFFCDD3DB, false, 700);
        c.restore();
    }
}
