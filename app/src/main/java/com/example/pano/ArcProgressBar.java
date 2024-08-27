package com.example.pano;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
public class ArcProgressBar extends View {
    private Paint paint;
    private RectF rectF;
    private float progress;

    public ArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20f);
        rectF = new RectF(100, 100, 500, 500); // Adjust size as needed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rectF, 270, progress, false, paint); // Start from top (270 degrees)
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}