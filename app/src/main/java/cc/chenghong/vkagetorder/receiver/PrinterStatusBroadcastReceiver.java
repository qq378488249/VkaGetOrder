package cc.chenghong.vkagetorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cc.chenghong.vkagetorder.app.App;

/**
 * Created by 何成龙 on 2016/8/12.
 */
public class PrinterStatusBroadcastReceiver extends BroadcastReceiver {
    String DEBUG_TAG = getClass().getSimpleName();
    //    蓝牙打印机连接状态广播
    public static final String ACTION_CONNECT_STATUS = "action.connect.status";

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (ACTION_CONNECT_STATUS.equals(intent.getAction())) {
//            int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
//            int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
//            Log.d(DEBUG_TAG, "connect status " + type);
//            if (type == GpDevice.STATE_CONNECTING) {//连接中
//                App.isConnection = false;
//                Log.e(DEBUG_TAG, "GpDevice.STATE_CONNECTING连接中");
//            } else if (type == GpDevice.STATE_NONE) {//未连接
//                logE("GpDevice.STATE_NONE未连接");
//            } else if (type == GpDevice.STATE_VALID_PRINTER) {//连接成功
//                logE("GpDevice.STATE_VALID_PRINTER连接成功");
//                App.isConnection = true;
//            } else if (type == GpDevice.STATE_INVALID_PRINTER) {//打印机无效
//                logE("GpDevice.STATE_INVALID_PRINTER连接无效");
//                App.isConnection = false;
//            }
//        }

        if("action.connect.status".equals(intent.getAction())) {
            int type = intent.getIntExtra("connect.status", 0);
            int id = intent.getIntExtra("printer.id", 0);
            logE("PRINTER_ID:" + id);
            logE("CONNECT_STATUS:" + type);
            App.falseConnection();
            if(type == 0) {
                logE("打印机-连接断开");
            } else if(type == 1) {
                logE("打印机-监听状态");
            } else if(type == 2) {
                logE("打印机-正在连接");
            } else if(type == 3) {
                logE("打印机-已连接");
            } else if(type == 4) {
                logE("打印机-无效的打印机");
            } else if(type == 5) {
                App.index = 1;
                App.trueConnection();
                logE("打印机-有效的打印机");
            }else{
                logE("打印机-未知状态");
            }
        }
    }

    private void logE(String s) {
        Log.e(DEBUG_TAG, s);
    }
}
