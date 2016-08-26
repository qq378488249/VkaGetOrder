package cc.chenghong.vkagetorder.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity_pad.BluetoothActivity;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Orders;
import cc.chenghong.vkagetorder.fragment.BaseFragment;
import cc.chenghong.vkagetorder.fragment.DdFragment;
import cc.chenghong.vkagetorder.fragment.MdFragment;
import cc.chenghong.vkagetorder.fragment.XtFragment;
import cc.chenghong.vkagetorder.fragment.XtFragment1;
import cc.chenghong.vkagetorder.receiver.PrinterStatusBroadcastReceiver;
import cc.chenghong.vkagetorder.util.StringUtils;
import cn.jpush.android.api.JPushInterface;

/**
 * 手机主页
 */
public class MainActivity extends BlueActivity {

    @Bind(R.id.fl)
    FrameLayout fl;
    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.ll2)
    LinearLayout ll2;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    FragmentManager fragmentManager;
    BaseFragment currentFragment;
//    @Bind(R.id.llBar)
//    LinearLayout llBar;

    DdFragment ddFragment = new DdFragment();
    MdFragment mdFragment = new MdFragment();
    XtFragment xtFragment = new XtFragment();
    @Bind(R.id.iv1)
    ImageView iv1;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.iv2)
    ImageView iv2;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.iv3)
    ImageView iv3;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.vBar)
    View vBar;
    //蓝牙适配器
//    BluetoothAdapter blueadapter;
    /**
     * 蓝牙状态
     */
    boolean isBluebooth = false;
    //蓝牙打印
//    private GpService mGpService = null;
//    public static BlueboothPrintService blueboothPrintService = null;
//    private int sendMsg = 30 * 1000;
//    /**
//     * 是否已经发送蓝牙消息
//     */
//    private boolean isSendBlueboothMsg = false;
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        connection();
//        initView();
        fragmentManager = getSupportFragmentManager();
//        statusBar(llBar);
        setSelectView(ll1);
        showFragment(DdFragment.class);
        registerMessageReceiver();  // used for receive msg
//        blueadapter = BluetoothAdapter.getDefaultAdapter();
//        blueboothPrintService = new BlueboothPrintService(getActivity());
        handler.sendEmptyMessage(329);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrint = true;
                handler.sendEmptyMessage(111);
            }
        });
        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrint = false;
            }
        });
        statusBar(vBar);
//        if (App.getBluetooth()) {
//            if (blueadapter == null) {
//                toask("您的设备蓝牙版本过低，已自动关闭蓝牙打印功能");
//                App.setBluetooth(false);//自动关闭蓝牙打印功能
//            } else {
////                startService();
//                blueboothPrintService = new BlueboothPrintService(getActivity());
//                if (!blueadapter.isEnabled()) {//蓝牙未打开
//                    openBluebooth();
//                } else {
//                    isBluebooth = true;
//                    App.isBluebooth = true;
//                }
//                handler.sendEmptyMessageDelayed(329, sendMsg);
//            }
//        }
//        updateFragment(ddFragment, false);
    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 111:
//                    if (isPrint) {
//                        print();
//                        handler.sendEmptyMessage(111);
//                    }
//                    break;
//                case 329:
//                    if (App.getBluetooth()) {//设置了蓝牙打印
//                        isSendBlueboothMsg = true;
//                        if (blueadapter != null) {
//                            if (!blueadapter.isEnabled()) {//没有打开蓝牙
//                                openBluebooth();
//                            } else {
//                                if (!App.isConnection && !App.autoConnection) {//未连接蓝牙打印机
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

    @OnClick({R.id.fl, R.id.ll1, R.id.ll2, R.id.ll3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl:
                break;
            case R.id.ll1:
                if (ll1.isSelected()) {
                    return;
                }
                setSelectView(ll1);
                showFragment(DdFragment.class);
//                updateFragment(ddFragment,true);
                break;
            case R.id.ll2:
                if (ll2.isSelected()) {
                    return;
                }
                setSelectView(ll2);
                showFragment(MdFragment.class);
//                updateFragment(mdFragment, true);
                break;
            case R.id.ll3:
                if (ll3.isSelected()) {
                    return;
                }
                setSelectView(ll3);
                showFragment(XtFragment1.class);
//                updateFragment(xtFragment, true);
                break;
        }
    }

    public void setSelectView(View view) {
        ll1.setSelected(false);
        ll2.setSelected(false);
        ll3.setSelected(false);
        view.setSelected(true);
    }

    private void updateFragment(Fragment f, boolean b) {
        // TODO Auto-generated method stub
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl, f);
        if (!b) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    /**
     * 判断应用是否在前台
     */
    public static boolean isForeground = false;
    /**
     * 判断应用是否被销毁
     */
    public static boolean isDestroy = false;
    //通知栏管理器
    private NotificationManager notificationManager;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String MESSAGE_REFRESH = "refresh";
    /**
     * 打开蓝牙广播
     */
    public static final String MESSAGE_OPEN_BLUEBOOTH = "open_bluebooth";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    private PrinterStatusBroadcastReceiver printerStatusBroadcastReceiver = new PrinterStatusBroadcastReceiver();

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction(MESSAGE_OPEN_BLUEBOOTH);
        filter.addAction(App.BLUEBOOTH_PRINT);
        filter.addAction(PrinterStatusBroadcastReceiver.ACTION_CONNECT_STATUS);
        registerReceiver(mMessageReceiver, filter);

        registerReceiver(printerStatusBroadcastReceiver, filter);
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
                showFragment(DdFragment.class);
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
    public void getPrinterStatusClickedNew() {
        if (mGpService == null) {
            toask("打印机服务未启动");
            return;
        }
        try {
            mGpService.queryPrinterStatus(0, 500, MAIN_QUERY_PRINTER_STATUS);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public String getPrinterStatusClicked() {
        String str = "";
        int status = 0;
        System.out.println(mGpService);
        try {
            if (mGpService != null) {
//                status = mGpService.queryPrinterStatus(0, 500,MAIN_QUERY_PRINTER_STATUS);
            } else {
                return str;
            }
            if (status == GpCom.STATE_NO_ERR) {
                str = "打印机正常";
            } else if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                str = "打印机脱机";//脱机时需要重新连接打印机
                App.isConnection = false;
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

    boolean isPrint = true;

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        if (!isSendBlueboothMsg && App.isBluebooth) {
            handler.sendEmptyMessageDelayed(329, sendMsg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (StringUtils.noEmpty(mMessageReceiver)) {
            unregisterReceiver(mMessageReceiver);
        }
        isDestroy = true;
        isForeground = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
