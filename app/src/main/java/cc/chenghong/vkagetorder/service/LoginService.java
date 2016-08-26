package cc.chenghong.vkagetorder.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import cc.chenghong.vkagetorder.activity.BlueActivity;
import cc.chenghong.vkagetorder.activity.LoginActivity;
import cc.chenghong.vkagetorder.activity.MainActivity;
import cc.chenghong.vkagetorder.activity_pad.PadLoginActivity;
import cc.chenghong.vkagetorder.activity_pad.PadMainActivity;
import cc.chenghong.vkagetorder.api.Api;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Account;
import cc.chenghong.vkagetorder.param.VolleyParam;
import cc.chenghong.vkagetorder.request.AsyncHttpParam;
import cc.chenghong.vkagetorder.request.VolleyRequest;
import cc.chenghong.vkagetorder.util.SharedPreferencesHelper;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by 何成龙 on 2016/7/13.
 */
public class LoginService {
    /**
     * 登陆方法
     *
     * @param ac
     * @param param
     */
    public void login(final BlueActivity ac, final VolleyParam param, final AsyncHttpParam asyncHttpParam) {
//        final BlueActivity ac = (BlueActivity) activity;
        ac.progress("登陆中...");
//        AsyncHttpRequest.post(Api.LOGIN, asyncHttpParam, new AsyncHttpResponseHandler<Account>() {
//            @Override
//            public void onSuccess(int statusCode, Account data, Header[] headers) {
//                ac.hideProgress();
//                if (data.isSuccess()) {
//                    SharedPreferencesHelper.saveString("parentCode", param.getHeaders().get("parentCode") + "");
//                    App.initJpush();
//                    JPushInterface.resumePush(App.getInstance());
//                    String registrationID = JPushInterface.getRegistrationID(App.getInstance());
//                    System.out.println(registrationID + "xxxxxxxxx");
//                    data.data.setParentCode(param.getHeaders().get("parentCode") + "");
//                    data.data.setStoreCode(param.getHeaders().get("code") + "");
//                    App.putAccount(data.data);
//                    App.setJPushInterfaceTagThread(0);
//                    App.initAsyncHttpRequestHeader();
//
//                    ac.toask("登陆成功");
//                    ac.setLogin(true);
//                    if (!App.isMobile()) {
//                        ac.startActivity(PadMainActivity.class);
//                    } else {
//                        ac.startActivity(MainActivity.class);
//                    }
//                    ac.finish();
//                } else {
//                    ac.toask("登陆失败" + data.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
//                ac.hideProgress();
//                ac.toask(error.toString());
//            }
//        });

        VolleyRequest.postJson(Api.LOGIN, param, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                ac.hideProgress();
                Account data = ac.fromJson(o.toString(), Account.class);
                if (data.isSuccess()) {
                    SharedPreferencesHelper.saveString("parentCode", param.getHeaders().get("parentCode") + "");
                    App.initJpush();
                    JPushInterface.resumePush(App.getInstance());
                    String registrationID = JPushInterface.getRegistrationID(App.getInstance());
                    System.out.println(registrationID + "xxxxxxxxx");
                    data.data.setParentCode(param.getHeaders().get("parentCode") + "");
                    data.data.setStoreCode(param.getHeaders().get("code") + "");
                    App.putAccount(data.data);
                    App.setJPushInterfaceTagThread(0);
                    App.initAsyncHttpRequestHeader();

                    ac.toask("登陆成功");
                    ac.setLogin(true);
                    if (!App.isMobile()) {
                        ac.startActivity(PadMainActivity.class);
                    } else {
                        ac.startActivity(MainActivity.class);
                    }
                    ac.finish();
                } else {
                    ac.toask("登陆失败" + data.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ac.volleyError(volleyError);
            }
        });
    }

    /**
     * 退出登陆方法
     */
    public void exitLogin(BlueActivity ac) {
        App.setLogin(false);
        App.setBluetooth(false);
        App.setMessage(false);
        JPushInterface.clearAllNotifications(App.getInstance());
        JPushInterface.stopPush(App.getInstance());
        if (!App.isMobile()) {
            ac.startActivity(PadLoginActivity.class);
        } else {
            ac.startActivity(LoginActivity.class);
        }
        ac.finish();
    }
}
