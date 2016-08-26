package com.ligthblue.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 何成龙 on 2016/8/23.
 */
public class BaseActivity extends CaptureActivity {
    public String TAG = getClass().getSimpleName();

    /**
     * 获取上下文
     *
     * @return
     */
    public Context getContext() {
        return this;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则返回null
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 获取系统状态栏高度
     *
     * @return 系统状态栏高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 弹窗信息
     *
     * @param message 弹窗信息
     */
    public void toask(Object message) {
        Toast.makeText(this, message + "", Toast.LENGTH_LONG).show();
    }

    /**
     * 弹窗控件内容信息
     *
     * @param view
     */
    public void toaskView(View view) {
        Toast.makeText(this, viewGetValue(view), Toast.LENGTH_LONG).show();
    }

    /**
     * 获取控件的值
     *
     * @param view
     * @return 控件的值
     */
    public String viewGetValue(View view) {
        TextView tv = (TextView) view;
        return tv.getText().toString();
    }

    /**
     * 初始化沉浸状态栏
     */
    public void initStatusBar(View ll) {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
            ll.setVisibility(View.VISIBLE);
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            ll.setVisibility(View.GONE);
        }
    }

    /**
     * 跳转到页面
     */
    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 新建一个意图
     *
     * @param cls
     * @return Intent
     */
    public Intent newIntent(Class<?> cls) {
        return new Intent(this, cls);
    }

    /**
     * 发送广播
     *
     * @param action
     */
    public void sendBroadcast(String action) {
        sendBroadcast(new Intent(action));
    }

    /**
     * 设置控件的宽度
     *
     * @param view
     * @param width
     */
    public void setViewWidth(View view, int width) {
        setViewLayoutParams(view, width, view.getLayoutParams().height);
    }

    /**
     * 设置控件的高度
     *
     * @param view
     * @param height
     */
    public void setViewHeigth(View view, int height) {
        setViewLayoutParams(view, view.getLayoutParams().width, height);
    }

    /**
     * 设置控件的宽度和高度
     *
     * @param view
     * @param width
     * @param height
     */
    public void setViewLayoutParams(View view, int width, int height) {
        if (view == null) {
            throw new NullPointerException("view can't be null");
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (width > -1) {
            lp.width = width;
        }
        if (height > -1) {
            lp.height = height;
        }
        view.setLayoutParams(lp);
    }

    /**
     * 获取手机屏幕宽度
     *
     * @return
     */
    public int getWidth() {
        return getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * 获取手机屏幕高度
     *
     * @return
     */
    public int getHeight() {
        return getWindowManager().getDefaultDisplay().getHeight();
    }

    @Override
    protected void onPhotoTaked(String photoPath) {

    }
}
