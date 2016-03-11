package com.ygsoft.pluginapp.pluginapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import dalvik.system.DexClassLoader;

/**
 * 所有的Activity都继承这个，核心代码
 */
public class BaseActivity extends Activity {

    protected static final String TAG = BaseActivity.class.getSimpleName();

    public static final String FROM = "extra.from";

    public static final int FROM_EXTERNAL = 0;
    public static final int FROM_INTERNAL = 1;
    public static final String EXTRA_DEX_PATH = "extra.dex.path";
    public static final String EXTRA_CLASS = "extra.class";

    public static final String PROXY_VIEW_ACTION = "com.ryg.dynamicloadhost.VIEW";
    public static final String DEX_PATH = "/mnt/sdcard/plugin.apk";

    protected Activity mProxyActivity;
    protected AssetManager mAssetManager;
    protected Resources mResources;
    protected int mFrom = FROM_INTERNAL;

    protected DexClassLoader mClassLoader;

    public boolean isFromExternal() {
        return mFrom == FROM_EXTERNAL;
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    public void setResource(Resources resources) {
        mResources = resources;
        FullContext.getInstance().setmResources(resources);
    }

    public void setAssetManager(AssetManager assetManager) {
        mAssetManager = assetManager;
        FullContext.getInstance().setmAssetManager(assetManager);
    }

    public void setProxy(Activity proxyActivity) {
        Log.d(TAG, "setProxy: proxyActivity= " + proxyActivity);
        mProxyActivity = proxyActivity;
    }

    public void setClassLoader(DexClassLoader classLoader) {
        mClassLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader() {
        return mClassLoader != null ? mClassLoader : super.getClassLoader();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFrom = savedInstanceState.getInt(FROM, FROM_INTERNAL);
        }
        if (mFrom == FROM_INTERNAL) {
            super.onCreate(savedInstanceState);
            mProxyActivity = this;
        }
        Log.d(TAG, "onCreate: from= " + mFrom);
    }

    protected void startActivityByProxy(String className) {
        if (mProxyActivity == this) {
            Intent intent = new Intent();
            intent.setClassName(this, className);
            this.startActivity(intent);
        } else {
            Intent intent = new Intent(PROXY_VIEW_ACTION);
            intent.putExtra(EXTRA_DEX_PATH, DEX_PATH);
            intent.putExtra(EXTRA_CLASS, className);
            mProxyActivity.startActivity(intent);
        }
    }

    @Override
    public void setContentView(View view) {
        if (mProxyActivity == this) {
            super.setContentView(view);
        } else {
            mProxyActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mProxyActivity == this) {
            super.setContentView(view, params);
        } else {
            mProxyActivity.setContentView(view, params);
        }
    }

    @Deprecated
    @Override
    public void setContentView(int layoutResID) {
        if (mProxyActivity == this) {
            super.setContentView(layoutResID);
        } else {
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (mProxyActivity == this) {
            super.addContentView(view, params);
        } else {
            mProxyActivity.addContentView(view, params);
        }
    }

    @Override
    protected void onStart() {
        if (mProxyActivity == this) {
            super.onStart();
        }
        Log.d(TAG, TAG + "=====================onStart");
    }

    @Override
    protected void onRestart() {
        if (mProxyActivity == this) {
            super.onRestart();
        }
        Log.d(TAG, TAG + "=====================onRestart");
    }

    @Override
    protected void onResume() {
        if (mProxyActivity == this) {
            super.onResume();
        }
        Log.d(TAG, TAG + "=====================onResume");
    }

    @Override
    protected void onPause() {
        if (mProxyActivity == this) {
            super.onPause();
        }
        Log.d(TAG, TAG + "=====================onPause");
    }

    @Override
    protected void onStop() {
        if (mProxyActivity == this) {
            super.onStop();
        }
        Log.d(TAG, TAG + "=====================onStop");
    }

    @Override
    protected void onDestroy() {
        if (mProxyActivity == this) {
            super.onDestroy();
        }
        Log.d(TAG, TAG + "=====================onDestroy");
    }

}
