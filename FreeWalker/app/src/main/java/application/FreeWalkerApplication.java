package application;

import android.app.Application;

/**
 * Created by zhangyanye on 2015/8/21
 * Description:数据缓存,通信,初始化等
 */
public class FreeWalkerApplication extends Application {


    private static FreeWalkerApplication mFreeWalkerApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mFreeWalkerApplication = this;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public static FreeWalkerApplication getApp() {
        return mFreeWalkerApplication;
    }
}
