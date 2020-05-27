package com.pjj.xsp.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Create by xinheng on 2018/10/30。
 * describe：
 */
@SuppressLint("AppCompatCustomView")
public class LineTextView extends TextView {
    private int lineColor = Color.BLACK;

    public LineTextView(Context context) {
        super(context);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float dp1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(dp1);
        paint.setColor(lineColor);
        float y = getMeasuredHeight() - dp1;
        canvas.drawLine(getPaddingLeft(), y, getMeasuredWidth() - getPaddingRight(), y, paint);
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        invalidate();
    }
}
