package com.jbro129.packagehook;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView packagaeName = (TextView) findViewById(R.id.packageName);
        TextView versionCode = (TextView) findViewById(R.id.versionCode);
        TextView versionName = (TextView) findViewById(R.id.versionName);

        PackageInfo pInfo = null;

        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0); // 'getPackageInfo' throws PackageManager.NameNotFoundException
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        /*
         * As set in 'build.gradle'
         *
         * applicationId is set to "com.jbro129.12345679.packagehook"
         * versionCode is set to 123456789
         * versionName is set to "123456789.0"
         *
         *
         */

        packagaeName.setText("Package Name: " + pInfo.packageName); // Set text to package name
        versionCode.setText("Version Code: " + pInfo.versionCode); // Set text to version code
        versionName.setText("Version Name: " + pInfo.versionName); // Set text to version name

    }
}
