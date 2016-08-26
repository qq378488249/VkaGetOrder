package cc.chenghong.vkagetorder.activity_pad;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity.BlueActivity;
import cc.chenghong.vkagetorder.adapter.LvAdapter;
import cc.chenghong.vkagetorder.adapter.LvViewHolder;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Bluebooth;
import cc.chenghong.vkagetorder.dialog.AskDialog;

/**
 * 蓝牙设备列表
 */
public class BluetoothActivity extends BlueActivity {
    // Debugging
    private static final String DEBUG_TAG = "BluetoothActivity";
    public static final String ACTION_CONNECT_STATUS = "action.connect.status";
    private static final String TAG = "BluetoothDevice";
    public static LinearLayout deviceNamelinearLayout;
    // Member fields
    private ListView lvPairedDevice = null, lvNewDevice = null;
    private TextView tvPairedDevice = null, tvNewDevice = null;
    private Button btDeviceScan = null;
    private BluetoothAdapter mBluetoothAdapter;
    //已配对设备列表适配器
    LvAdapter<Bluebooth> mPairedDevicesArrayAdapter;
    List<Bluebooth> mPairedDevicesArray = new ArrayList<Bluebooth>();

    //搜索到的新设备列表适配器
    LvAdapter<Bluebooth> mNewDevicesArrayAdapter;
    List<Bluebooth> mNewDevicesArray = new ArrayList<Bluebooth>();
    //    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
//    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private PortParameters mPortParam[] = new PortParameters[GpPrintService.MAX_PRINTER_CNT];
    private int mPrinterId = 0;
    //0已配对列表，1新列表
    int clickType = 0;
    //点击的位置
    int clickPosition = 0;

    public class PrinterSeial {
        static final int GPIRNTER001 = 0;
        static final int GPIRNTER002 = 1;
        static final int GPIRNTER003 = 2;
        static final int GPIRNTER004 = 3;
        static final int GPIRNTER005 = 4;
        static final int GPIRNTER006 = 5;
    }

    Button btPrint;

    private int mPrinterIndex = 0;

//    public void connection() {
//        conn = new PrinterServiceConnection();
//        Log.i(DEBUG_TAG, "connection");
//        Intent intent = null;
//        Log.i(TAG, "connection: " + getSDKVersionNumber());
//        if (getSDKVersionNumber() > 20) {//5.0以上需要显示的
//            intent = new Intent(createExplicitFromImplicitIntent(this, new Intent("com.gprinter.aidl.GpPrintService")));
//        } else {
//            intent = new Intent("com.gprinter.aidl.GpPrintService");
//        }
//        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
//    }

