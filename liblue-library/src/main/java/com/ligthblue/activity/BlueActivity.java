package com.ligthblue.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ligthblue.R;
import com.ligthblue.fragment.BaseFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;


/**
 * 基类activity hcl 20160224
 */
public class BlueActivity extends CaptureActivity {
    public String TAG = getClass().getSimpleName();// 标签
    protected View contentView;// 子容器
    private LinearLayout ll_content;// 父容器
    public TextView tv_center;// 标题
    public LinearLayout ll_bar;//状态栏
    public ImageView iv_back;// 后退按钮
    public ImageView iv_rigth;// 右边按钮
    public static Context context;// 上下文
    public TextView tv_rigth;//右边文字
    public Button bt_rigth;//右边按钮
    BaseFragment currentFragment;
    int code = -1;
    static String fileName = "";

    public static int i15 = 15;//小号字体
    public static int i20 = 20;//默认字体
    public static int i25 = 25;//默认字体

    public static int i30 = 30;
    public static int i40 = 40;

    public static final String ISPAD = "isPad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        initXutils();
        initBaseView();
        code = getIntent().getIntExtra("code", 1);
    }

    @Override
    protected void onPhotoTaked(String photoPath) {

    }

    private void initBaseView() {
        // TODO Auto-generated method stub
        context = this;
        fileName = getResources().getString(R.string.app_name);//
//        ViewUtils.inject(this);
//        registerReceiver(logout, new IntentFilter("logout"));
        tv_center = (TextView) findViewById(R.id.tv_center);
        ll_bar = (LinearLayout) findViewById(R.id.ll_bar);
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        iv_rigth = (ImageView) findViewById(R.id.iv_rigth);
//        ll_content = (LinearLayout) findViewById(R.id.ll_content);
//        tv_rigth = (TextView) findViewById(R.id.tv_rigth);
//        bt_rigth = (Button) findViewById(R.id.bt_rigth);
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                onclick_iv_back();
//            }
//        });
//        if (App.getUser()!=null){//说明已经保存了用户的登录信息
//            requestParams.put("user_id",App.getUser().getId());
//            requestParams.put("id",App.getUser().getId());
//        }
    }

    /**
     * 广播接受器，当收到logout广播时关闭页面
     */
    BroadcastReceiver logout = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            finish();
        }
    };

    /**
     * 设置标题
     *
     * @param string
     */
    public void setTitleName(String string) {
        tv_center.setText(string);
    }

    /**
     * 加入页面内容布局
     *
     * @param layoutId 布局编号
     */
    protected void contentView(int layoutId) {
        contentView = getLayoutInflater().inflate(layoutId, null);
        if (ll_content.getChildCount() > 0) {
            ll_content.removeAllViews();
        }
        if (contentView != null) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            ll_content.addView(contentView, params);
        }
    }

    /**
     * 初始化xutils
     */
    public void initXutils() {
        TAG = getClass().getSimpleName();
//        com.lidroid.xutils.ViewUtils.inject(this);//初始化xutils框架
        statusBar(ll_bar);
    }

    /**
     * 输出 i 级日志信息
     *
     * @param o
     */
    public void log_i(Object o) {
        Log.i(TAG, o + "");
    }

    /**
     * 输出 w 级日志信息
     *
     * @param o
     */
    public void log_w(Object o) {
        Log.w(TAG, o + "");
    }

    /**
     * 提供给子类重写的点击事件方法
     *
     * @param view
     */
    public void onClick(View view) {
    }

    /**
     * 判断控件是否为空
     *
     * @param view
     * @return true控件为空，false控件不为空
     */
    public boolean viewIsNull(View view) {
        if (view == null) {
            return true;
        }
        TextView tv = (TextView) view;
        return tv.getText().toString().equals("");
    }

    /**
     * 把控件内的值转化为double型
     *
     * @param view
     * @return double
     */
    public double viewValueFormatDouble(View view) {
        double i = 0;
        TextView tv = (TextView) view;
        if (viewIsNull(view)) {
            i = 0;
        } else {
            i = Double.valueOf(tv.getText().toString());
        }
        return i;
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
    public void statusBar(View ll) {
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

    public void statusBar() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ll_bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
            ll_bar.setVisibility(View.VISIBLE);
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            ll_bar.setVisibility(View.GONE);
        }
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
     * 打开软键盘
     *
     * @param et 获取焦点的编辑框
     */
    public void openInput(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 关闭软键盘
     *
     * @param et 关闭编辑框的键盘
     */
    public void closeInput(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 保存一个变量
     *
     * @param name  变量名
     * @param value 变量值
     */
    public static void setString(String name, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value).commit();
    }

    /**
     * 取出一个变量
     *
     * @param name 变量名
     */
    public static String getString(String name) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                .getString(name, "");
    }

    /**
     * 清除一个变量
     *
     * @param name 变量名
     */
    public static void cleanString(String name) {
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit()
                .remove(name).commit();
    }

    /**
     * 字符串转化为实体类
     *
     * @param strJson
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(String strJson, Class<T> clazz) {
        log_i(strJson);
        return new Gson().fromJson(strJson, clazz);
    }

    /**
     * data数组转化为实体类
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(byte[] bytes, Class<T> clazz) {
        String strJson = new String(bytes);
        log_i(new String(bytes));
        return new Gson().fromJson(strJson, clazz);
    }

    /**
     * json转化为实体类
     *
     * @param strJson
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(JSONObject strJson, Class<T> clazz) {
        log_i(strJson);
        return new Gson().fromJson(strJson.toString(), clazz);
    }

    /**
     * 2016 03 15
     * 设置控件的选择状态，若当前为true则设为false，否则设为true
     *
     * @param view
     */
    public void autoSelect(View view) {
        if (view.isSelected()) {
            view.setSelected(false);
        } else {
            view.setSelected(true);
        }
    }

    /**
     * 2016 03 15
     * 判断控件里的值是否为手机号
     *
     * @param view
     * @return false 手机号正确
     */
    boolean isPhoneNum(View view) {
        String value = viewGetValue(view);
        String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
//        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
//        if (!m.find()) {
//            App.toask("请输入正确的手机号");
//        }
        return m.find();
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
     * 弹窗信息
     *
     * @param view
     */
    public void toaskView(View view) {
        Toast.makeText(this, viewGetValue(view), Toast.LENGTH_LONG).show();
    }


    /**
     * 获取网络图片
     *
     * @param url
     * @return bitmap
     */
    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(5000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    /**
     * 请求网络图片
     *
     * @param url
     * @param iv
     */
    void getUrlBitmap(Object url, ImageView iv) {
        if (url == null || url.equals("")) {
            return;
        }
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.c5)
//                .showImageOnFail(R.drawable.c5)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(url.toString(), iv, options);
//        BaseRequest.getImage(url, 0, 0, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                iv.setImageBitmap(bitmap);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
////                toask("加载图片失败");
//            }
//        });
    }

    // 检查金额是否有误
    boolean checkMoney(View view) {
        if (viewGetValue(view).equals("0.0")
                || viewGetValue(view).equals("0.")
                || viewGetValue(view).equals("0.00")) {
            return false;
        } else {
            String str = viewGetValue(view);
            String regex = "^[+]?(([1-9]\\d*[.]?)|(0.))(\\d{0,2})?$";//
            return str.matches(regex);
        }
    }

    String formatDate(String str) {
        if (str == null) {
            return "";
        }
        return str.substring(5, str.length() - 3);
    }


    /**
     * 判断字符串是否为空或者空字符串
     *
     * @param str
     * @return true代表为空或空字符串
     */
    public boolean stringIsNull(Object str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    public String stringToDouble(View view) {
        // TODO Auto-generated method stub
        TextView tv = (TextView) view;
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(Double.valueOf(viewGetValue(tv)));
    }


    /**
     * 跳转到页面
     */
    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 跳转页面并保存code
     *
     * @param code
     * @param activityClass
     */
    public void openActivityCode(Class<?> activityClass, int code) {
        startActivity(new Intent(this, activityClass).putExtra("code", code));
    }

    /**
     * 跳转页面并保存序列化对象data
     *
     * @param activityClass
     * @param serializableData
     */
    public void openActivityData(Class<?> activityClass, Serializable serializableData) {
        startActivity(new Intent(this, activityClass).putExtra("data", serializableData));
    }

    /**
     * 跳转页面并设置请求码和code
     *
     * @param activityClass
     * @param requestCode
     * @param code
     */
    public void openActivityRequest(Class<?> activityClass, int requestCode, int code) {
        startActivityForResult(new Intent(this, activityClass).putExtra("code", code), requestCode);
    }

    /**
     * 跳转页面并设置请求码
     *
     * @param activityClass 跳转的activity
     * @param requestCode   请求码
     */
    public void openActivityRequest(Class<?> activityClass, int requestCode) {
        startActivityForResult(new Intent(this, activityClass), requestCode);
    }

    /**
     * 跳转到有返回码的页面
     *
     * @param activityClass 有返回码的页面
     * @param resultCode    返回码
     */
    public void setActivityResult(Class<?> activityClass, int resultCode) {
        setResult(resultCode, new Intent(this, activityClass));
        finish();//跳转之后必须要finish，不然数据传不进去
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
     * 设置控件的宽度
     *
     * @param view
     * @param width
     */
    public void setViewWidth(View view, int width) {
        setViewLayoutParams(view, width, 0);
    }

    /**
     * 设置控件的高度
     *
     * @param view
     * @param height
     */
    public void setViewHeigth(View view, int height) {
        setViewLayoutParams(view, 0, height);
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
        if (width > 1) {
            lp.width = width;
        }
        if (height > 1) {
            lp.height = height;
        }
        view.setLayoutParams(lp);
    }

    /**
     * 获取手机屏幕宽度
     * @return
     */
    public int getWidth(){
        return getWindowManager().getDefaultDisplay().getWidth();
    }
    /**
     * 获取手机屏幕高度
     * @return
     */
    public int getHeight(){
        return getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 初始化控件并设置沉浸状态栏
     */
    public void initBind(){
        ButterKnife.bind(this);
        statusBar();
    }

}
