package com.ygsoft.pluginapp.pluginapp;

import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by yuting2 on 2016/3/10.
 */
public class FullContext {

    public static FullContext instance;

    public static void setInstance(FullContext instance) {
        FullContext.instance = instance;
    }

    public AssetManager getmAssetManager() {
        return mAssetManager;
    }

    public Resources getmResources() {
        return mResources;
    }

    public AssetManager mAssetManager;

    public void setmResources(Resources mResources) {
        this.mResources = mResources;
    }

    public void setmAssetManager(AssetManager mAssetManager) {
        this.mAssetManager = mAssetManager;
    }

    public Resources mResources;

    private FullContext() {

    }

    public static FullContext getInstance() {
        if (instance == null) {
            instance = new FullContext();
        }
        return instance;
    }


}
