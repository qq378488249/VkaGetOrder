package cc.chenghong.vkagetorder.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.listener.VolleyListener;
import cc.chenghong.vkagetorder.param.VolleyParam;

/**
 * Volley连接基类
 * Created by 何成龙 on 2016/7/12.
 */
public class VolleyRequest {
    private static final String TAG = "VolleyRequest";
    private static RequestQueue requestQueue = null;
    private static int initialTimeoutMs = 10 * 1000;//请求超时时间
    private static int maxNumRetries = 0;//最大重试次数
    private static float backoffMultiplier = 1f;//倒扣称数
    private static Context mContext;
    private String mainUrl = "";//主地址

    public static void init(Context context) {
        mContext = context;
        requestQueue = Volley.newRequestQueue(App.getInstance());
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    /**
     * 发送请求
     *
     * @param request
     */
    public static void addRequest(Request request) {
        String method = "";
        switch (request.getMethod()) {
            case 0:
                method = "GET";
                break;
            case 1:
                method = "POST";
                break;
            case 2:
                method = "PUT";
                break;
            case 3:
                method = "DELETE";
                break;
            default:
                break;
        }
        i("请求方式：" + method);
        Log.i(TAG, "请求地址：" + request.getUrl());
        // 打印头部参数
        try {
            Map<String, String> headers = request.getHeaders();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    Log.i(TAG, "头部参数键：" + entry.getKey() +
                            ",头部参数值：" + entry.getValue());
                }
            }
        } catch (AuthFailureError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //设置重试策略
        request.setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs, maxNumRetries, backoffMultiplier));
        request.setTag("tag");
        requestQueue.add(request);
    }

    /**
     * 关闭所有请求
     */
    public static void cancelAll() {
        requestQueue.cancelAll("tag");
    }

    /**
     * 发送json格式的post请求
     *
     * @param url           请求地址
     * @param params        json格式的参数
     * @param headers       头部参数
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void postJson(String url, Map params, final Map headers,
                                Response.Listener listener, Response.ErrorListener errorListener) {
        JSONObject jsonParam = new JSONObject(params);
        i("发送的json参数：" + jsonParam);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParam, listener, errorListener) {
            public Map getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        addRequest(request);
    }

    /**
     * 发送json格式的post请求
     *
     * @param url           请求地址
     * @param param         请求参数
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void postJson(String url, final VolleyParam param,
                                Response.Listener listener, Response.ErrorListener errorListener) {
        if (param == null || param.getParams() == null) {
            new NullPointerException("Json Params not null");
            return;
        }
        if (param.getHeaders() == null) {
            new NullPointerException("Headers not null");
            return;
        }
//        JsonObjectRequest request = null;
        JSONObject jsonParam = new JSONObject(param.getParams());
        i("json参数：" + jsonParam);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParam, listener, errorListener) {
            public Map getHeaders() throws AuthFailureError {
                return param.getHeaders();
            }
        };
        addRequest(request);
    }

    /**
     * 发送无内容参数但是有头部请求参数的post请求
     *
     * @param url           请求地址
     * @param param         请求参数
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void postHeaders(String url, final VolleyParam param,
                                   Response.Listener listener, Response.ErrorListener errorListener) {
        if (param == null || param.getHeaders() == null) {
            new NullPointerException("Headers not null");
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            public Map getHeaders() throws AuthFailureError {
                return param.getHeaders();
            }
        };
        addRequest(request);
    }

    /**
     * 发送无内容参数也无头部请求参数的post请求
     *
     * @param url           请求地址
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void post(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        StringRequest request = null;
        request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
        };
        addRequest(request);
    }

    /**
     * 发送需要header参数的get请求
     *
     * @param url           请求地址
     * @param param         参数
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void getHeaders(String url, final VolleyParam param,
                                  Response.Listener listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                // TODO Auto-generated method stub
                return param.getHeaders();
            }
        };
        addRequest(stringRequest);
    }

    /**
     * 发送需要header参数的get请求
     *
     * @param url           请求地址
     * @param headers       头部参数
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void get(String url, final Map headers,
                           Response.Listener listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                // TODO Auto-generated method stub
                return headers;
            }
        };
        addRequest(stringRequest);
    }

    /**
     * 发送普通get请求
     *
     * @param url           请求地址
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void get(String url,
                           Response.Listener listener, Response.ErrorListener errorListener) {
        //打印请求方式
//		i("请求方式：get");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener) {
        };
        addRequest(stringRequest);
    }

    static void i(String s) {
        Log.i(TAG, s);
    }

    public static void setInitialTimeoutMs(int initialTimeoutMs) {
        VolleyRequest.initialTimeoutMs = initialTimeoutMs;
    }

    public static void setMaxNumRetries(int maxNumRetries) {
        VolleyRequest.maxNumRetries = maxNumRetries;
    }

    public static void setBackoffMultiplier(float backoffMultiplier) {
        VolleyRequest.backoffMultiplier = backoffMultiplier;
    }

    /**
     * 发送普通get请求
     *
     * @param url            请求地址
     * @param volleyListener 请求监听器
     */
    public static void get(String url, VolleyListener volleyListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, volleyListener.getListener(), volleyListener.getErrorListener()) {
        };
        addRequest(stringRequest);
    }

    /**
     * 发送带头部参数的get请求
     *
     * @param url            请求地址
     * @param volleyListener 请求监听器
     */
    public static void getHeaders(String url, final VolleyParam volleyParam, VolleyListener volleyListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, volleyListener.getListener(), volleyListener.getErrorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (volleyParam.getHeaders() != null) {
                    return volleyParam.getHeaders();
                } else {
                    return super.getHeaders();
                }
            }
        };
        addRequest(stringRequest);
    }

}
