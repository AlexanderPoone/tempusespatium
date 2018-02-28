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
            COLUMN_ANTHEM = "anthem",
            COLUMN_ANTHEM_URL = "anthemUrl",
            COLUMN_FLAG_URL = "flagUrl",
            COLUMN_SIMILAR_FLAG_1 = "similarFlag1",
            COLUMN_SIMILAR_FLAG_2 = "similarFlag2",
            COLUMN_CAPITAL = "capital",
            COLUMN_CAPITAL_PIC = "capitalPic";

    static final String TABLE_HIST = "hist",
            COLUMN_HISTORIC_EVENT = "historicEvent",
            COLUMN_YEAR = "year",
            COLUMN_MONTH = "month",
            COLUMN_DAY = "day",
            COLUMN_LAT = "lat",
            COLUMN_LNG = "lng",
            COLUMN_PIC_URL = "picUrl";

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
