package com.pjj.xsp.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PasswordDialog {
    private EditText editText;
    private Context context;
    private boolean isShow;
    private View contentView;
    private final WindowManager wm;
    private final WindowManager.LayoutParams para;

    public PasswordDialog(Context context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //dp30 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
        para = new WindowManager.LayoutParams();
        para.height = WindowManager.LayoutParams.WRAP_CONTENT;
        para.width = WindowManager.LayoutParams.WRAP_CONTENT;
        para.format = 1;
        para.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        para.gravity = Gravity.CENTER;
        para.type = WindowManager.LayoutParams.TYPE_TOAST;
        contentView = createContentView();
        //show();
    }

    public void show() {
        wm.addView(contentView, para);
        isShow = true;
    }

    public boolean isShowing() {
        return isShow;
    }

    public void dismiss() {
        if (isShow && null != contentView) {
            wm.removeView(contentView);
            isShow = false;
        }
    }

    private View createContentView() {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setPadding(20, 0, 20, 0);
        ll.setBackground(new ColorDrawable(Color.WHITE));
        ll.addView(getET());
        ll.addView(getLine());
        ll.addView(getTV());
        return ll;
    }

    private EditText getET() {
        EditText et = new EditText(context);
        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //et.setBackground(new ColorDrawable(Color.WHITE));
        et.setTextColor(Color.BLACK);
        et.setMaxLines(1);
        et.setTextSize(15);
        et.setPadding(0, 10, 0, 10);
        et.setEllipsize(TextUtils.TruncateAt.END);
        et.setGravity(Gravity.CENTER_VERTICAL);
        editText = et;
        et.setHint("请输入密码");
        et.setHintTextColor(Color.GRAY);
        return et;
    }

    private View getLine() {
        View view = new View(context);
        view.setBackground(new ColorDrawable(Color.GRAY));
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics())));
        return view;
    }

    private TextView getTV() {
        TextView tv = new TextView(context);
        tv.setText("确定");
        tv.setTextSize(15);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0, 10, 0, 10);
        tv.setTextColor(Color.BLACK);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                Log.e("TAG", "onClick: " + s);
                if (s.length() > 0 && null != onPasswordDialogListener) {
                    onPasswordDialogListener.sureClick(s);
                }
            }
        });
        return tv;
    }

    private OnPasswordDialogListener onPasswordDialogListener;

    public void setOnPasswordDialogListener(OnPasswordDialogListener onPasswordDialogListener) {
        this.onPasswordDialogListener = onPasswordDialogListener;
    }

    public interface OnPasswordDialogListener {
        void sureClick(String text);
    }
}
