package hk.edu.cuhk.cse.tempusespatium.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alex Poon on 1/17/2018.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "geog_en.db";
    private static final int DATABASE_VER = 1;
    public static final String TABLE_GEOG = "geog",
            COLUMN_COUNTRY_ID = "country_id",
            COLUMN_COUNTRY_NAME_EN = "country_name_en",
            COLUMN_AUDIO_URL = "audio_url",
            COLUMN_FLAG_URL = "flag_url";   // <-- Preliminary


    public static final String TABLE_HIGH_SCORES = "high_scores",
            COLUMN_GAMER = "gamer",
            COLUMN_SCORE = "score";

    private static final String CREATE_SQLITEDB = "CREATE TABLE IF NOT EXISTS " + TABLE_GEOG + " (" +
            COLUMN_COUNTRY_ID + " TEXT, " +
            COLUMN_COUNTRY_NAME_EN + " TEXT, " +
            COLUMN_AUDIO_URL + " TEXT, " +
            COLUMN_FLAG_URL + " TEXT );\n" +
            "CREATE TABLE IF NOT EXISTS " + TABLE_HIGH_SCORES + " (" +
            COLUMN_GAMER + " TEXT, " +
            COLUMN_SCORE + " INTEGER );";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SQLITEDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.exec("DROP TABLE IF EXISTS " + TABLE_GEOG + ", " + TABLE_HIGH_SCORES);
        onCreate(mDb);
    }
}
