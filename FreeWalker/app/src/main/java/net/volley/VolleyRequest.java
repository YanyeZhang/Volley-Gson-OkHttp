package net.volley;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import java.util.Map;

import application.FreeWalkerApplication;

/**
 * Created by zhangyanye on 2015/8/22
 * Description:自定义volley请求
 */
public class VolleyRequest {

    private static VolleyRequest mVolleyRequest;
    private static RequestQueue mRequestQueues;

    private VolleyRequest() {
        mRequestQueues = Volley.newRequestQueue(FreeWalkerApplication.getApp(), new OkHttpStack(new OkHttpClient()));
    }

    public static VolleyRequest getInstance() {
        if (mVolleyRequest == null) {
            synchronized (mVolleyRequest) {
                if (mVolleyRequest == null) {
                    mVolleyRequest = new VolleyRequest();
                }
            }
        }
        return mVolleyRequest;
    }

    /**
     * 初始化 Get请求
     *
     * @param url      ：request Url
     * @param tag      ：唯一标示符
     * @param listener ：监听回调事件
     * @return
     */
    public CustomRequest setGetRequest(String url, Object tag, @NonNull VolleyListener listener) {
        mRequestQueues.cancelAll(tag);
        CustomRequest request = new CustomRequest(url, listener.sucessListener(), listener.errorListener());
        request.setTag(tag);
        return request;
    }

    /**
     * 初始化 Post请求
     *
     * @param url      ： request url
     * @param tag      ：唯一标符
     * @param params   ：参数
     * @param listener ：监听回调事件
     * @return
     */
    public CustomRequest setPostRequest(String url, Object tag, final Map params,@NonNull VolleyListener listener) {
        mRequestQueues.cancelAll(tag);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, listener.sucessListener(), listener.errorListener()) {
            @Override
            protected Map getParams() throws AuthFailureError {
                return params;
            }
        };
        request.setTag(tag);
        return request;
    }


    public void RequestGet(String url, Object tag, @NonNull VolleyListener listener) {
        start(setGetRequest(url, tag, listener));
    }

    public void RequestPost(String url, Object tag, final ArrayMap<String, String> params, @NonNull VolleyListener listener) {
        start(setPostRequest(url, tag, params, listener));
    }

    /**
     * 加入请求队列后执行请求
     *
     * @param request
     */
    public void start(Request request) {
        mRequestQueues.add(request);
        mRequestQueues.start();
    }

    /**
     * 获取请求队列
     *
     * @return
     */
    public RequestQueue getRequestQueues() {
        return mRequestQueues;
    }
}



