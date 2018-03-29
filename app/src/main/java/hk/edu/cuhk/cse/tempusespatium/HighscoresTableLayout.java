package hk.edu.cuhk.cse.tempusespatium;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by softfeta on 3/28/18.
 */

public class HighscoresTableLayout extends TableLayout {

    @SuppressLint("SetTextI18n")
    public HighscoresTableLayout(Context context) {
        super(context);
        SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(context);
        SQLiteDatabase sqLiteDatabase = sqLiteAssetHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " +
                DBAssetHelper.COLUMN_PLAYER + ", " +
                DBAssetHelper.COLUMN_SCORE +
                " FROM " +
                DBAssetHelper.TABLE_HIGHSCORES, null);
        for (int i = 1; i <= cursor.getCount(); i++) {
            cursor.moveToNext();
            TableRow row = new TableRow(context);

            TextView rank = new TextView(context);
            rank.setText(Integer.toString(i));
            row.addView(rank);

            TextView player = new TextView(context);
            player.setText(cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_PLAYER)));
            row.addView(player);

            TextView score = new TextView(context);
            score.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DBAssetHelper.COLUMN_SCORE))));
            row.addView(score);

            addView(row);
        }
        cursor.close();
    }
}
