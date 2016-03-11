package com.ygsoft.pluginapk.pluginapkapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * 代理界面，资源加入
 */
public class ResourceProxyActivity extends ProxyActivity {

    private static final String TAG = ResourceProxyActivity.class.getSimpleName();

    protected AssetManager mAssetManager;
    protected Resources mResources;
    protected Resources.Theme mTheme;

    protected  void launchTargetActivity(String className) {
        Log.d(TAG, "start launchTargetActivity, className=" + className);
        loadResources();
        File dexOutputDir = getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath, dexOutputPath, null, localClassLoader);
        try {
            Class<?> localClass = dexClassLoader.loadClass(className);
            Object instance = invokeConstructor(localClass, new Class[] {}, new Object[] {});
            Log.d(TAG, "instance = " + instance);

            invokeProxyMethod(localClass, instance);
            invokeSetManagerMethod(localClass, instance);
            invokeSetResourceMethod(localClass, instance);
            invokeOnCreateMethod(localClass, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadResources() {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, mDexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    protected void invokeSetManagerMethod(Class localClass, Object instance) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        invokeMethod(localClass, instance, "setAssetManager", new Class[]{AssetManager.class}, mAssetManager);
    }

    protected void invokeSetResourceMethod(Class localClass, Object instance) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        invokeMethod(localClass, instance, "setResource", new Class[] {Resources.class}, mResources);
    }

}
