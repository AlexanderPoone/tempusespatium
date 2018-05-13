package hk.edu.cuhk.cse.tempusespatium;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Alex Poon on 1/26/2018.
 */

public class HighscoresEnterNameDialog extends Dialog {
    private boolean mFirst;
    private int mScore;

    public HighscoresEnterNameDialog(@NonNull Context context) {
        super(context);
    }

    public HighscoresEnterNameDialog(@NonNull Context context, boolean first, int score) {
        super(context);
        mFirst = first;
        mScore = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.highscores_enter_name_dialog);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.add_name_layout);
        if (mFirst) {
            relativeLayout.setRotation(180);
        }
        AwesomeTextView rulesCaption = (AwesomeTextView) findViewById(R.id.highscores_caption);
        Typeface skylark_irc = ResourcesCompat.getFont(getContext(), R.font.skylark_itc_tt);
        BootstrapButton done = (BootstrapButton) findViewById(R.id.done_entering_text);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BootstrapEditText bootstrapEditText = (BootstrapEditText) findViewById(R.id.nameEdit);
                String name = bootstrapEditText.getText().toString();
                if (name.trim().length() > 0) {
                    SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(getContext());
                    SQLiteDatabase sqLiteDatabase = sqLiteAssetHelper.getReadableDatabase();
                    // TODO: Timestamp maybe?
                    sqLiteDatabase.execSQL("INSERT INTO " +
                            DBAssetHelper.TABLE_HIGHSCORES + " (" +
                            DBAssetHelper.COLUMN_PLAYER + ", " +
                            DBAssetHelper.COLUMN_SCORE +
                            ") VALUES ('" +
                            name + "', " +
                            mScore + ");");
//                    sqLiteDatabase.beginTransaction();
                    sqLiteDatabase.close();
                    sqLiteAssetHelper.close();
                    dismiss();
//                    cancel();
                    HighscoresDialog highscoresDialog = new HighscoresDialog(getContext());
                    highscoresDialog.show();
                }
            }
        });
    }

//
//    @Override
//    public void onClick(View view) {
//        dismiss();
//    }
}
