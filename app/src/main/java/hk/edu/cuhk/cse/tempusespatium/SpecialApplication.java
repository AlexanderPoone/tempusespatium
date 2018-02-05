package hk.edu.cuhk.cse.tempusespatium;

import android.app.Application;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.lang.reflect.Field;

public class SpecialApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
        Typeface baker_signet = ResourcesCompat.getFont(this, R.font.futuralt_book);
        try {
            final Field staticField = Typeface.class.getDeclaredField("DEFAULT");
            staticField.setAccessible(true);
            staticField.set(null, baker_signet);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
