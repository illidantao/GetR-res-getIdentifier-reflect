package com.dhunter.getr;

import android.app.Application;

/**
 * @author illidantao
 * @date 2016/5/8 20:28
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        com.dhunter.getr.r.R.init(this);
        //sometimes: not getPackageName() here is: "com.dhunter.appid"
        com.dhunter.getr.r.R.setRjavaPkgName("com.dhunter.getr");
    }
}
