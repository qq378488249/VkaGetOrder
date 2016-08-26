package cc.chenghong.vkagetorder.listener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Created by 何成龙 on 2016/8/18.
 */
public abstract class VolleyListener<T> {
    private Type type;

    public VolleyListener() {
        Type mySuperClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
        this.type = type;
    }

    private Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String t) {
            onSuccess((T) new Gson().fromJson(t, type));
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            onFail(volleyError);
        }
    };

    /**
     * 请求成功监听器
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败监听器
     *
     * @param volleyError
     */
    public abstract void onFail(VolleyError volleyError);

    public Response.Listener<String> getListener() {
        return listener;
    }

    public void setListener(Response.Listener<String> listener) {
        this.listener = listener;
    }

    public Response.ErrorListener getErrorListener() {
        return errorListener;
    }

    public void setErrorListener(Response.ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

}
