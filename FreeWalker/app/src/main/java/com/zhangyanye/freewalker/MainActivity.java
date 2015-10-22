package com.zhangyanye.freewalker;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends BaseActivity {

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        imageView = findView(R.id.iv_img);
       /* VolleyRequest.getInstance().RequestGet("https://www.csdn.net/", this, new VolleyListener<List<User>>() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onSuccess(List<User> result) {
                Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VolleyError volleyError) {
            }
        });*/

       /* String url = "http://ubmcmm.baidustatic.com/media/v1/0f0005yYIsGLBGDGfPud10.png";
        VolleyImageLoader.getInstance().load(imageView, url, 0, 0, 100, 100);*/
    }
}
