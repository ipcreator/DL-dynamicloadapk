package com.ygsoft.pluginapk.pluginapkapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * 代理界面，生命周期加入测试
 */
public class LifecycleProxyActivity extends ProxyActivity {

    private static final String TAG = LifecycleProxyActivity.class.getSimpleName();
    protected Map<String, Method> mActivityLifeCircleMethods = new HashMap<>();
    protected Activity mRemoteActivity = null;

    protected void instantiateLifeCircleMethods(Class<?> localClass) {
        String[] methodNames = new String[] {"onRestart", "onStart", "onResume", "onPause", "onStop", "onDestroy"};
        for (String methodName: methodNames) {
            Method method = null;
            try {
                method = localClass.getDeclaredMethod(methodName, new Class[] {});
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            mActivityLifeCircleMethods.put(methodName, method);
        }

        Method onCreate = null;
        try {
            onCreate = localClass.getDeclaredMethod("onCreate", new Class[] {Bundle.class});
            onCreate.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        mActivityLifeCircleMethods.put("onCreate", onCreate);

        Method onActivityResult = null;
        try {
            onActivityResult = localClass.getDeclaredMethod("onActivityResult", new Class[] {int.class, int.class, Intent.class});
            onActivityResult.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        mActivityLifeCircleMethods.put("onActivityResult", onActivityResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Method onResume = mActivityLifeCircleMethods.get("onResume");
        if (onResume != null) {
            try {
                if (mRemoteActivity != null) {
                    onResume.invoke(mRemoteActivity, new Object[] {});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        Method onPause = mActivityLifeCircleMethods.get("onPause");
        if (onPause != null) {
            try {
                if (mRemoteActivity != null) {
                    onPause.invoke(mRemoteActivity, new Object[] {});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    protected  void launchTargetActivity(String className) {
        Log.d(TAG, "start launchTargetActivity, className=" + className);
        File dexOutputDir = getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath, dexOutputPath, null, localClassLoader);
        try {
            Class<?> localClass = dexClassLoader.loadClass(className);

            instantiateLifeCircleMethods(localClass);
            mRemoteActivity = (Activity) localClass.newInstance();

            Constructor<?> localConstructor = localClass.getConstructor(new Class[]{});
            Object instance = localConstructor.newInstance(new Object[]{});
            Log.d(TAG, "instance = " + instance);

            Method setProxy = localClass.getMethod("setProxy", new Class[] { Activity.class });
            setProxy.setAccessible(true);
            setProxy.invoke(instance, new Object[]{this});

            Method onCreate = localClass.getDeclaredMethod("onCreate", new Class[] { Bundle.class });
            onCreate.setAccessible(true);
            Bundle bundle = new Bundle();
            bundle.putInt(FROM, FROM_EXTERNAL);
            onCreate.invoke(instance, new Object[] { bundle });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
