package com.zhangyanye.freewalker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by zhangyanye on 2015/8/22
 * Description:所有android的基类
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected  <T extends View> T findView(int id) {

        return (T) findViewById(id);
    }
}

