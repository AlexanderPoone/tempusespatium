package hk.edu.cuhk.cse.tempusespatium;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class SpecialApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
