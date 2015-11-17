package net.volley;

import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by zhangyanye on 2015/9/17.
 * Description:自定义的HeaderParser,跟默认的比，可以强制缓存，忽略服务器的设置，不过还是服务器的动态配置为优
 */
public class CustomHttpHeaderParser extends HttpHeaderParser {
    /**
     * Extracts a {@link com.android.volley.Cache.Entry} from a {@link com.android.volley.NetworkResponse}.
     *
     * @param response  The network response to parse headers from
     * @param cacheTime 缓存时间，如果设置了这个值，不管服务器返回是否可以缓存，都会缓存,一天为1000*60*60*24
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static Cache.Entry parseCacheHeaders(NetworkResponse response, long cacheTime) {
        long now = System.currentTimeMillis();
        long softExpire = now + cacheTime;
        Cache.Entry entry = parseCacheHeaders(response);
        if (null == entry && response.data.length > 0) {
            entry = new Cache.Entry();
            entry.data = response.data;
            entry.serverDate = now;
            // Time to Leave 客户端可以通过这个数据来判断当前内容是否过期。
            entry.ttl = softExpire;
            // 在这个时间内，是否需要刷新
            entry.softTtl = softExpire;
        }
        return entry;
    }
}

