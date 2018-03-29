package hk.edu.cuhk.cse.tempusespatium;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Alex Poon on 1/26/2018.
 */

public class HighscoresDialog extends Dialog implements View.OnClickListener {
    public HighscoresDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.highscores_dialog);
        AwesomeTextView rulesCaption = (AwesomeTextView) findViewById(R.id.highscores_caption);
        Typeface skylark_irc = ResourcesCompat.getFont(getContext(), R.font.skylark_itc_tt);
        rulesCaption.setTypeface(skylark_irc);

        final TableLayout tableLayout=(TableLayout) findViewById(R.id.tableLayout);
        SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(getContext());
        SQLiteDatabase sqLiteDatabase = sqLiteAssetHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " +
                DBAssetHelper.COLUMN_PLAYER + ", " +
                DBAssetHelper.COLUMN_SCORE +
                " FROM " +
                DBAssetHelper.TABLE_HIGHSCORES +
                " ORDER BY "+
                DBAssetHelper.COLUMN_SCORE +
                " DESC", null);
        Log.i("asdf", Integer.toString(cursor.getCount()));
        Typeface skylark_itc = ResourcesCompat.getFont(getContext(), R.font.skylark_itc_tt);

        for (int i = 1; i <= cursor.getCount(); i++) {
            cursor.moveToNext();
            TableRow row = new TableRow(getContext());
            row.setPadding(10, 10, 10, 10);

            TextView rank = new TextView(getContext());
            rank.setText(Integer.toString(i));
            rank.setPadding(0,0,10,0);
            rank.setTypeface(skylark_itc);
            rank.setTextSize(24f);
            row.addView(rank);


            TextView player = new TextView(getContext());
            player.setText(cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_PLAYER)));
            player.setPadding(0,0,10,0);
            player.setTypeface(skylark_itc);
            player.setTextSize(24f);
            row.addView(player);

            TextView score = new TextView(getContext());
            score.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            score.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DBAssetHelper.COLUMN_SCORE))));
            score.setTypeface(skylark_itc);
            score.setTextSize(24f);
            row.addView(score);

            tableLayout.addView(row);
        }
        cursor.close();
        sqLiteDatabase.close();
        sqLiteAssetHelper.close();
        Button close=(Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        BootstrapButton clearAll=(BootstrapButton) findViewById(R.id.clear_button);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(getContext());
                SQLiteDatabase sqLiteDatabase = sqLiteAssetHelper.getReadableDatabase();
                sqLiteDatabase.execSQL("DELETE FROM " +
                        DBAssetHelper.TABLE_HIGHSCORES);
                tableLayout.removeAllViews();
            }
        });
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
}
