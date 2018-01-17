package hk.edu.cuhk.cse.tempusespatium;

import android.content.Context;
import android.support.v7.preference.DialogPreference;

public class LocalePreference extends DialogPreference {

    public LocalePreference(Context context) {
        super(context);
        setKey("LOCALE");
    }
}
