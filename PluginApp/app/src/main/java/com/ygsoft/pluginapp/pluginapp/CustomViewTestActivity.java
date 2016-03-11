package com.ygsoft.pluginapp.pluginapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;

/**
 * Created by yuting2 on 2016/3/10.
 */
public class CustomViewTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = null;
        try {
            Class clazz = getClassLoader().loadClass(SearchView.class.getName());
            Constructor localConstructor = clazz.getConstructor(new Class[]{Context.class});
            view = (View) localConstructor.newInstance(mProxyActivity);

//            XmlResourceParser parser = getResources().getLayout(R.layout.activity_custom_view);
//            LayoutInflater layoutInflater = mProxyActivity.getLayoutInflater();
//            view = layoutInflater.inflate(parser, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(CustomViewTestActivity.class.getSimpleName(), "view" + view);

        if (view != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            setContentView(view, layoutParams);
        }
    }

}
