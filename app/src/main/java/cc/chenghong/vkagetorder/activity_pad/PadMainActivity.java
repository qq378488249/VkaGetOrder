package cc.chenghong.vkagetorder.activity_pad;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gprinter.aidl.GpService;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity.BlueActivity;
import cc.chenghong.vkagetorder.activity.MainActivity;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Orders;
import cc.chenghong.vkagetorder.fragment.BaseFragment;
import cc.chenghong.vkagetorder.fragment_pad.PadDdFragment1;
import cc.chenghong.vkagetorder.fragment_pad.PadMdFragment;
import cc.chenghong.vkagetorder.fragment_pad.PadXtFragment;
import cc.chenghong.vkagetorder.service.BlueboothPrintService;
import cn.jpush.android.api.JPushInterface;

/**
 * 平板主页
 */
public class PadMainActivity extends BlueActivity {
    FragmentManager fragmentManager;
    BaseFragment currentFragment;

    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.ll2)
    LinearLayout ll2;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    @Bind(R.id.fl)
    FrameLayout fl;
    @Bind(R.id.btPrint)
    Button btPrint;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.vBar)
    View vBar;

    /**
     * 蓝牙打开成功返回码
     */
    public final static int OK = 120;
    //蓝牙适配器
    BluetoothAdapter blueadapter;
    /**
     * 蓝牙状态
     */
    boolean isBluebooth = false;
    //蓝牙打印
    private GpService mGpService = null;
    public static BlueboothPrintService blueboothPrintService = null;
    private int sendMsg = 10 * 1000;
    //是否已经发送蓝牙消息
    private boolean isSendBlueboothMsg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad_main);
        init();
    }


    private void init() {
        ButterKnife.bind(this);
        statusBar(vBar);
        connection();
        App.isConnection = false;
        isForeground = true;
        setLl(ll1);
        showFragment(PadDdFragment1.class);
        registerMessageReceiver();
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        handler.sendEmptyMessage(329);
        bt.setVisibility(View.GONE);
        btPrint.setVisibility(View.GONE);
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 329 && resultCode == 120) {
//            isBluebooth = true;
//            App.isBluebooth = true;
//        }
//    }

    @OnClick({R.id.ll1, R.id.ll2, R.id.ll3, R.id.fl, R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                setLl(ll1);
//                showFragment(PadDdFragment.class);
                showFragment(PadDdFragment1.class);
                break;
            case R.id.ll2:
                setLl(ll2);
                showFragment(PadMdFragment.class);
                break;
            case R.id.ll3:
                setLl(ll3);
                showFragment(PadXtFragment.class);
                break;
            case R.id.fl:
                break;
            case R.id.bt:
                startActivity(BluetoothActivity.class);
                break;
        }
    }

    void setLl(View view) {
        ll1.setSelected(false);
        ll2.setSelected(false);
        ll3.setSelected(false);
        view.setSelected(true);
    }

    //极光推送
    /**
     * 判断应用是否在前台
     */
//    public static boolean isForeground = false;
    /**
     * 判断应用是否被销毁
     */
    public static boolean isDestroy = false;
    //通知栏管理器
    private NotificationManager notificationManager;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
        filter.addAction(MainActivity.MESSAGE_OPEN_BLUEBOOTH);
        filter.addAction(App.BLUEBOOTH_PRINT);
        filter.addAction(BluetoothActivity.ACTION_CONNECT_STATUS);

        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {//接受到推送订单或者用户点击了通知栏都显示订单页面
            if (BluetoothActivity.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d(TAG, "connect status " + type);
                if (type == GpDevice.STATE_CONNECTING) {//连接中
                    logE("GpDevice.STATE_CONNECTING连接中");
                    App.isConnection = false;
                } else if (type == GpDevice.STATE_NONE) {//未连接
                    logE("GpDevice.STATE_NONE未连接");
                    App.isConnection = false;
                } else if (type == GpDevice.STATE_VALID_PRINTER) {//连接成功
                    logE("GpDevice.STATE_VALID_PRINTER连接成功");
                    App.isConnection = true;
                    App.autoConnection = false;
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {//打印机无效
                    logE("GpDevice.STATE_INVALID_PRINTER连接无效");
                    App.isConnection = false;
                }
            }
            if (App.BLUEBOOTH_PRINT.equals(intent.getAction())) {
                if (App.getBluetooth()) {
                    printReceipts((Orders) intent.getSerializableExtra("data"));
                }
            }
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction()) || JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                showFragment(PadDdFragment1.class);
//                Push push = (Push) intent.getSerializableExtra("push");
//                if (StringUtils.noEmpty(push) && StringUtils.noEmpty(push.getALERT())) {
//                    showFragment(PadDdFragment1.class);
//                }
            }
            if (MainActivity.MESSAGE_OPEN_BLUEBOOTH.equals(intent.getAction())) {
                isSendBlueboothMsg = false;
                App.isConnection = false;
                handler.sendEmptyMessage(329);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logI("onResume");
        isForeground = true;
        if (!isSendBlueboothMsg && App.isBluebooth) {
            handler.sendEmptyMessageDelayed(329, sendMsg);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        logI("onPause");
        isForeground = false;
    }

    @Override
    protected void onDestroy() {
        logI("onDestroy");
        super.onDestroy();
        if (mMessageReceiver != null) {
            unregisterReceiver(mMessageReceiver);
        }
        isDestroy = true;
        isForeground = false;
        if (blueboothPrintService != null && blueboothPrintService.getBlueboothPrinterBroadcastReceiver() != null) {
            unregisterReceiver(blueboothPrintService.getBlueboothPrinterBroadcastReceiver());
        }
    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 329:
//                    if (App.getBluetooth()) {//设置了蓝牙打印
//                        isSendBlueboothMsg = true;
//                        if (blueadapter != null) {
//                            if (!blueadapter.isEnabled()) {//没有打开蓝牙
//                                openBluebooth();
//                            } else {
//                                if (!App.isConnection && !App.autoConnection) {//未连接蓝牙打印机，并且没有自动连接
//                                    Intent intent = newIntent(BluetoothActivity.class);
////                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//                                    startActivity(new Intent(getActivity(), BluetoothActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
//                                }
//                            }
//                            handler.sendEmptyMessageDelayed(329, sendMsg);//60秒发送一个消息
//                        } else {
//                            toask("您的设备蓝牙版本过低，已自动关闭蓝牙打印功能");
//                            App.setBluetooth(false);
//                        }
//                    } else {
//                        isSendBlueboothMsg = false;
//                    }
//                    break;
//            }
//        }
//    };

//    void printReceipts(Orders orders) {
//        String s = getPrinterStatusClicked();
//        if (s != null && !s.equals("") && s.equals("打印机正常")) {
//            try {
////                        int rel = mGpService.printeTestPage(mPrinterIndex); //
//                int rel = 0;
//                EscCommand esc = new EscCommand();
//
//        /*打印文字*/
//                esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
//                esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
//                esc.addText("上海澄泓信息科技有限公司\n");   //  打印文字
//                esc.addPrintAndLineFeed();//打印和换行
//
//                Vector<Byte> datas = esc.getCommand(); //发送数据
//                Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
//                byte[] bytes = ArrayUtils.toPrimitive(Bytes);
//                String str = Base64.encodeToString(bytes, Base64.DEFAULT);
//                try {
//                    rel = mGpService.sendEscCommand(0, str);
//                    GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
//                    if (r != GpCom.ERROR_CODE.SUCCESS) {
//                        Toast.makeText(getApplicationContext(), GpCom.getErrorText(r),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                } catch (RemoteException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
////                Log.i("ServiceConnection", "rel " + rel);
//                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
//                toask(GpCom.getErrorText(r));
////                if (r != GpCom.ERROR_CODE.SUCCESS) {
////                    Toast.makeText(getApplicationContext(), GpCom.getErrorText(r),
////                            Toast.LENGTH_SHORT).show();
////                } else {
////                    toask("打印成功");
////                }
//            } catch (Exception e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//        } else {
//            toask(s);
//        }
//    }

    public String getPrinterStatusClicked() {
        String str = "打印机服务为空";
        int status = 0;
        try {
            if (mGpService != null) {
//                status = mGpService.queryPrinterStatus(0, 500);
            } else {
                return str;
            }
            if (status == GpCom.STATE_NO_ERR) {
                str = "打印机正常";
            } else if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                str = "打印机脱机";
            } else if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                str = "打印机缺纸";
            } else if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                str = "打印机开盖";
            } else if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                str = "打印机出错";
            }
//            Toast.makeText(getApplicationContext(),
//                    "打印机：" + '0' + " 状态：" + str, Toast.LENGTH_SHORT).show();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return str;
    }

    private void startService() {
        Intent i = new Intent(this, GpPrintService.class);
        startService(i);
//        blueboothPrintService = new BlueboothPrintService(this, mGpService);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
