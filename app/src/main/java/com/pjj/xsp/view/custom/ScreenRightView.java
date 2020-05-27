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
 * describe：便民右边
 */
public class ScreenRightView extends View {
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

    public ScreenRightView(Context context) {
        super(context);
        init();
    }

    public ScreenRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScreenRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setColorBg(int colorBg) {
        this.colorBg = colorBg;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rate = getMeasuredHeight() / 180f;
        setPathPoint(pathOutline);
    }

    private void setPathPoint(Path path) {
        path.moveTo(15 * rate, 0f);
        path.lineTo(getMeasuredWidth(), 0f);
        path.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path.lineTo(15 * rate, getMeasuredHeight());
        path.lineTo(38 * rate, 90 * rate);
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        Path path = new Path();
        setPathPoint(path);
        paint.setColor(colorBg);
        canvas.drawPath(path, paint);

        Path pathLine = new Path();
        pathLine.moveTo(19 * rate, 0f);
        pathLine.lineTo(22 * rate, 0f);
        pathLine.lineTo(45 * rate, 90 * rate);
        pathLine.lineTo(22 * rate, getMeasuredHeight());
        pathLine.lineTo(19 * rate, getMeasuredHeight());
        pathLine.lineTo(42 * rate, 90 * rate);
        pathLine.close();
        canvas.drawPath(pathLine, paintWhite);
    }
}
