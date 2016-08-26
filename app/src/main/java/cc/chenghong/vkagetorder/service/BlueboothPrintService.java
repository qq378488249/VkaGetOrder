package cc.chenghong.vkagetorder.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;

import org.apache.commons.lang.ArrayUtils;

import java.util.List;
import java.util.Vector;

import cc.chenghong.vkagetorder.activity_pad.BluetoothActivity;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Orders;
import cc.chenghong.vkagetorder.bean.OrdersDetailsList;
import cc.chenghong.vkagetorder.util.StringUtils;

/**
 * 蓝牙打印服务
 * Created by 何成龙 on 2016/8/3.
 */
public class BlueboothPrintService {
    private static final String TAG = "BlueboothPrintService";

    public GpService getmGpService() {
        return mGpService;
    }

    private GpService mGpService;
    private Context mContext;
    //判断是否连接打印机
    private boolean isConnection;
    private PrinterServiceConnection conn = null;

    public BroadcastReceiver getBlueboothPrinterBroadcastReceiver() {
        return BlueboothPrinterBroadcastReceiver;
    }

    public BlueboothPrintService(Context context) {
        mContext = context;
//        this.mGpService = service;
        IntentFilter filter = new IntentFilter();
        filter.addAction(App.BLUEBOOTH_PRINT);//监听打印广播
        filter.addAction(BluetoothActivity.ACTION_CONNECT_STATUS);
        mContext.registerReceiver(BlueboothPrinterBroadcastReceiver, filter);
        if (App.mGpService != null) {
            mGpService = App.mGpService;
        }
//        mGpService = PadMainActivity.blueboothPrintService.getmGpService();
//        startService();
//        connection();
    }

    private void startService() {
        Intent i = new Intent(mContext, GpPrintService.class);
        mContext.startService(i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private void connection() {
        conn = new PrinterServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "onServiceDisconnected() called");
                mGpService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mGpService = GpService.Stub.asInterface(service);
            }
        };
        Log.i(TAG, "connection");
        Intent intent = null;
        Log.i(TAG, "connection: " + getSDKVersionNumber());
        if (getSDKVersionNumber() > 20) {//5.0以上需要显示的
            intent = new Intent(createExplicitFromImplicitIntent(mContext, new Intent("com.gprinter.aidl.GpPrintService")));
        } else {
            intent = new Intent("com.gprinter.aidl.GpPrintService");
        }
        mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
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

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }

    /**
     * 重新连接打印机
     */
    public void reconnectPrint() {
        int index = App.index;
        if (App.index < 4) {
            toask("蓝牙打印机已断开，正在进行第" + index + "次重连");
        } else {
            toask("重连失败，请检查蓝牙打印机是否正确打开");
        }
        PortParameters mPortParam[] = new PortParameters[GpPrintService.MAX_PRINTER_CNT];
        if (mGpService == null) {
            toask("蓝牙打印机服务未启动");
            return;
        }
        int PrinterId = 0;
        mPortParam[PrinterId].setBluetoothAddr(App.blueboothAddress);
        mPortParam[PrinterId].setPortType(PortParameters.BLUETOOTH);

        int rel = 0;
        if (mPortParam[PrinterId].getPortOpenState() == false) {
            switch (mPortParam[PrinterId].getPortType()) {
                case PortParameters.USB:
                    try {
                        rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getUsbDeviceName(), 0);
                    } catch (RemoteException e) {
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
                        System.out.println("sdfasdf");

                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            System.out.println(r);
            if (r != GpCom.ERROR_CODE.SUCCESS) {//连接成功
                if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                    toask("连接成功");
                    mPortParam[PrinterId].setPortOpenState(true);
                    App.isConnection = true;
                } else {
                    toask("连接失败");
                }
            }
        } else {
            Log.d(TAG, "DisconnectToDevice ");
            try {
                mGpService.closePort(PrinterId);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private BroadcastReceiver BlueboothPrinterBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (App.BLUEBOOTH_PRINT.equals(intent.getAction())) {
                if (App.getBluetooth()) {
                    Orders orders = (Orders) intent.getSerializableExtra("data");
                    printReceipts(orders);
                }
            }
            if (BluetoothActivity.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d(TAG, "connect status " + type);
                if (type == GpDevice.STATE_CONNECTING) {//连接中
                    App.isConnection = false;
                } else if (type == GpDevice.STATE_NONE) {//未连接
                    App.isConnection = false;
                } else if (type == GpDevice.STATE_VALID_PRINTER) {//有效的打印机
                    App.isConnection = true;
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {//无效的打印机
                    App.isConnection = false;
                    toask("请使用佳博打印机");
                }
            }
        }
    };

    void toask(String s) {
        App.toask(s);
    }

    private void printReceipts(Orders orders) {
        if (orders == null) {
            return;
        }
        String s = getPrinterStatusClicked();
        if (s != null && !s.equals("") && s.equals("打印机正常")) {
            try {
//                        int rel = mGpService.printeTestPage(mPrinterIndex); //
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
                        int i1 = 13 - ordersDetailsList.getProductName().length() * 2;
                        for (int i = 0; i < i1; i++) {
                            sb.append(" ");
                        }
                        sb.append("x1         ");//数量
                        sb.append(ordersDetailsList.getPrice() + n);//价格
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
                    if (r != GpCom.ERROR_CODE.SUCCESS) {
                        App.toask(GpCom.getErrorText(r));
                    }
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.i("ServiceConnection", "rel " + rel);
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
//                App.toask(GpCom.getErrorText(r));
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    App.toask(GpCom.getErrorText(r));
                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else {
            App.toask(s);
        }
    }

    public String getPrinterStatusClicked() {
        String str = "";
        int status = 0;
        System.out.println(mGpService);
        try {
            if (mGpService != null) {
//                status = mGpService.queryPrinterStatus(0, 500);
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
}
