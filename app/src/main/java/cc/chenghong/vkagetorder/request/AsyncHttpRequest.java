package cc.chenghong.vkagetorder.request;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;

import java.util.Map;

import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.util.StringUtils;

/**
 * AsyncHttp连接类
 * Created by 何成龙 on 2016/3/16.
 */
public class AsyncHttpRequest extends AsyncHttpClient {
    private static final String TAG = "AsyncHttpRequest";
    private static AsyncHttpClient client = new AsyncHttpRequest();

    /**
     * 获取一个连接器，如果没有就新建一个
     *
     * @return
     */
    public static AsyncHttpClient getClient() {
        if (client == null) {
            client = new AsyncHttpClient();
        }
        return client;
    }

    /**
     * 发送需要头部参数的post请求（不适用于处理json请求格式的接口）
     *
     * @param url
     * @param asyncHttpParam
     * @param asyncHttpResponseHandler
     */
    public static void postHead(String url, AsyncHttpParam asyncHttpParam, cc.chenghong.vkagetorder.request.AsyncHttpResponseHandler asyncHttpResponseHandler) {
        client.addHeader("parentCode", App.getAccount().getParentCode());
        client.addHeader("code", App.getAccount().getStoreCode() + "");
        client.addHeader("accessToken", App.getAccount().getAccessToken());
        client.addHeader("Content-Type", "application/json");

        asyncHttpParam.putHeader("parentCode", App.getAccount().getParentCode()).
                putHeader("code", App.getAccount().getStoreCode() + "").
                putHeader("accessToken", App.getAccount().getAccessToken());

//        soutLog(url,"POST",asyncHttpParam,null);
        client.post(url, asyncHttpParam.getParam(), asyncHttpResponseHandler);
    }

    /**
     * 发送表单格式的post请求（不适用于只处理json请求格式的接口）
     *
     * @param url
     * @param asyncHttpParam
     * @param asyncHttpResponseHandler
     */
    public static void postForm(String url, AsyncHttpParam asyncHttpParam, cc.chenghong.vkagetorder.request.AsyncHttpResponseHandler asyncHttpResponseHandler) {
        soutLog(url, "POST", asyncHttpParam);
    }

    /**
     * 发送post请求（不适用于处理json请求格式的接口）
     *
     * @param url
     * @param asyncHttpParam
     * @param asyncHttpResponseHandler
     */
    public static void post(String url, AsyncHttpParam asyncHttpParam, cc.chenghong.vkagetorder.request.AsyncHttpResponseHandler asyncHttpResponseHandler) {
        if (asyncHttpParam == null) {
            new NullPointerException("asyncHttpParam not null");
            return;
        }
        if (asyncHttpParam.getHeaders() == null) {
            new NullPointerException("Headers not null");
            return;
        }
        logI("请求方式：POST");
        logI("请求地址：" + url);
        for (Map.Entry<String, Object> entry : asyncHttpParam.getHeaders().entrySet()) {
            logI("头部参数键：" + entry.getKey() + ",头部参数值：" + entry.getValue());
            client.addHeader(entry.getKey(), entry.getValue().toString());
        }
        client.post(url, asyncHttpParam.getParam(), asyncHttpResponseHandler);
    }

    /**
     * 有内容参数的get请求
     *
     * @param url
     * @param asyncHttpParam
     * @param asyncHttpResponseHandler
     */
    public static void get(String url, AsyncHttpParam asyncHttpParam, cc.chenghong.vkagetorder.request.AsyncHttpResponseHandler asyncHttpResponseHandler) {
        soutLog(url, "GET", asyncHttpParam);
        if (StringUtils.noEmpty(asyncHttpParam.getParam())) {//有内容参数的get请求
            client.get(url, asyncHttpParam.getParam(), asyncHttpResponseHandler);
        } else {//无内容参数的get请求
            client.get(url, asyncHttpResponseHandler);
        }
    }

    /**
     * 没有内容参数的get请求
     *
     * @param url
     * @param asyncHttpResponseHandler
     */
    public static void get(String url, cc.chenghong.vkagetorder.request.AsyncHttpResponseHandler asyncHttpResponseHandler) {
//        soutLog(url, "GET", null, null);
        logI("请求地址：" + url);
        logI("请求方式：GET");
        client.get(url, asyncHttpResponseHandler);
    }

    private static void logI(String str) {
        Log.i(TAG, str);
    }

    /**
     * 输出log日志
     *
     * @param url            请求地址
     * @param requestMode    请求方式
     * @param asyncHttpParam 请求参数
     */
    private static void soutLog(String url, String requestMode, AsyncHttpParam asyncHttpParam) {
        if (requestMode != null) {
            logI("请求方式：" + requestMode);
        }
        if (url != null) {
            logI("请求地址：" + url);
        }
//        client.removeAllHeaders();//清空所有头部参数，以免影响到本次网络请求
        if (asyncHttpParam != null || asyncHttpParam.getHeaders() != null) {
            for (Map.Entry<String, Object> entry : asyncHttpParam.getHeaders().entrySet()) {
                logI("头部参数键：" + entry.getKey() + ",头部参数值：" + entry.getValue());
                client.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        if (asyncHttpParam != null && asyncHttpParam.getParam() != null) {
            String vals[] = asyncHttpParam.getParam().toString().split("&");
            for (int i = 0; i < vals.length; i++) {
                String val[] = vals[i].split("=");
                logI("内容参数键：" + val[0] + "，内容参数值：" + val[1]);
            }
            logI("原始内容参数：" + asyncHttpParam.getParam().toString());
        }
//        if (StringUtils.noEmpty(asyncHttpParam.getParam())){//有内容参数的post请求
//            client.post(url, asyncHttpParam.getParam(), asyncHttpResponseHandler);
//        }else {//无内容参数的post请求
//            client.post(url,  asyncHttpResponseHandler);
//        }
    }

}