    private void initPortParam() {
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            PortParamDataBase database = new PortParamDataBase(this);
            mPortParam[i] = new PortParameters();
            mPortParam[i] = database.queryPortParamDataBase("" + i);
            mPortParam[i].setPortOpenState(false);//全部初始化为开放状态
        }
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECT_STATUS);
        this.registerReceiver(PrinterStatusBroadcastReceiver, filter);
    }

    private BroadcastReceiver PrinterStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d(DEBUG_TAG, "connect status " + type);
                if (type == GpDevice.STATE_CONNECTING) {//连接中
                    logE("GpDevice.STATE_CONNECTING连接中");
                    progress("连接中...");
                    App.isConnection = false;
                    mPortParam[id].setPortOpenState(false);
                    if (clickType == 0) {
                        mPairedDevicesArray.get(clickPosition).isConnection = false;
                    } else {
                        mNewDevicesArray.get(clickPosition).isConnection = false;
                    }
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
//                    setProgressBarIndeterminateVisibility(true);
//                    SetLinkButtonEnable(ListViewAdapter.DISABLE);
//                    Map<String, Object> map;
//                    map = mList.get(id);
//                    map.put(ListViewAdapter.STATUS,
//                            getString(R.string.connecting));
//                    mList.set(id, map);
//                    mListViewAdapter.notifyDataSetChanged();

                } else if (type == GpDevice.STATE_NONE) {//未连接
                    logE("GpDevice.STATE_NONE未连接");
                    hideProgress();
//                    progress("连接中...");
                    App.isConnection = false;
                    mPortParam[id].setPortOpenState(false);
                    if (clickType == 0) {
                        mPairedDevicesArray.get(clickPosition).isConnection = false;
                    } else {
                        mNewDevicesArray.get(clickPosition).isConnection = false;
                    }
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
//                    setProgressBarIndeterminateVisibility(false);
//                    SetLinkButtonEnable(ListViewAdapter.ENABLE);
//                    Map<String, Object> map;
//                    map = mList.get(id);
//                    map.put(ListViewAdapter.STATUS, getString(R.string.connect));
//                    mList.set(id, map);
//                    mListViewAdapter.notifyDataSetChanged();
                } else if (type == GpDevice.STATE_VALID_PRINTER) {//连接成功
                    logE("GpDevice.STATE_VALID_PRINTER连接成功");
                    App.isConnection = true;
                    hideProgress();
                    finish();
                    mPortParam[id].setPortOpenState(true);
                    if (clickType == 0) {
                        mPairedDevicesArray.get(clickPosition).isConnection = true;
                    } else {
                        mNewDevicesArray.get(clickPosition).isConnection = true;
                    }
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
//                    setProgressBarIndeterminateVisibility(false);
//                    SetLinkButtonEnable(ListViewAdapter.ENABLE);
//                    Map<String, Object> map;
//                    map = mList.get(id);
//                    map.put(ListViewAdapter.STATUS, getString(R.string.cut));
//                    mList.set(id, map);
//                    mListViewAdapter.notifyDataSetChanged();
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {//打印机无效
                    logE("GpDevice.STATE_INVALID_PRINTER连接无效");
                    hideProgress();
                    App.isConnection = false;
                    if (clickType == 0) {
                        mPairedDevicesArray.get(clickPosition).isConnection = false;
                    } else {
                        mNewDevicesArray.get(clickPosition).isConnection = false;
                    }
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
//                    setProgressBarIndeterminateVisibility(false);
//                    SetLinkButtonEnable(ListViewAdapter.ENABLE);
//                    messageBox("Please use Gprinter!");
                    toask("请使用佳博打印机");
                }
            }
        }
    };

    private void toask(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void startService() {
        Intent i = new Intent(this, GpPrintService.class);
        startService(i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getPrinterStatusClicked() {
        String str = "";
        int status = 0;
        try {
            if (mGpService != null) {
//                status = mGpService.queryPrinterStatus(mPrinterIndex, 500);
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

    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_bluetooth_list);
        Log.e(DEBUG_TAG, "On Create");
        // TextView �����?
        tvPairedDevice = (TextView) findViewById(R.id.tvPairedDevices);
        //ListView �����?
        lvPairedDevice = (ListView) findViewById(R.id.lvPairedDevices);
        // TextView �µ�
        tvNewDevice = (TextView) findViewById(R.id.tvNewDevices);
        // ListView �µ�
        lvNewDevice = (ListView) findViewById(R.id.lvNewDevices);
        // Button ɨ���豸
        btDeviceScan = (Button) findViewById(R.id.btBluetoothScan);

        pb = (ProgressBar) findViewById(R.id.pb);
        LinearLayout llBluetoothScan = (LinearLayout) findViewById(R.id.llBluetoothScan);
        llBluetoothScan.setSelected(false);
        llBluetoothScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    if (mBluetoothAdapter != null) {
                        v.setSelected(false);
                        btDeviceScan.setText("搜索");
                        pb.setVisibility(View.GONE);
                        mBluetoothAdapter.cancelDiscovery();//停止搜索
                    }
                } else {
                    v.setSelected(true);
                    pb.setVisibility(View.VISIBLE);
                    btDeviceScan.setText("停止搜索");
                    mNewDevicesArrayAdapter.clear();
                    discoveryDevice();
                }
            }
        });
//        btDeviceScan.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (v.isSelected()) {
//                    if (mBluetoothAdapter != null) {
//                        v.setSelected(false);
//                        btDeviceScan.setText("搜索");
//                        pb.setVisibility(View.GONE);
//                        mBluetoothAdapter.cancelDiscovery();//停止搜索
//                    }
//                } else {
//                    v.setSelected(true);
//                    pb.setVisibility(View.VISIBLE);
//                    btDeviceScan.setText("停止搜索");
//                    discoveryDevice();
//                }
//            }
//        });
        findViewById(R.id.btExit).setOnClickListener(new OnClickListener() {//关闭蓝牙打印
            @Override
            public void onClick(View view) {
                showExit();
            }
        });
//        startService();
        getDeviceList();
        initPortParam();
//        initView();
        registerBroadcast();
        connection();
//        if (App.mGpService != null) {
//            mGpService = App.mGpService;
//        }
        System.out.println("asdf" + mGpService);
        btPrint = (Button) findViewById(R.id.btPrint);
        btPrint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getPrinterStatusClicked();
                if (s != null && !s.equals("") && s.equals("打印机正常")) {
                    try {
//                        int rel = mGpService.printeTestPage(mPrinterIndex); //
                        int rel = 0;
                        EscCommand esc = new EscCommand();
//                        esc.addPrintAndFeedLines((byte) 3);
//                        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
//                        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);//设置为倍高倍宽
//                        esc.addText("Sample\n");   //  打印文字
//                        esc.addPrintAndLineFeed();

        /*打印文字*/
                        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
                        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
                        esc.addText("上海澄泓信息科技有限公司\n");   //  打印文字
                        esc.addPrintAndLineFeed();//打印和换行
        /*打印图片*/
//                        esc.addText("Print bitmap!\n");   //  打印文字
//                        Bitmap b = BitmapFactory.decodeResource(getResources(),
//                                R.drawable.gprinter);
//                        esc.addRastBitImage(b, b.getWidth(), 0);   //打印图片

        /*打印一维条码*/
//                        esc.addText("Print code128\n");   //  打印文字
//                        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//设置条码可识别字符位置在条码下方
//                        esc.addSetBarcodeHeight((byte) 60); //设置条码高度为60点
//                        esc.addCODE128("Gprinter");  //打印Code128码
//                        esc.addPrintAndLineFeed();

        /*QRCode命令打印
            此命令只在支持QRCode命令打印的机型才能使用。
            在不支持二维码指令打印的机型上，则需要发送二维条码图片
        */
//                        esc.addText("Print QRcode\n");   //  打印文字
//                        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); //设置纠错等级
//                        esc.addSelectSizeOfModuleForQRCode((byte) 3);//设置qrcode模块大小
//                        esc.addStoreQRCodeData("www.gprinter.com.cn");//设置qrcode内容
//                        esc.addPrintQRCode();//打印QRCode
//                        esc.addPrintAndLineFeed();

        /*打印文字*/
//                        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印左对齐
//                        esc.addText("Completed!\r\n");   //  打印结束
//                        esc.addPrintAndFeedLines((byte) 8);

                        Vector<Byte> datas = esc.getCommand(); //发送数据
                        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
                        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
                        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
                        try {
                            rel = mGpService.sendEscCommand(mPrinterIndex, str);
                            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                            if (r != GpCom.ERROR_CODE.SUCCESS) {
                                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//                        mGpService.sendEscCommand(mPrinterIndex, "sdfsadf11111");
//                        mGpService.sendTscCommand(mPrinterIndex, "sdfsadf22222");
                        Log.i("ServiceConnection", "rel " + rel);
                        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                        if (r != GpCom.ERROR_CODE.SUCCESS) {
                            Toast.makeText(getApplicationContext(), GpCom.getErrorText(r),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            toask("打印成功");
                        }
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                    toask(s);
                }
            }
        });
    }

    AskDialog dialog;

    private void showExit() {
        if (dialog == null) {
            dialog = new AskDialog(this, "提示", "确认取消蓝牙打印？", new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    App.setBluetooth(false);
                    finish();
                }
            });
        }
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        if (mFindBlueToothReceiver != null) {//解除查找蓝牙广播
            unregisterReceiver(mFindBlueToothReceiver);
        }
        if (PrinterStatusBroadcastReceiver != null) {//解除蓝牙打印机广播
            unregisterReceiver(PrinterStatusBroadcastReceiver);
        }
    }

    /**
     * 检查请求参数
     *
     * @param param
     * @return
     */
    public Boolean CheckPortParamters(PortParameters param) {
        boolean rel = false;
        int type = param.getPortType();
        if (type == PortParameters.BLUETOOTH) {
            if (!param.getBluetoothAddr().equals("")) {
                rel = true;
            }
        } else if (type == PortParameters.ETHERNET) {
            if ((!param.getIpAddr().equals("")) && (param.getPortNumber() != 0)) {
                rel = true;
            }
        } else if (type == PortParameters.USB) {
            if (!param.getUsbDeviceName().equals("")) {
                rel = true;
            }
        }
        return rel;
    }

    /**
     * 连接已配对的蓝牙设备
     *
     * @param position
     * @param state    1已配对设备，2搜索到的新设备
     */
    public void connectOrDisConnectToDevice(final int position, int state) {
        if (mGpService == null) {
            toask("蓝牙打印机服务未启动");
            return;
        }
        int PrinterId = 0;
        if (state == 1) {
            mPortParam[PrinterId].setBluetoothAddr(mPairedDevicesArray.get(PrinterId).address);
            App.blueboothAddress = mPairedDevicesArray.get(PrinterId).address;
        } else if (state == 2) {
            mPortParam[PrinterId].setBluetoothAddr(mNewDevicesArray.get(PrinterId).address);
            App.blueboothAddress = mNewDevicesArray.get(PrinterId).address;
        }
        mPortParam[PrinterId].setPortType(PortParameters.BLUETOOTH);

        mPrinterId = PrinterId;
        int rel = 0;
        if (mPortParam[PrinterId].getPortOpenState() == false) {
            if (CheckPortParamters(mPortParam[PrinterId])) {
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
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                logE("rel"+rel);
                logE("r"+r);
                if (r != GpCom.ERROR_CODE.SUCCESS) {//连接成功
                    if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                        toask("连接成功");
                        mPortParam[PrinterId].setPortOpenState(true);
                        if (state == 1) {
                            mPairedDevicesArray.get(position).isConnection = true;
                            mPairedDevicesArrayAdapter.notifyDataSetChanged();
                        } else if (state == 2) {
                            mNewDevicesArray.get(position).isConnection = true;
                            mNewDevicesArrayAdapter.notifyDataSetChanged();
                        }
                        App.isConnection = true;
                        finish();
                        hideProgress();
                    } else {
                        toask("连接失败");
                    }
                }
            } else {
                Log.d(DEBUG_TAG, "DisconnectToDevice ");
                try {
                    mGpService.closePort(PrinterId);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 连接扫描到的蓝牙设备
     *
     * @param PrinterId
     */

    public void connectOrDisConnectToNewDevice(int PrinterId) {
        if (mPortParam.length > mNewDevicesArray.size()) {
            mPortParam[PrinterId].setBluetoothAddr(mNewDevicesArray.get(PrinterId).address);
            mPortParam[PrinterId].setPortType(PortParameters.BLUETOOTH);
        } else {
            toask("超过最大连接数量");
        }
        mPrinterId = PrinterId;
        int rel = 0;
        if (mPortParam[PrinterId].getPortOpenState() == false) {
            if (CheckPortParamters(mPortParam[PrinterId])) {
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
                            System.out.println("sdfasdf");

                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {//已连接
                        mPortParam[PrinterId].setPortOpenState(true);
                        mPairedDevicesArray.get(PrinterId).isConnection = true;
//                        Map<String, Object> map;
//                        map = mList.get(PrinterId);
//                        map.put(ListViewAdapter.STATUS, getString(R.string.cut));
//                        mList.set(PrinterId, map);
//                        mListViewAdapter.notifyDataSetChanged();
                    } else {
                        toask(GpCom.getErrorText(r));
                    }
                }
            } else {
                toask(getString(R.string.port_parameters_wrong));
            }
        } else {
            Log.d(DEBUG_TAG, "DisconnectToDevice ");
//            setProgressBarIndeterminateVisibility(true);
//            SetLinkButtonEnable(ListViewAdapter.DISABLE);
//            Map<String, Object> map;
//            map = mList.get(PrinterId);
//            map.put(ListViewAdapter.STATUS,
//                    getString(R.string.cutting));
//            mList.set(PrinterId, map);
//            mListViewAdapter.notifyDataSetChanged();
            try {
                mGpService.closePort(PrinterId);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    protected void getDeviceList() {
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
//        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
//                R.layout.bluetooth_device_name_item);

        mPairedDevicesArrayAdapter = new LvAdapter<Bluebooth>(this, mPairedDevicesArray, R.layout.lv_item_blue_booth) {
            @Override
            public void convert(LvViewHolder helper, final Bluebooth item, final int position) {
                helper.setText(R.id.tv1, item.name);
                TextView tv1 = helper.getView(R.id.tv1);
                final TextView tv2 = helper.getView(R.id.tv2);
                helper.setText(R.id.tv2, "连接");
                if (item.isConnection) {
                    tv2.setText("断开");
                } else {
                    tv2.setText("连接");
                }
                if (item.name.equals(getString(R.string.none_paired)) || item.name.equals(getString(R.string.none_bluetooth_device_found))) {//没有已配对的设备
                    tv2.setVisibility(View.GONE);
                } else {
                    tv2.setVisibility(View.VISIBLE);
                }
                if (!tv2.getText().toString().equals(getString(R.string.none_paired))) {
                    tv2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tv2.getText().toString().equals("连接")) {
                                clickType = 0;
                                clickPosition = position;
                                connectOrDisConnectToDevice(position, 1);
                                progress("连接中...");
                            } else {
                                try {
                                    mGpService.closePort(position);
                                    tv2.setText("连接");
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        };
        lvPairedDevice.setAdapter(mPairedDevicesArrayAdapter);
//        lvPairedDevice.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mBluetoothAdapter.cancelDiscovery();//取消扫描蓝牙设备
//                // Get the device MAC address, which is the last 17 chars in the View
//                if (view != null) {
//                    TextView tv = (TextView) view.findViewById(R.id.tv1);
//                    String info = tv.getText().toString();
//                    String noDevices = getResources().getText(R.string.none_paired).toString();
//                    String noNewDevice = getResources().getText(R.string.none_bluetooth_device_found).toString();
//                    Log.i("tag", info);
//                    if (!info.equals(noDevices) && !info.equals(noNewDevice)) {
//                        connectOrDisConnectToDevice(position, 1);
//                    }
//                }
//            }
//        });

//        mPairedDevicesArrayAdapter = new lv
//        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
//                R.layout.bluetooth_device_name_item);
        mNewDevicesArrayAdapter = new LvAdapter<Bluebooth>(this, mNewDevicesArray, R.layout.lv_item_blue_booth) {
            @Override
            public void convert(LvViewHolder helper, final Bluebooth item, final int position) {
                if (item == null) {
                    return;
                }
                helper.setText(R.id.tv1, item.name);
                TextView tv1 = helper.getView(R.id.tv1);
                final TextView tv2 = helper.getView(R.id.tv2);
                if (item.name.equals(getString(R.string.none_paired)) || item.name.equals(getString(R.string.none_bluetooth_device_found))) {//没有已配对的设备
                    tv2.setVisibility(View.GONE);
                } else {
                    tv2.setVisibility(View.VISIBLE);
                }
                helper.setText(R.id.tv2, "连接");
                if (item.isConnection) {
                    tv2.setText("断开");
                } else {
                    tv2.setText("连接");
                }
                if (!tv2.getText().toString().equals(getString(R.string.none_paired)) || item.name.equals(getString(R.string.none_bluetooth_device_found))) {
                    tv2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tv2.getText().toString().equals("连接")) {
                                progress("连接中...");
                                clickType = 1;
                                clickPosition = position;
                                connectOrDisConnectToDevice(position, 2);
                            } else {
                                try {
                                    mGpService.closePort(position);
                                    tv2.setText("连接");
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        };

        lvNewDevice.setAdapter(mNewDevicesArrayAdapter);
//        lvNewDevice.setOnItemClickListener(mDeviceClickListener);
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            tvPairedDevice.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
//                mPairedDevicesArrayAdapter.add(device.getName()+device.getAddress()+ "\n");
                mPairedDevicesArrayAdapter.add(new Bluebooth(device.getName(), device.getAddress(), false));
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired)
                    .toString();
//            mPairedDevicesArrayAdapter.add(noDevices);
            mPairedDevicesArrayAdapter.add(new Bluebooth(noDevices, null, false));
        }
    }

    // changes the title when discovery is finished
    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
//                pb.setVisibility(View.GONE);
//                btDeviceScan.setSelected(false);
//                btDeviceScan.setText("搜索");
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    mNewDevicesArrayAdapter.add(device.getName() + "\n"
//                            + device.getAddress());
                    mNewDevicesArrayAdapter.add(new Bluebooth(device.getName(), device.getAddress(), false));
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                pb.setVisibility(View.GONE);
                btDeviceScan.setSelected(false);
                btDeviceScan.setText("搜索");
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_bluetooth_device);
                Log.i("tag", "finish discovery" + mNewDevicesArrayAdapter.getCount());
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(
                            R.string.none_bluetooth_device_found).toString();
//                    mNewDevicesArrayAdapter.add(noDevices);
                    mNewDevicesArrayAdapter.add(new Bluebooth(getString(R.string.none_bluetooth_device_found), "", false));
                }
            }
        }
    };

    private void discoveryDevice() {
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scaning);
        // Turn on sub-title for new devices
        tvNewDevice.setVisibility(View.VISIBLE);

        lvNewDevice.setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();//取消扫描蓝牙设备
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String noDevices = getResources().getText(R.string.none_paired).toString();
            String noNewDevice = getResources().getText(R.string.none_bluetooth_device_found).toString();
            Log.i("tag", info);
            if (!info.equals(noDevices) && !info.equals(noNewDevice)) {
                connectOrDisConnectToNewDevice(arg2);
//                mPairedDevicesArrayAdapter.add(new Bluebooth(info.substring(info.length() - 17)));
//                String address = info.substring(info.length() - 17);
//                // Create the result Intent and include the MAC address
//                Intent intent = new Intent();
//                intent.putExtra(PortConfigurationActivity.EXTRA_DEVICE_ADDRESS, address);
//                // Set result and finish this Activity
//                setResult(Activity.RESULT_OK, intent);
//                finish();
            }
        }
    };


}
