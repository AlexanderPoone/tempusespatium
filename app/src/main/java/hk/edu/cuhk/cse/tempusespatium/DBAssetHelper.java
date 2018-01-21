package hk.edu.cuhk.cse.tempusespatium;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Alex Poon on 1/21/2018.
 */

public class DBAssetHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "geog.db";
    private static final int DATABASE_VERSION = 1;

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
