package com.jbro129.packagehook;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MainApp extends Application implements InvocationHandler{

    private Object base;
    private String pkg = "";

    @Override
    protected void attachBaseContext(Context base) {
        hook(base);
        super.attachBaseContext(base);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if ("getPackageInfo".equals(method.getName())) {
            String pkgName = (String) args[0];
            Integer flag = (Integer) args[1];
            if (pkg.equals(pkgName)) {
                PackageInfo info = (PackageInfo) method.invoke(base, args);

                // Set Info here

                info.versionName = "121.0"; //  <---
                info.versionCode = 121; //  <---
                info.packageName = "com.package.name"; //  <---
                
                //

                return info;
            }
        }
        return method.invoke(base, args);
    }

    private void hook(Context context) {
        try
        {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod =
                    activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);

            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            this.base = sPackageManager;
            this.pkg = context.getPackageName();

            Object proxy = Proxy.newProxyInstance(
                    iPackageManagerInterface.getClassLoader(),
                    new Class<?>[]{iPackageManagerInterface},
                    this);

            sPackageManagerField.set(currentActivityThread, proxy);

            Class<?> pInfo = Class.forName("android.content.pm.PackageInfo");
            Object pInstance = pInfo.newInstance();

            // Set info here as well

            Field versionName = pInfo.getField("versionName");
            versionName.setAccessible(true);

            Field versionCode = pInfo.getField("versionCode");
            versionCode.setAccessible(true);

            Field packageName = pInfo.getField("packageName");
            packageName.setAccessible(true);

            sPackageManagerField.set(currentActivityThread, proxy);

            versionName.set(pInstance, "121.0"); //  <---
            versionCode.set(pInstance, 121); //  <---
            packageName.set(pInstance, "com.package.name"); // <---

            //

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
