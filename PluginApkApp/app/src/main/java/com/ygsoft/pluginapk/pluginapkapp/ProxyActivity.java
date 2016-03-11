package com.ygsoft.pluginapk.pluginapkapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * 代理界面
 */
public class ProxyActivity extends AppCompatActivity {

    private static final String TAG = ProxyActivity.class.getSimpleName();

    public static final String FROM = "extra.from";

    public static final int FROM_EXTERNAL = 0;
    public static final int FROM_INTERNAL = 1;

    public static final String EXTRA_DEX_PATH = "extra.dex.path";
    public static final String EXTRA_CLASS = "extra.class";

    private String mClass;
    protected String mDexPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDexPath = getIntent().getStringExtra(EXTRA_DEX_PATH);
        mClass = getIntent().getStringExtra(EXTRA_CLASS);

        if (mClass == null) {
            launchTargetActivity();
        } else {
            launchTargetActivity(mClass);
        }
    }

    protected  void launchTargetActivity() {
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(mDexPath, PackageManager.GET_ACTIVITIES);
        if (packageInfo.activities != null && (packageInfo.activities.length > 0)) {
            String activityName = packageInfo.activities[0].name;
            mClass = activityName;
            launchTargetActivity(mClass);
        }
    }

    protected  void launchTargetActivity(String className) {
        Log.d(TAG, "start launchTargetActivity, className=" + className);
        File dexOutputDir = getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath, dexOutputPath, null, localClassLoader);
        try {
            Class<?> localClass = dexClassLoader.loadClass(className);
            Object instance = invokeConstructor(localClass, new Class[] {}, new Object[] {});
            Log.d(TAG, "instance = " + instance);

            invokeProxyMethod(localClass, instance);
            invokeOnCreateMethod(localClass, instance);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Object invokeConstructor(Class localClass, Class[] param, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> localConstructor = localClass.getConstructor(param);
        return localConstructor.newInstance(args);
    }

    protected Object invokeMethod(Class localClass, Object instance, String methodName, Class[] param, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = localClass.getMethod(methodName, param);
        method.setAccessible(true);
        return method.invoke(instance, args);
    }

    protected void invokeProxyMethod(Class localClass, Object instance) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException  {
        invokeMethod(localClass, instance, "setProxy", new Class[]{Activity.class}, this);
    }

    protected void invokeOnCreateMethod(Class localClass, Object instance) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        Bundle bundle = new Bundle();
//        bundle.putInt(FROM, FROM_EXTERNAL);
//
//        invokeMethod(localClass, instance, "onCreate", new Class[] {Bundle.class}, new Object[] {bundle});

        Method onCreate = localClass.getDeclaredMethod("onCreate", new Class[] { Bundle.class });
        onCreate.setAccessible(true);
        Bundle bundle = new Bundle();
        bundle.putInt(FROM, FROM_EXTERNAL);
        onCreate.invoke(instance, new Object[] { bundle });
    }

}
