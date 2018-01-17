package hk.edu.cuhk.cse.tempusespatium.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alex Poon on 1/17/2018.
 */

public class HighscoresOpenHelper extends SQLiteOpenHelper {

    public HighscoresOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        mDb.execSQL(CREATE_SQLITEDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        mDb.exec("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);
        onCreate(mDb);
    }
}
