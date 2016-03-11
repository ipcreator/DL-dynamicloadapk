package com.ygsoft.pluginapp.pluginapp;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * 对应序，测试资源加入
 */
public class ResourcesTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = null;

        try {
            XmlResourceParser parser = getResources().getLayout(R.layout.activity_resources);
            LayoutInflater layoutInflater = mProxyActivity.getLayoutInflater();
            view = layoutInflater.inflate(parser, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (view != null) {
            setContentView(view);
            ImageView iv = (ImageView) mProxyActivity.findViewById(R.id.iv);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.f_0));
        }
    }

}
