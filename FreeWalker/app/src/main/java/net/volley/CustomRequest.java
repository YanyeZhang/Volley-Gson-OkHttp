package net.volley;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhangyanye on 2015/9/17.
 * Description:自定义Requeset
 */
public class CustomRequest extends StringRequest {


    private Priority mPriority;
    private long mCacheTime = 1000 * 60;// 1m


    public CustomRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public CustomRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return mPriority == null ? Priority.NORMAL : mPriority;
    }

    /**
     * 设置优先级
     *
     * @param priority Priority.LOW // images, thumbnails, ...
     *                 Priority.NORMAL // residual
     *                 Priority.HIGH // descriptions, lists, ...
     *                 Priority.IMMEDIATE // login, logout, ...
     * @return
     */
    public final CustomRequest setPriority(Priority priority) {
        this.mPriority = priority;
        return this;
    }


    /**
     * 设置重连策略
     *
     * @param timeOut   :time out 时间
     * @param retryTime :最大的重连次数
     * @return
     */
    public final CustomRequest setDefaultRetryPolicy(int timeOut, int retryTime) {
        if (retryTime == 0)
            retryTime = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
        if (timeOut == 0)
            timeOut = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
        this.setRetryPolicy(new DefaultRetryPolicy(
                timeOut,
                retryTime,
                //下一次连接时间 timeOut = timeout +（timeout * back_off_mult ）
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return this;
    }


    /**
     * 设置缓存时间
     *
     * @param cacheTime :单位 s
     */
    public final CustomRequest setCacheTime(long cacheTime) {
        this.mCacheTime = cacheTime;
        return this;
    }

    /**
     * 是否缓存
     *
     * @param flag
     * @return
     */
    public final CustomRequest isCache(boolean flag) {
        super.setShouldCache(flag);
        return this;
    }

    /**
     * 执行请求
     */
    public final void start() {
        VolleyRequest.getInstance().start(this);
    }

    /**
     * 将respose信息缓存如本地
     *
     * @param response:后端的resonse
     * @return
     */
    @Override
    protected final Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, CustomHttpHeaderParser.parseCacheHeaders(response, mCacheTime));
    }
}
