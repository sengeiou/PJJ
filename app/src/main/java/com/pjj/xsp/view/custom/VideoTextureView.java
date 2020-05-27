package com.pjj.xsp.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import com.pjj.xsp.utils.Log;
import android.view.TextureView;

/**
 * Create by xinheng on 2018/10/27。
 * describe：
 */
public class VideoTextureView extends TextureView {
    private String TAG = "1TAG";
    /**
     * 视频大小
     */
    private int videoWidth, videoHeight;

    public VideoTextureView(Context context) {
        super(context);
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoSize(int width, int height, int rotation) {
        setRotation(rotation);


        if (width == videoWidth && height == videoHeight) {
            return;
        }
        if (rotation == 90 || rotation == 270) {
            if (width <= height) {
                videoWidth = width;
                videoHeight = height;
            }else{
                videoWidth=0;
                videoHeight=0;
            }
        } else {
            if (width >= height) {
                videoWidth = width;
                videoHeight = height;
            }else{
                videoWidth=0;
                videoHeight=0;
            }
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*int viewRotation = (int) getRotation();
        Log.e("TAG", "onMeasure: " + viewRotation);
        if(((viewRotation==90||viewRotation==270)&&videoWidth>videoHeight)||
                viewRotation ==0&&videoHeight>videoWidth  ){
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        //int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("TAG", "onMeasure mode: " + sizeWidth + ", " + sizeHeight);
        int width, height;
        if (modeWidth == MeasureSpec.EXACTLY) {
            if (videoWidth > 0 && videoHeight > 0) {
                width = sizeWidth;
                height = (int) (videoHeight * 1f / videoWidth * sizeWidth);
            } else {
                width = sizeWidth;
                height = sizeHeight;
            }
        } else {
            if (videoWidth > 0 && videoHeight > 0) {
                width = videoWidth;
                height = videoHeight;
            } else {
                //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                //return;
                width = sizeWidth;
                height = sizeHeight;
            }
        }
        Log.e("TAG", "onMeasure old : " + width + ", " + height);
        if(viewRotation==90||viewRotation==270){
            int s=width;
            width=height;
            height=s;
        }
        Log.e("TAG", "onMeasure new : " + width + ", " + height);

        setMeasuredDimension(width, height);*/
        int viewRotation = (int) getRotation();
        // 如果判断成立，则说明显示的TextureView和本身的位置是有90度的旋转的，所以需要交换宽高参数。
        if (viewRotation == 90 || viewRotation == 270) {
            int tempMeasureSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempMeasureSpec;
        }

        Log.i(TAG, "onMeasure " + " [" + this.hashCode() + "] ");
        Log.i(TAG, "viewRotation = " + viewRotation);

        int videoWidth = this.videoWidth;
        int videoHeight = this.videoHeight;

        Log.i(TAG, "videoWidth = " + videoWidth + ", " + "videoHeight = " + videoHeight);
        if (videoWidth > 0 && videoHeight > 0) {
            Log.i(TAG, "videoWidth / videoHeight = " + videoWidth / videoHeight);
        }

        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);
        if (videoWidth > 0 && videoHeight > 0) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            Log.i(TAG, "widthMeasureSpec  [" + MeasureSpec.toString(widthMeasureSpec) + "]");
            Log.i(TAG, "heightMeasureSpec [" + MeasureSpec.toString(heightMeasureSpec) + "]");

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if (videoWidth * height < width * videoHeight) {
                    width = height * videoWidth / videoHeight;
                } else if (videoWidth * height > width * videoHeight) {
                    height = width * videoHeight / videoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * videoHeight / videoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * videoWidth / videoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = videoWidth;
                height = videoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        Log.i(TAG, "viewWidth = " + width + ", " + "viewHeight = " + height);
        Log.i(TAG, "viewWidth / viewHeight = " + width / height);
        setMeasuredDimension(width, height);
    }
}
