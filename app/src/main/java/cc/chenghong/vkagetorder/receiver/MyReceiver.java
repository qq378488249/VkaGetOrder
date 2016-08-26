package cc.chenghong.vkagetorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cc.chenghong.vkagetorder.activity.MainActivity;
import cc.chenghong.vkagetorder.activity_pad.PadMainActivity;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Push;
import cc.chenghong.vkagetorder.util.ExampleUtil;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            sendMessage2MainActivity(context, bundle);
            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);//普通消息
//            App.newOrderVoice(alert);
            App.newOrderVoice("您有新的订单，请及时处理");
//			Bundle[{cn.jpush.android.ALERT=blue1我, cn.jpush.android.EXTRA={}, cn.jpush.android.NOTIFICATION_ID=186319373, cn.jpush.android.NOTIFICATION_CONTENT_TITLE=VkaGetOrder, cn.jpush.android.MSG_ID=3161165948}]
//			String message = bundle.getString("cn.jpush.android.ALERT");
//            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
//            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            String msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);
//            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            if (App.isMobile()) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                sendMessage2MainActivity(context, bundle);
            } else {
                Intent i = new Intent(context, PadMainActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                sendMessage2MainActivity(context, bundle);
            }
//            if (MainActivity.isDestroy){//如果已经被销毁，则重新新建主页面
            //打开自定义的Activity

//			}else{
//				App.getInstance().star
//			}
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity发送消息到主活动
    private void processCustomMessage(Context context, Bundle bundle) {
        if (!MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            context.sendBroadcast(msgIntent);
        } else {
            Toast.makeText(App.getInstance(), "新通知", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 发送消息到主页面
     *
     * @param context
     * @param bundle
     */
    private void sendMessage2MainActivity(Context context, Bundle bundle) {
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);//普通消息
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//通知栏消息标题
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//附加消息
        String msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);//极光推送消息id
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//通知栏消息id

        Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        msgIntent.putExtra("push", new Push(alert, null, notifactionId, title, msg_id));
        context.sendBroadcast(msgIntent);
        if (MainActivity.isForeground || PadMainActivity.isForeground) {
//            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);//普通消息
//            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//通知栏消息标题
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//附加消息
//            String msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);//极光推送消息id
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//通知栏消息id
//
            JPushInterface.clearNotificationById(App.getInstance(), notifactionId);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra("push", new Push(alert, null, notifactionId, title, msg_id));
//            context.sendBroadcast(msgIntent);
        }
    }
}