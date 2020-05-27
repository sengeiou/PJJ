package com.pjj.xsp.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by XinHeng on 2019/02/25.
 * describe：便民左边
 */
public class ScreenLeftView extends View {
    private int colorBg = Color.parseColor("#0172AE");
    private Path pathOutline = new Path();
    private float rate = 1f;
    private Paint paint;
    private Paint paintWhite;

    private void init() {
        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paintWhite = new Paint(paint);
        paintWhite.setColor(Color.WHITE);
    }

    public ScreenLeftView(Context context) {
        super(context);
        init();
    }

    public ScreenLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScreenLeftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rate = getMeasuredHeight() / 180f;
        setPathPoint(pathOutline);
    }

    private void setPathPoint(Path path) {
        path.reset();
        path.moveTo(0f, 0f);
        path.lineTo(64 * rate, 0f);
        path.lineTo(89 * rate, 90 * rate);
        path.lineTo(64 * rate, getMeasuredHeight());
        path.lineTo(0f, getMeasuredHeight());
        path.close();
    }

    public void setColorBg(int colorBg) {
        this.colorBg = colorBg;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        Path path = new Path();
        setPathPoint(path);
        paint.setColor(colorBg);
        canvas.drawPath(path, paint);

        Path pathTriangle = new Path();
        pathTriangle.moveTo(60 * rate, 34 * rate);
        pathTriangle.lineTo(74 * rate, 90 * rate);
        pathTriangle.lineTo(60 * rate, 145 * rate);
        pathTriangle.close();
        canvas.drawPath(pathTriangle, paintWhite);

        Path pathLine = new Path();
        pathLine.moveTo(57 * rate, 0f);
        pathLine.lineTo(60 * rate, 0f);
        pathLine.lineTo(85 * rate, 90 * rate);
        pathLine.lineTo(60 * rate, getMeasuredHeight());
        pathLine.lineTo(57 * rate, getMeasuredHeight());
        pathLine.lineTo(82 * rate, 90 * rate);
        pathLine.close();
        canvas.drawPath(pathLine, paintWhite);
    }
}
