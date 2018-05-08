package hk.edu.cuhk.cse.tempusespatium;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Alex Poon on 1/21/2018.
 */

public class DBAssetHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "geog.db";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_GEOG = "geog",
            COLUMN_COUNTRY = "country",
            COLUMN_SIMILAR_FLAG_1 = "similarFlag1",
            COLUMN_SIMILAR_FLAG_2 = "similarFlag2";

    static final String TABLE_HIGHSCORES = "highscores",
            COLUMN_PLAYER = "player",
            COLUMN_SCORE = "score";

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
