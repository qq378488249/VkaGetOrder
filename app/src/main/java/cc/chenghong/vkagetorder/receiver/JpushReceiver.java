package cc.chenghong.vkagetorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import cc.chenghong.vkagetorder.activity.MainActivity;
import cc.chenghong.vkagetorder.activity_pad.PadMainActivity;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Push;
import cc.chenghong.vkagetorder.fragment_pad.PadDdFragment;
import cc.chenghong.vkagetorder.util.StringUtils;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by 何成龙 on 2016/8/8.
 */
public class JpushReceiver extends BroadcastReceiver {
    private Handler mHander;

    public JpushReceiver(Handler mHander) {
        this.mHander = mHander;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (PadMainActivity.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {//接受到极光推送订单
            Push push = (Push) intent.getSerializableExtra("push");
            if (StringUtils.noEmpty(push) && StringUtils.noEmpty(push.getALERT())) {
                Message msg = Message.obtain();
                msg.obj = push;
                msg.what = 200;
                mHander.sendMessage(msg);
            }
        } else if (MainActivity.MESSAGE_REFRESH.equals(intent.getAction())) {//刷新列表
            mHander.sendEmptyMessage(329);
        }
    }
}
