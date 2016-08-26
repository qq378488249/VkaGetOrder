package cc.chenghong.vkagetorder.request;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

import cc.chenghong.vkagetorder.util.StringUtils;

/**
 * AsyncHttp连接参数类
 * Created by hcl on 2016/4/28.
 */
public class AsyncHttpParam {
    private RequestParams param = new RequestParams();
    private Map<String, Object> headers = new HashMap<>();

    /**
     * 存入内容参数
     *
     * @param key
     * @param val
     * @return
     */
    public AsyncHttpParam putParam(String key, Object val) {
        if (StringUtils.noEmpty(key, val)) {
            param.put(key, val);
        }
        return this;
    }

    /**
     * 存入头部参数
     *
     * @param key
     * @param val
     * @return
     */
    public AsyncHttpParam putHeader(String key, Object val) {
        if (StringUtils.noEmpty(key, val)) {
            headers.put(key, val);
        }
        return this;
    }

    /**
     * 清空所有内容参数
     */
    public void removeAllParam() {
        param = null;
        param = new RequestParams();
    }

    /**
     * 清除相应内容参数
     *
     * @param key
     */
    public void removeParam(String key) {
        param.remove(key);
    }

    /**
     * 清空所有头部参数
     */
    public void removeAllHeader() {
        headers.clear();
    }

    public static AsyncHttpParam New() {
        return new AsyncHttpParam();
    }

    /**
     * 获取内容参数
     *
     * @return
     */
    public RequestParams getParam() {
        return param;
    }

    /**
     * 获取头部参数
     *
     * @return
     */
    public Map<String, Object> getHeaders() {
        return headers;
    }
}
