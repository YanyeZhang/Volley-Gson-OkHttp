package net.volley;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import utils.CommonUtils;
import utils.FileUtils;

/**
 * Created by zhangyanye on 2015/9/21.
 * Description:自定义缓存（Memory + Disk）
 */
public class CustomImageCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache = null;
    private static final long DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;
    private boolean isCache = true;

    public CustomImageCache() {
        int maxSize = (int) Runtime.getRuntime().maxMemory() / 8;
        mMemoryCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //这个方法一定要重写，不然缓存没有效果
                return value.getHeight() * value.getRowBytes();
            }
        };
        try {
            // 获取图片缓存路径
            File cacheDir = FileUtils.getDiskCacheDir("thumb");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            // 创建DiskLruCache实例，初始化缓存数据
            mDiskLruCache = DiskLruCache
                    .open(cacheDir, CommonUtils.getAppVersion(), 1, DISK_CACHE_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        if (mMemoryCache.get(url) != null) {
            bitmap = mMemoryCache.get(url);
        } else {
            String key = CommonUtils.encryptionWithMD5(url);
            try {
                if (mDiskLruCache.get(key) != null) {
                    CommonUtils.log("hehe");
                    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                    InputStream is = snapShot.getInputStream(0);
                    bitmap = BitmapFactory.decodeStream(is);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    public void putBitmap(final String url, final Bitmap bitmap) {
        if (isCache) {
            mMemoryCache.put(url, bitmap);
            setDiskCache(url, bitmap);
        }
    }

    /**
     * 设置是否缓存，仅对某一次请求有效
     *
     * @param flag
     */
    public void isCache(boolean flag) {
        this.isCache = flag;
    }

    /**
     * 清除缓存
     */
    public void clearMemory() {
        if (this.mMemoryCache != null)
            this.mMemoryCache.evictAll();
    }

    /**
     * 删除指定的缓存
     *
     * @param url
     */
    public void removeCache(String url) {
        try {
            this.mMemoryCache.remove(url);
            String key = CommonUtils.encryptionWithMD5(url);
            this.mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置磁盘缓存
     *
     * @param url
     * @param bitmap
     */
    private void setDiskCache(final String url, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String key = CommonUtils.encryptionWithMD5(url);
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        outputStream = baos;
                        if (outputStream != null) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                        mDiskLruCache.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}