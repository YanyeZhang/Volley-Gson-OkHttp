package net.volley;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by zhangyanye on 2015/9/22.
 * Description: 加载图片
 */
public class VolleyImageLoader {

    private static VolleyImageLoader mVolleyImageLoader;
    private ImageLoader mImageLoader;
    private CustomImageCache mCache;
    private ImageLoader.ImageListener mListener;

    public final static VolleyImageLoader getInstance() {
        if (mVolleyImageLoader == null) {
            synchronized (VolleyImageLoader.class) {
                if (mVolleyImageLoader == null) {
                    mVolleyImageLoader = new VolleyImageLoader();
                }
            }
        }
        return mVolleyImageLoader;
    }

    private VolleyImageLoader() {
        //初始化图片内存缓存
        mCache = new CustomImageCache();
        //初始化ImageLoader
        mImageLoader = new ImageLoader(VolleyRequest.getInstance().getRequestQueues(), mCache);
    }


    public final void load(ImageView view, String url, @DrawableRes int defPic, @DrawableRes int failPic) {
        this.load(view, url, defPic, failPic, 0, 0);
    }

    /**
     * 带缓存加载图片
     *
     * @param view
     * @param url
     * @param defPic    默认加载图
     * @param failPic   失败加载图
     * @param maxWidth
     * @param maxHeight
     */
    public final void load(ImageView view, String url, @DrawableRes int defPic, @DrawableRes int failPic, int maxWidth, int maxHeight) {
        if (defPic == 0)
            defPic = android.R.drawable.ic_menu_gallery;
        if (failPic == 0)
            defPic = android.R.drawable.ic_menu_mapmode;
        mListener = ImageLoader.getImageListener(view, defPic, failPic);
        mImageLoader.get(url, mListener, maxWidth, maxHeight);
    }


    public final void loadWithoutCache(ImageView view, String url, @DrawableRes int defPic, @DrawableRes int failPic) {
        this.loadWithoutCache(view, url, defPic, failPic, 0, 0);
    }

    /**
     * 不带缓存加载图片
     * Notice:如果图片的返回的response有 "Cache-Control" 则交由volley自带的respose缓存
     *
     * @param view
     * @param url
     * @param defPic    默认加载图
     * @param failPic   失败加载图
     * @param maxWidth
     * @param maxHeight
     */
    public final void loadWithoutCache(ImageView view, String url, @DrawableRes int defPic, @DrawableRes int failPic, int maxWidth, int maxHeight) {
        if (defPic == 0)
            defPic = android.R.drawable.ic_menu_gallery;
        if (failPic == 0)
            failPic = android.R.drawable.ic_menu_mapmode;
        mListener = ImageLoader.getImageListener(view, defPic, failPic);
        mCache.isCache(false);
        ImageLoader imageLoader = new ImageLoader(VolleyRequest.getInstance().getRequestQueues(), mCache);
        imageLoader.get(url, mListener, maxWidth, maxHeight);
    }

    /**
     * 清除缓存
     */
    public final void clearMemoryCache() {
        mCache.clearMemory();
    }

    /**
     * 清除缓存
     *
     * @param url
     */
    public final void removeCache(String url) {
        mCache.removeCache(url);
    }
}
