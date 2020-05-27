package com.pjj.xsp.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.pjj.xsp.R;

/**
 * Create by xinheng on 2018/10/30。
 * describe：
 */
public class ExplainTextView extends View {
    private boolean isLine;
    private int textLinePadding = 10;
    private int textSize = 15;
    private int textColor = Color.BLACK;
    private int lineColor = Color.BLACK;
    private Paint paint;
    public ExplainTextView(Context context) {
        this(context, null);
    }

    public ExplainTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExplainTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExplainTextView, defStyle, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index){
                case R.styleable.ExplainTextView_text_explain_size:
                    textSize=typedArray.getDimensionPixelSize(index,textSize);
                    break;
                case R.styleable.ExplainTextView_text_explain_color:
                    textColor=typedArray.getColor(index,textColor);
                    break;
                case R.styleable.ExplainTextView_text_explain_line_padding:
                    textLinePadding=typedArray.getDimensionPixelSize(index,textLinePadding);
                    break;
                case R.styleable.ExplainTextView_text_explain_line_color:
                    lineColor=typedArray.getColor(index,lineColor);
                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        float textHeight = (getMeasuredHeight() - textLinePadding * 4f) / 3;
        //canvas.drawText("电梯维保单位：");

    }
}
