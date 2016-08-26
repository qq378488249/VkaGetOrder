package cc.chenghong.vkagetorder.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity_pad.BluetoothActivity;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Account;
import cc.chenghong.vkagetorder.bean.Orders;
import cc.chenghong.vkagetorder.bean.OrdersDetailsList;
import cc.chenghong.vkagetorder.fragment.BaseFragment;
import cc.chenghong.vkagetorder.util.SharedPreferencesHelper;
import cc.chenghong.vkagetorder.util.StringUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class BlueActivity extends ProgressActivity {
    public String TAG = getClass().getSimpleName();
    public BaseFragment currentFragment;
    public GpService mGpService = null;//蓝牙打印机服务
    public PrinterServiceConnection conn = null;
    /**
     * 判断应用是否在前台
     */
    public static boolean isForeground = false;
    /**
     * 是否已经发送蓝牙消息
     */
    public boolean isSendBlueboothMsg = false;
    public BluetoothAdapter blueadapter;
    /**
     * 发送选择蓝牙列表框的时间
     */
    public int sendMsg = 60 * 1000;

    public void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 119:
                    reconnectPrint();
                    break;
                case 329:
                    if (App.getBluetooth()) {//设置了蓝牙打印
                        isSendBlueboothMsg = true;
                        if (blueadapter != null) {
                            if (!blueadapter.isEnabled()) {//没有打开蓝牙
                                openBluebooth();
                            } else {
                                if (!App.isConnection && !App.autoConnection) {//未连接蓝牙打印机
                                    Intent intent = newIntent(BluetoothActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                                    startActivity(newIntent(BluetoothActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                }
                            }
                            handler.sendEmptyMessageDelayed(329, sendMsg);//60秒发送一个消息
                        } else {
                            toask("您的设备蓝牙版本过低，已自动关闭蓝牙打印功能");
                            App.setBluetooth(false);
                        }
                    } else {
                        isSendBlueboothMsg = false;
                    }
                    break;
            }
        }
    };

    /**
     * 重新连接打印机
     */
    public void reconnectPrint() {
        if (App.isConnection) {//已连接的话就跳过了，不自动连接
            return;
        }
        int index = App.index;
        if (App.index < 4) {
            toask("蓝牙打印机已断开，正在进行第" + index + "次自动重连");
        } else {//3次重试失败，停止重试
            App.autoConnection = false;
            App.isConnection = false;
            App.index = 1;//重新初始化，但是不连接蓝牙打印机
            toask("自动重连失败，请检查蓝牙打印机是否正确打开");
            handler.sendEmptyMessageDelayed(329, sendMsg);//60秒后再次弹出连接蓝牙打印机弹窗
            return;
        }
        if (mGpService == null) {
            toask("蓝牙打印机服务未启动");
            return;
        }
        int PrinterId = 0;
        PortParameters mPortParam[] = new PortParameters[1];
        PortParamDataBase database = new PortParamDataBase(this);

        mPortParam[PrinterId] = new PortParameters();
        mPortParam[PrinterId] = database.queryPortParamDataBase("" + PrinterId);
        mPortParam[PrinterId].setPortOpenState(false);//全部初始化为开放状态

        mPortParam[PrinterId].setBluetoothAddr(App.blueboothAddress);
        mPortParam[PrinterId].setPortType(PortParameters.BLUETOOTH);

        int rel = 0;
        switch (mPortParam[PrinterId].getPortType()) {
            case PortParameters.USB:
                try {
                    rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getUsbDeviceName(), 0);
                } catch (RemoteException e) {
                    //
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case PortParameters.ETHERNET:
                try {
                    rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getIpAddr(), mPortParam[PrinterId].getPortNumber());
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case PortParameters.BLUETOOTH:
                try {
                    int portType = mPortParam[PrinterId].getPortType();
                    String addr = mPortParam[PrinterId].getBluetoothAddr();
                    rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getBluetoothAddr(), 0);
//                            rel = PadMainActivity.blueboothPrintService.getmGpService().openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getBluetoothAddr(), 0);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
        }
        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
        logE(r);
//            if (r != GpCom.ERROR_CODE.SUCCESS) {//连接成功
        if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
            toask("连接成功");
            App.autoConnection = false;
            mPortParam[PrinterId].setPortOpenState(true);
            App.isConnection = true;
        } else {
//                    toask("连接失败");
            App.autoConnection = true;
            handler.sendEmptyMessageDelayed(119, 5000);
            mPortParam[PrinterId].setPortOpenState(false);
            App.isConnection = false;
            App.index++;
        }
//            }
    }

    int i = 0;

    protected void print() {
        if (mGpService == null) {
            toask("打印机服务为空");
            return;
        }
        i++;
        int rel = 0;
        try {
            EscCommand esc = new EscCommand();
            String n = "\n";
        /*打印文字*/
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
            esc.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);//设置打印左对齐
            esc.addText(new StringBuffer().append("测试" + i).toString());//打印文字
            esc.addPrintAndLineFeed();//打印和换行

            Vector<Byte> datas = esc.getCommand(); //发送数据
            Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
            byte[] bytes = ArrayUtils.toPrimitive(Bytes);
            String str = Base64.encodeToString(bytes, Base64.DEFAULT);
            try {
                rel = mGpService.sendEscCommand(0, str);
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                logI("rel：" + "    " + rel);
                logI("r：" + "    " + r);
                if (rel == 0) {
                    App.toask("打印成功");
                } else {
                    App.toask("打印失败" + GpCom.getErrorText(r));
                    App.isConnection = false;
                    App.autoConnection = true;
                    handler.sendEmptyMessage(119);
                }
//                if (r != GpCom.ERROR_CODE.SUCCESS) {
//                    App.toask("打印失败" + GpCom.getErrorText(r));
//                    App.isConnection = false;
//                    App.autoConnection = true;
//                    handler.sendEmptyMessage(119);
//                } else {
//                    App.toask("打印成功");
//                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
////                App.toask(GpCom.getErrorText(r));
//            if (r != GpCom.ERROR_CODE.SUCCESS) {
//                App.toask(GpCom.getErrorText(r));
//                App.isConnection = false;
//            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
//            App.isConnection = false;
            e1.printStackTrace();
//            getPrinterStatusClickedNew();
        }
    }

    public void printReceipts(Orders orders) {
        if (orders == null) {
            toask("订单信息为空");
            return;
        }
        if (mGpService == null) {
            toask("打印机服务未启动");
            return;
        }
        try {
            int rel = 0;
            EscCommand esc = new EscCommand();
            String n = "\n";
        /*打印文字*/
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
            StringBuffer sb = new StringBuffer();
            sb.append("--------------------------------\n");
            sb.append("商家：" + orders.getStoreName() + n);
            sb.append("订单编号：" + orders.getId() + n);
            sb.append("打印时间：" + orders.getCreated() + n);
            sb.append("--------------------------------\n");
//                sb.append("商品\t数量\t金额\n");
//                if (orders.getOrdersDetailsList() != null) {
//                    for (OrdersDetailsList ordersDetailsList : orders.getOrdersDetailsList()) {
//                        sb.append(ordersDetailsList.getProductName()+"\tx1"+"\t"+ordersDetailsList.getPrice() + n);//商品名
////                        int i1 = 13 - ordersDetailsList.getProductName().length() * 2;
////                        for (int i = 0; i < i1; i++) {
////                            sb.append(" ");
////                        }
////                        sb.append("         ");//数量
////                        sb.append(ordersDetailsList.getPrice() + n);//价格
//                    }
//                }
            sb.append("商品        数量        金额\n");
            if (orders.getOrdersDetailsList() != null) {
                for (OrdersDetailsList ordersDetailsList : orders.getOrdersDetailsList()) {
                    sb.append(ordersDetailsList.getProductName());//商品名
                    int i1 = 13 - StringUtils.getByteCount(ordersDetailsList.getProductName());
                    logI(i1);
                    for (int i = 0; i < i1; i++) {
                        sb.append(" ");
                    }
                    sb.append("x1          ");//数量
                    sb.append(ordersDetailsList.getPrice() + n);//价格
                    if (StringUtils.noEmpty(ordersDetailsList.getAttributeNames())) {
                        sb.append("口味：" + ordersDetailsList.getAttributeNames() + n);
                    }
                }
            }
            sb.append("--------------------------------\n");
            sb.append("总计：                  " + orders.getAmount() + n);
            sb.append("--------------------------------\n");
            if (orders.getCard() != null) {
                if (StringUtils.noEmpty(orders.getCard().getMobile())) {
                    sb.append("卡号：" + orders.getCard().getMobile() + n);
                }
                if (StringUtils.noEmpty(orders.getCard().getName())) {
                    sb.append("姓名：" + orders.getCard().getName() + n);
                }
                sb.append("卡内余额：" + orders.getCard().getBalance() + n);
                sb.append("卡内积分：" + orders.getCard().getPoint() + n);
                if (StringUtils.noEmpty(orders.getCard().getTicketsCount())) {
                    sb.append("卡内优惠券：" + orders.getCard().getTicketsCount() + "张" + n);
                } else {
                    sb.append("卡内优惠券：0张" + n);
                }
            } else {
                sb.append("姓名：" + orders.getCardName() + n);
            }
            for (int i = 0; i < 2; i++) {
                sb.append(n);
            }
//                sb.append(""+orders.get);
            esc.addText(sb.toString());//打印文字
            esc.addPrintAndLineFeed();//打印和换行

            Vector<Byte> datas = esc.getCommand(); //发送数据
            Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
            byte[] bytes = ArrayUtils.toPrimitive(Bytes);
            String str = Base64.encodeToString(bytes, Base64.DEFAULT);
            try {
                rel = mGpService.sendEscCommand(0, str);
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                logI("rel :" + rel);
                logI("r :" + r);
                if (r != GpCom.ERROR_CODE.SUCCESS) {//打印失败，自动重连
                    App.toask("打印失败" + GpCom.getErrorText(r));
                    App.autoConnection = true;
                    App.index = 1;
                    handler.sendEmptyMessage(119);
                } else {
                    App.toask("打印成功");
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
////                App.toask(GpCom.getErrorText(r));
//            if (r != GpCom.ERROR_CODE.SUCCESS) {
//                App.toask(GpCom.getErrorText(r));
//                App.isConnection = false;
//            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
//            getPrinterStatusClickedNew();
        }
//        } else {
//            App.toask(s);
//        }
    }

    public void connection1() {
        conn = new PrinterServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                logI("onServiceDisconnected() called");
                mGpService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mGpService = GpService.Stub.asInterface(service);
            }
        };
        logI("connection");
        Intent intent = null;
        logI("connection: " + getSDKVersionNumber());
        if (getSDKVersionNumber() > 20) {//5.0以上需要显示的
            intent = new Intent(createExplicitFromImplicitIntent(this, new Intent("com.gprinter.aidl.GpPrintService")));
        } else {
            intent = new Intent("com.gprinter.aidl.GpPrintService");
        }
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    /***
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     * <p/>
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     * <p/>
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     *
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    public class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            logI("onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }

    protected View vBar;
    protected LinearLayout llContent;
    protected LinearLayout llBack;
    protected TextView tvTitle;
    protected View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        initBaseView();
        logI("onCreate");
    }

    private void initBaseView() {
        llBack = (LinearLayout) findViewById(R.id.llBack);
        tvTitle = findView(R.id.tvTitle);
        llContent = findView(R.id.llContent);
        vBar = findView(R.id.vBar);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void setTitleName(String s) {
        if (tvTitle == null) {
            throw new NullPointerException("tvTitle not null");
        }
        tvTitle.setText(s);
    }

    /**
     * 加入页面内容布局
     */
    protected void contentView(int layoutId) {
        statusBar(vBar);
        contentView = getLayoutInflater().inflate(layoutId, null);
        if (llContent.getChildCount() > 0) {
            llContent.removeAllViews();
        }
        if (contentView != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            llContent.addView(contentView, params);
        }
    }
    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
//    @SuppressWarnings("unchecked")
//    public <T extends View> T getView(int viewId) {
//        View view = findViewById(viewId);
////        if (view == null) {
////            view = mConvertView.findViewById(viewId);
////            mViews.put(viewId, view);
////        }
//        return (T) view;
//    }

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
        if (tv.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 获取控件的值
     *
     * @param view
     * @return 控件的值
     */
    public String viewGetValue(View view) {
        String result = "";
        if (view != null) {
            TextView tv = (TextView) view;
            result = tv.getText().toString();
        }
        return result;
    }

    public void statusBar(View view) {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setViewHeigth(view, getStatusBarHeight());
            view.setVisibility(View.VISIBLE);
            // Translucent status bar
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            view.setVisibility(View.GONE);
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
     * 打开软键盘+
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
     * 字符串转化为实体类
     *
     * @param strJson
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(String strJson, Class<T> clazz) {
        return new Gson().fromJson(strJson, clazz);
    }

    /**
     * 对象转化为实体类
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(Object obj, Class<T> clazz) {
        if (StringUtils.noEmpty(obj)) {
            return new Gson().fromJson(obj.toString(), clazz);
        }
        return null;
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

    /**
     * 初始化控件并设置沉浸状态栏
     */
    public void initBind() {
        ButterKnife.bind(this);
//        statusBar();
    }

    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(App.getInstance());
    }

    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(App.getInstance());
    }

    /**
     * 打印d级日志
     *
     * @param msg
     */
    public void logD(String msg) {
        if (StringUtils.noEmpty(msg)) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 获取string.xml里对应id的值
     *
     * @param id
     * @return
     */
    public String getResourcesString(int id) {
        return getResources().getString(id);
    }

    /**
     * 处理volley错误
     *
     * @param volleyError
     */
    public void volleyError(VolleyError volleyError) {
        logI(volleyError.toString());
        toask("网络连接失败");
        hideProgress();
    }

    /**
     * 设置是否登录
     *
     * @param b
     */
    public void isLogin(boolean b) {
        SharedPreferencesHelper.saveBoolean("isLogin", b);
    }

    /**
     * 保存登陆的账户
     *
     * @param account
     */
    public static void putAccount(Account account) {
        SharedPreferencesHelper.saveJSON("account", account);
    }

    /**
     * 获取登陆的账户
     *
     * @return
     */
    public static Account getAccount() {
        return SharedPreferencesHelper.getJSON("account", Account.class);
    }

    /**
     * 设置极光推送标签
     *
     * @param strs
     */
    public void setJPushInterfaceTag(String... strs) {
        Set<String> set = new HashSet<>();
        for (String str : strs) {
            set.add(str);
        }
        JPushInterface.setTags(getApplication(), set, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                toask(i + s + set.toString());
                System.out.println(i + s);
            }
        });
    }

    /**
     * 设置登陆状态
     *
     * @param b
     */
    public void setLogin(boolean b) {
        SharedPreferencesHelper.saveBoolean("isLogin", b);
    }

    public Context getActivity() {
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
     * 显示一个Fragment
     *
     * @param clazz
     * @return 是否已经是当前fragment
     */
    public boolean showFragment(Class<?> clazz) {
        try {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            if (currentFragment != null
                    && !(currentFragment.getClass() == clazz)) {
                t.hide(currentFragment);
            }
            BaseFragment f = (BaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(clazz.getName());
            if (f == null)
                f = (BaseFragment) clazz.newInstance();
            if (!f.isAdded()) {
                t.add(R.id.fl, f, f.getClass().getName());
                t.show(f);
            } else {
                t.show(f);
            }
            boolean b = currentFragment == f;
            currentFragment = f;
            t.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                    R.anim.fade_in, R.anim.fade_out);
            t.commit();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打印e级日志
     */
    protected void logE(Object o) {
        if (o != null) {
            Log.e(TAG, o.toString());
        }
    }

    /**
     * 打印i级日志
     */
    protected void logI(Object o) {
        if (o != null) {
            Log.i(TAG, o.toString());
        }
    }

    public void openBluebooth() {
        // 请求打开 Bluetooth
        Intent requestBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
        requestBluetoothOn.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        // 设置 Bluetooth 设备可见时间
        requestBluetoothOn.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        // 请求开启 Bluetooth
        startActivityForResult(requestBluetoothOn, 329);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 329 && resultCode == 120) {
            App.isBluebooth = true;
            startActivity(BluetoothActivity.class);//弹出连接打印机页面
        }
    }
}
