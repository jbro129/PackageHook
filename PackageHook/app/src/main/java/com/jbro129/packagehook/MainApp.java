package com.jbro129.packagehook;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MainApp extends Application implements InvocationHandler {
    private static final int FLAG_0 = 0x0;

    private Object base;

    @Override
    protected void attachBaseContext(Context base) {
        hook(base);
        super.attachBaseContext(base);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getPackageInfo".equals(method.getName())) {
            String pkgName = (String) args[0];
            Integer flag = (Integer) args[1];
            if ((flag & FLAG_0) != 0 && getPackageName().equals(pkgName))
            {
                PackageInfo info = (PackageInfo) method.invoke(base, args);

                // Set Info here

                info.packageName = "package.name.has.been.changed";
                info.versionName = "987654321.0";
                info.versionCode = 987654321;

                //

                return info;
            }
        }
        return method.invoke(base, args);
    }

    private void hook(Context context) {
        try
        {
            Class<?> pInfo = Class.forName("android.content.pm.PackageInfo");
            Object pInstance = pInfo.newInstance();

            Field versionName = pInfo.getField("versionName");
            versionName.setAccessible(true);

            Field versionCode = pInfo.getField("versionCode");
            versionCode.setAccessible(true);

            Field packageName = pInfo.getField("packageName");
            packageName.setAccessible(true);

            //Set info here as well

            versionName.set(pInstance, "987654321.0");
            versionCode.set(pInstance, 987654321);
            packageName.set(pInstance, "package.name.has.been.changed");

            //

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}