package com.ygsoft.pluginapk.pluginapkapp;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;

/**
 * Created by yuting2 on 2016/3/11.
 */
public class CustomViewProxyActivity extends ResourceProxyActivity {

    private static final String TAG = CustomViewProxyActivity.class.getSimpleName();

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
            invokeSetClassLoaderMethod(localClass, instance, dexClassLoader);
            invokeSetManagerMethod(localClass, instance);
            invokeSetResourceMethod(localClass, instance);
            invokeOnCreateMethod(localClass, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void invokeSetClassLoaderMethod(Class localClass, Object instance, DexClassLoader dexClassLoader) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        invokeMethod(localClass, instance, "setClassLoader", new Class[] {DexClassLoader.class}, dexClassLoader);
    }


}
