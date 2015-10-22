package net.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import utils.CommonUtils;


/**
 * Created by zhangyanye on 2015/8/22
 * Description: 封装volleyListener
 */
public abstract class VolleyListener<T extends Object> {
    private static Response.Listener mListener;
    private static Response.ErrorListener mErrorListener;
    private static final String TAG = "VolleyListenner";
    private Type mType;
    private static final int TYPE_STRING = 0;
    private static final int TYPE_JSON = 1;
    private static final int TYPE_UNKONWN = -1;

    public VolleyListener() {
        setTarget();
    }

    public abstract void onComplete();

    public abstract void onSuccess(T result);

    public abstract void onError(VolleyError error);

    /**
     * 请求成功的回调事件
     *
     * @return
     */
    public final Response.Listener sucessListener() {
        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    switch (handleFactoty()) {
                        case TYPE_STRING: {
                            onSuccess((T) result);
                            break;
                        }
                        case TYPE_JSON: {
                            try {
                                onSuccess((T) CommonUtils.gson.fromJson(result, mType));
                            } catch (Exception e) {
                                handleException(e);
                            }
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unkonwn type");
                        }
                    }
                    onComplete();
                } catch (Exception e) {
                    handleException(e);
                }
            }
        };
        return mListener;
    }

    /**
     * 请求失败的回调事件
     *
     * @return
     */
    public final Response.ErrorListener errorListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Exception exception = error;
                handleException(exception);
                try {
                    onError(error);
                    onComplete();
                } catch (Exception e) {
                    handleException(e);
                }
            }
        };
        return mErrorListener;
    }

    /**
     * 获取泛型
     */
    private void setTarget() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof Class) {
            throw new RuntimeException("Missing type params");
        }
        if (type instanceof ParameterizedType) {
            try {
                Type temp = ((ParameterizedType) type).getActualTypeArguments()[0];
                if ((temp instanceof ParameterizedType)) { // 处理多级泛型
                    Class rawType = (Class) ((ParameterizedType) temp).getRawType();
                    if (List.class.equals(rawType) || ArrayList.class.equals(rawType)) {
                        mType = ((ParameterizedType) type).getActualTypeArguments()[0];
                    }
                } else {
                    mType = ((ParameterizedType) type).getActualTypeArguments()[0];
                }
            } catch (TypeNotPresentException e) {
                e.printStackTrace();
            } catch (MalformedParameterizedTypeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            return;
    }


    /**
     * 处理泛型类型
     *
     * @return
     */
    private int handleFactoty() {
        if (mType == String.class || mType == Object.class)
            return TYPE_STRING;
        else if (mType != null)
            return TYPE_JSON;
        else
            return TYPE_UNKONWN;
    }

    /**
     * VolleyError.java
     * Volley 中所有错误异常的父类，继承自 Exception，可通过此类设置和获取 NetworkResponse 或者请求的耗时。
     * AuthFailureError
     * 继承自 VolleyError，代表请求认证失败错误，如 RespondeCode 的 401 和 403 (需服务器配合)。
     * NetworkError
     * 继承自 VolleyError，代表网络错误。
     * ParseError（Request继承自StringRequest 使用不到）
     * 继承自 VolleyError，代表内容解析错误。
     * ServerError
     * 继承自 VolleyError，代表服务端错误 (最有可能的4xx或5xx HTTP状态代码)。
     * TimeoutError
     * 继承自 VolleyError，代表请求超时错误。
     * NoConnectionError
     * 继承自NetworkError，代表无法建立连接错误。
     *
     * @param exception
     */
    private void handleException(Exception exception) {
        String errorMessage;
        try {
            throw exception;
        } catch (NetworkError e) {
            errorMessage = "连接服务器失败，请检查当前网络";
        } catch (ServerError | TimeoutError e) {
            errorMessage = "网络连接超时，请稍后再试";
        } catch (AuthFailureError e) {
            errorMessage = "身份验证错误,请重新登录";
            //other logic
        } catch (VolleyError e) {
            errorMessage = "数据异常，我们会尽快修复";
        } catch (Exception e) {
            errorMessage = "请求错误，请稍后再试";
            Log.e(TAG, e.getMessage());
        }
        //可以添加服务器协议自定义错误
        CommonUtils.showToast(errorMessage);
    }
}