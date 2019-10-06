package hk.edu.cuhk.cse.tempusespatium;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by softfeta on 3/15/18.
 */

public class EndGameFragment extends Fragment {
    private boolean mFirst, mWon;
    private int mScore;

    public EndGameFragment() {

    }

    public EndGameFragment(boolean first, boolean won, int score) {
        mFirst = first;
        mWon = won;
        mScore = score;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_endgame, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.endgame_animation);
        if (mFirst && mWon)
            Glide.with(this).asGif().load(R.raw.player_1_wins).into(imageView);
        else if (!mFirst && mWon)
            Glide.with(this).asGif().load(R.raw.player_2_wins).into(imageView);
        else
            Glide.with(this).asGif().load("https://i.imgur.com/0mKXcg1.gif").into(imageView);
        if (mWon) {
            SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(getContext());
            SQLiteDatabase sqLiteDatabase = sqLiteAssetHelper.getReadableDatabase();
//            Cursor cursor = sqLiteDatabase.rawQuery("CREATE TABLE IF NOT EXISTS `highscores` (" +
//                    "`key` INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "`player` TEXT," +
//                    "`score` INTEGER" +
//                    ");", null);
//            sqLiteDatabase.beginTransaction();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " +
                    DBAssetHelper.TABLE_HIGHSCORES + " WHERE " + DBAssetHelper.COLUMN_SCORE +
                    " > " + mScore, null);
            cursor.moveToNext();
            int num = cursor.getInt(0);
            cursor.close();
            sqLiteDatabase.close();
            sqLiteAssetHelper.close();
            if (num < 10) {
                HighscoresEnterNameDialog highscoresEnterNameDialog = new HighscoresEnterNameDialog(getContext(), mFirst, mScore);
                highscoresEnterNameDialog.setCancelable(false);
                highscoresEnterNameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                highscoresEnterNameDialog.show();
            }
        }
        BootstrapButton btnBackToMenu = view.findViewById(R.id.endgame_return);
        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

//        LottieAnimationView lottieAnimationView= (LottieAnimationView) view.findViewById(R.id.endgame_animation);
//        lottieAnimationView.setAnimation(R.raw.);
    }
}
