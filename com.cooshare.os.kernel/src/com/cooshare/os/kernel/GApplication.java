package com.cooshare.os.kernel;

import android.app.Application;

public class GApplication extends Application {
private static GApplication instance;

public static GApplication getInstance() {
return instance;
}

@Override
public void onCreate() {
// TODO Auto-generated method stub
	
	
super.onCreate();
instance = this;
}


}



