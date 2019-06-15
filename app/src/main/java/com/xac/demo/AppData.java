package com.xac.demo;

import android.app.Application;

public class AppData extends Application {

    private static AppData app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static AppData getInstance() {
        return app;
    }

    public String serialNumber = null;
    public String pan = "";
    public String product = "unknown";


}
