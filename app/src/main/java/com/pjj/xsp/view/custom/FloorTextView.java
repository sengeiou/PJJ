package com.pjj.xsp.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.pjj.xsp.R;
import com.pjj.xsp.utils.TextViewUtils;

/**
 * Create by xinheng on 2018/11/01。
 * describe：
 */
public class FloorTextView extends View {
    private int smallTextSize = 15;
    private String smallText;
    private int smallTextColor = Color.BLACK;
    private String text;
    private int textSize = 20;
    private int textColor = Color.BLACK;
    private int paddingH = 5;
    private Paint paint, paintSmall;

    public FloorTextView(Context context) {
        this(context, null);
    }

    public FloorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FloorTextView, defStyle, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.FloorTextView_text_floor_h_padding:
                    paddingH = typedArray.getDimensionPixelSize(index, paddingH);
                    break;
                case R.styleable.FloorTextView_text_floor_small_text:
                    smallText = typedArray.getString(index);
                    break;
                case R.styleable.FloorTextView_text_floor_small_text_color:
                    smallTextColor = typedArray.getColor(index, smallTextColor);
                    break;
                case R.styleable.FloorTextView_text_floor_small_text_size:
                    smallTextSize = typedArray.getDimensionPixelSize(index, smallTextSize);
                    break;
                case R.styleable.FloorTextView_text_floor_text_color:
                    textColor = typedArray.getColor(index, textColor);
                    break;
                case R.styleable.FloorTextView_text_floor_text_size:
                    textSize = typedArray.getDimensionPixelSize(index, textSize);
                    break;
                case R.styleable.FloorTextView_text_floor_text:
                    text = typedArray.getString(index);
                    break;
            }
        }
        typedArray.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paintSmall = new Paint(paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int textHeight = 0;
        int textWidth = 0;
        if (!TextViewUtils.isEmpty(text)) {
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            float baseLine = TextViewUtils.getBaseLine(getMeasuredHeight(), paint);
            Rect rect = TextViewUtils.getRectText(text, paint);
            textWidth = rect.width();

            canvas.drawText(text, getMeasuredWidth() - textWidth - 34, baseLine, paint);
        }
        if (!TextViewUtils.isEmpty(smallText)) {
            paintSmall.setTextSize(smallTextSize);
            paintSmall.setColor(smallTextColor);
            float baseLine;
            if (textHeight == 0) {
                baseLine = TextViewUtils.getBaseLine(getMeasuredHeight(), paintSmall);
            } else {
                int smallTextHeight = TextViewUtils.getTextHeight(smallText, paintSmall);
                float textBottom = (getMeasuredHeight() - textHeight) / 2f + textHeight;
                float smallTextTop = textBottom - smallTextHeight;
                baseLine = TextViewUtils.getBaseLine(smallTextHeight, paintSmall) + smallTextTop - 30;
            }
            canvas.drawText(smallText, getPaddingLeft() + textWidth + paddingH, baseLine, paintSmall);
        }
    }

    public void setText(String text) {
        this.text = text;
        if (getMeasuredWidth() > 0) {
            invalidate();
        }
    }
}
