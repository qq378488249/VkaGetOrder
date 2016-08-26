package cc.chenghong.vkagetorder.header;

import java.util.HashMap;

/** Volley头部参数类
 * Created by 何成龙 on 2016/7/12.
 */
public class VolleyHeader {
    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    private HashMap<String, String> headers = new HashMap<String, String>();

    private VolleyHeader put(String key,String val){
        headers.put(key, val);
        return this;
    }

    private VolleyHeader clear() {
        headers.clear();
        return this;
    }

    private HashMap<String, String> getHeaders(){
        return headers;
    }

}
