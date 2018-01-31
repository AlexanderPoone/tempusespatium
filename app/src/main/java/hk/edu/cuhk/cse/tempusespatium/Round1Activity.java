package hk.edu.cuhk.cse.tempusespatium;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.Random;

/**
 * Created by Alex Poon on 10/17/2017.
 */

public class Round1Activity extends AppCompatActivity {

    int mScore, mScore2, mLastQuestionType;

    DonutProgress mDonutTime;
    DonutProgress mDonutTime2;
    BootstrapProgressBar mScoreBar;
    BootstrapProgressBar mScoreBar2;
    TextView mScoreText;
    TextView mScoreText2;
    TextView mScoreChangeText;
    TextView mScoreChangeText2;

    Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_exterior);

        mScore = mScore2 = 0;
        mDonutTime = (DonutProgress) findViewById(R.id.donutTime);
        mDonutTime2 = (DonutProgress) findViewById(R.id.donutTime2);
        mScoreBar = (BootstrapProgressBar) findViewById(R.id.progressBar);
        mScoreBar2 = (BootstrapProgressBar) findViewById(R.id.progressBar2);
        mScoreText = (TextView) findViewById(R.id.pointsText);
        mScoreText2 = (TextView) findViewById(R.id.pointsText2);
        mScoreChangeText = (TextView) findViewById(R.id.addDeduct);
        mScoreChangeText2 = (TextView) findViewById(R.id.addDeduct2);

        mScoreText2.setText(getResources().getString(R.string.bar_points, 10));

        randomPuzzle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideStatusBar();
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
        hideStatusBar();
    }

    private void hideStatusBar() {
        //Hide the status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void randomPuzzle() {
        Random random = new Random();
        int type;
        do {
            type = random.nextInt(5);           // Generate integer from 0 to 3.
        } while (type == mLastQuestionType);
        mLastQuestionType = type;


        switch (type) {
            case 0:
                generateAnagramPuzzle();
                break;
            case 1:
                generateFlagsPuzzle();
                break;
            case 2:
                generateMapPuzzle();
                break;
            case 3:
                generateDatePuzzle();
                break;
            case 4:
                generateBlanksPuzzle();
        }
    }

    public void generateAnagramPuzzle() {
        generateBlanksPuzzle();
//        // Create new fragment and transaction
//        PuzzleAnagramFragment anagramFragment0 = new PuzzleAnagramFragment(true);
//        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack
//        transaction0.replace(R.id.player1FragmentContainer, anagramFragment0, "player1");
////        transaction0.addToBackStack(null);
//        // Commit the transaction
//        int commit = transaction0.commit();
//
//        // Create new fragment and transaction
//        PuzzleAnagramFragment anagramFragment1 = new PuzzleAnagramFragment(false);
//        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack
//        transaction1.replace(R.id.player2FragmentContainer, anagramFragment1, "player2");
////        transaction1.addToBackStack(null);
//        // Commit the transaction
//        int commit1 = transaction1.commit();
//
//        countDown(anagramFragment0, anagramFragment1, 10000);
    }

    public void generateDatePuzzle() {
        Random random = new Random();
        SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(this);
        SQLiteDatabase sqLiteDatabase = sqLiteAssetHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " +
                DBAssetHelper.COLUMN_HISTORIC_EVENT + ", " +
                DBAssetHelper.COLUMN_YEAR + ", " +
                DBAssetHelper.COLUMN_MONTH + ", " +
                DBAssetHelper.COLUMN_PIC_URL + " " +
                "FROM hist " +
                "LIMIT 1 " +
                "OFFSET " + (random.nextInt(15) + 1), null);
        cursor.moveToNext();
        String historicEvent = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_HISTORIC_EVENT));
        int year = cursor.getInt(cursor.getColumnIndex(DBAssetHelper.COLUMN_YEAR));
        int month = cursor.getInt(cursor.getColumnIndex(DBAssetHelper.COLUMN_MONTH));
        String picUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_PIC_URL));

        PuzzleDateFragment dateFragment0 = new PuzzleDateFragment(true, historicEvent, year, month, picUrl);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        transaction0.replace(R.id.player1FragmentContainer, dateFragment0, "player1");
        int commit = transaction0.commit();

        PuzzleDateFragment dateFragment1 = new PuzzleDateFragment(false, historicEvent, year, month, picUrl);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.player2FragmentContainer, dateFragment1, "player2");
        int commit1 = transaction1.commit();

        cursor.close();
        sqLiteAssetHelper.close();
        sqLiteDatabase.close();

        countDown(dateFragment0, dateFragment1, 10000);
    }

    public void generateFlagsPuzzle() {
        String[] countries = new String[]{null, null, null, null};      //new String[4];
        String[] flagURLs = new String[]{null, null, null, null};      //new String[4];
        Random random = new Random();
        SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(this);
        SQLiteDatabase sqLiteDatabase = sqLiteAssetHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " +
                        DBAssetHelper.COLUMN_COUNTRY + ", " +
                        DBAssetHelper.COLUMN_ANTHEM + ", " +
                        DBAssetHelper.COLUMN_FLAG_URL + ", " +
                        DBAssetHelper.COLUMN_SIMILAR_FLAG_1 + ", " +
                        DBAssetHelper.COLUMN_SIMILAR_FLAG_2 + " " +
                        "FROM geog " +
                        "LIMIT 1 " +
                        "OFFSET " + (random.nextInt(195) + 0)
//                "ORDER BY " + (random.nextInt(195) + 1) + " " +
//                "LIMIT 1"
                , null);
        cursor.moveToNext();
        String similarFlag1, similarFlag2;
        String country = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_COUNTRY));
        String anthem = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_ANTHEM));
        String flagUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL));
        similarFlag1 = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_SIMILAR_FLAG_1));
        similarFlag2 = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_SIMILAR_FLAG_2));

        int correctCountryIndex = random.nextInt(4);                // 0=A, 1=B, 2=C, 3=D
        countries[correctCountryIndex] = country;
        flagURLs[correctCountryIndex] = flagUrl;

        cursor.close();


        String tmpUrl;
        cursor = sqLiteDatabase.rawQuery("SELECT " +
                DBAssetHelper.COLUMN_FLAG_URL + " " +
                "FROM geog " +
                "WHERE " + DBAssetHelper.COLUMN_COUNTRY + " = '" + similarFlag1 +
                "' OR " + DBAssetHelper.COLUMN_COUNTRY + " = '" + similarFlag2 +
                "'", null);
        cursor.moveToNext();
        tmpUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL));
        int tmpIndex;
        do {
            tmpIndex = random.nextInt(4);
        } while (flagURLs[tmpIndex] != null);
        flagURLs[tmpIndex] = tmpUrl;

        cursor.moveToNext();
        tmpUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL));
        do {
            tmpIndex = random.nextInt(4);
        } while (flagURLs[tmpIndex] != null);
        flagURLs[tmpIndex] = tmpUrl;
        cursor.close();


        cursor = sqLiteDatabase.rawQuery("SELECT " +
                DBAssetHelper.COLUMN_FLAG_URL + " " +
                "FROM geog " +
                "WHERE " + DBAssetHelper.COLUMN_COUNTRY +
                " <> '" + country + "' " +
                "LIMIT 1 " +
                "OFFSET " + (random.nextInt(194) + 0), null);
        cursor.moveToNext();
        tmpUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL));
        do {
            tmpIndex = random.nextInt(4);
        } while (flagURLs[tmpIndex] != null);
        flagURLs[tmpIndex] = tmpUrl;
        cursor.close();
        sqLiteAssetHelper.close();
        sqLiteDatabase.close();

        PuzzleFlagsFragment flagFragment0 = new PuzzleFlagsFragment(true, correctCountryIndex, countries, flagURLs);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        transaction0.replace(R.id.player1FragmentContainer, flagFragment0, "player1");
        int commit = transaction0.commit();

        PuzzleFlagsFragment flagFragment1 = new PuzzleFlagsFragment(false, correctCountryIndex, countries, flagURLs);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.player2FragmentContainer, flagFragment1, "player2");
        int commit1 = transaction1.commit();

        countDown(flagFragment0, flagFragment1, 5000);
    }

    public void generateMapPuzzle() {
        Random random = new Random();
        SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(this);
        SQLiteDatabase sqLiteDatabase = sqLiteAssetHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " +
                        DBAssetHelper.COLUMN_COUNTRY + ", " +
                        DBAssetHelper.COLUMN_ANTHEM + ", " +
                        DBAssetHelper.COLUMN_ANTHEM_URL + " " +
                        "FROM geog " +
                        "LIMIT 1 " +
                        "OFFSET " + random.nextInt(195)
                , null);
        cursor.moveToNext();
        String country = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_COUNTRY));
        String anthem = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_ANTHEM));
        String anthemUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_ANTHEM_URL));

        cursor.close();
        sqLiteAssetHelper.close();
        sqLiteDatabase.close();


        // Create new fragment and transaction
        PuzzleMapFragment mapFragment0 = new PuzzleMapFragment(true, country, anthem, anthemUrl);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction0.replace(R.id.player1FragmentContainer, mapFragment0, "player1");
//        transaction0.addToBackStack(null);
        // Commit the transaction
        int commit = transaction0.commit();

        // Create new fragment and transaction
        PuzzleMapFragment mapFragment1 = new PuzzleMapFragment(false, country, anthem, anthemUrl);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction1.replace(R.id.player2FragmentContainer, mapFragment1, "player2");
//        transaction1.addToBackStack(null);
        // Commit the transaction
        int commit1 = transaction1.commit();

        countDown(mapFragment0, mapFragment1, 12000);
    }

    public void generateBlanksPuzzle() {
        // TODO: !!!!!!!!!!!!!! https://en.wikipedia.org/wiki/Category:WikiProjects_by_topic !!!!!!!!!!!!!!!!!!!!!!!!

        PuzzleBlanksFragment blanksFragment0 = new PuzzleBlanksFragment(true, "https://en.wikipedia.org/wiki/Emu_War");
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        transaction0.replace(R.id.player1FragmentContainer, blanksFragment0, "player1");
        int commit = transaction0.commit();

        PuzzleBlanksFragment blanksFragment1 = new PuzzleBlanksFragment(false, "https://en.wikipedia.org/wiki/Emu_War");
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.player2FragmentContainer, blanksFragment1, "player2");
        int commit1 = transaction1.commit();

        countDown(blanksFragment0, blanksFragment1, 12000);
    }

    public void countDown(final PuzzleFragmentInterface f1, final PuzzleFragmentInterface f2, final int millis) {
        mDonutTime.setMax(millis / 1000);
        mDonutTime2.setMax(millis / 1000);
        new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long l) {
                String seconds = Integer.toString((int) l / 1000);
//                String percentage = Integer.toString((int) l / millis);
//                Log.e(seconds, percentage);
                mDonutTime.setDonut_progress(seconds);
                mDonutTime.setText(seconds);
                mDonutTime2.setDonut_progress(seconds);
                mDonutTime2.setText(seconds);
            }

            @Override
            public void onFinish() {
                mDonutTime.setDonut_progress("0");
                mDonutTime.setText("0");
                mDonutTime2.setDonut_progress("0");
                mDonutTime2.setText("0");

//                deductPoints(true, f1.revealAnswer()[0]);
//                deductPoints(false, f2.revealAnswer()[1]);
                //TODO: Remove placeholder.
                deductPoints(true, 0);
                deductPoints(false, 0);
                //TODO: Remove placeholder.


                /*
                TODO: Wait for 5 seconds then replace the fragment.
                 */
                if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
                mHandler = new Handler();
                final int delay = 5000; //5 seconds

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        randomPuzzle();
                    }
                }, delay);
            }
        }.start();
    }

    public void addPoints(boolean isFirst, int points) {
        if (isFirst) {
            mScore += points;
            mScoreBar.setProgress(mScore);
            mScoreText.setText(String.format("%d", mScore));
            mScoreChangeText.setText("+" + points);
            mScoreChangeText.setTextColor(getResources().getColor(R.color.ForestGreen, null));
        } else {
            mScore2 += points;
            mScoreBar2.setProgress(mScore2);
            mScoreText2.setText(String.format("%d", mScore2));
            mScoreChangeText2.setText("+" + points);
            mScoreChangeText2.setTextColor(getResources().getColor(R.color.ForestGreen, null));
        }
    }

    public void deductPoints(boolean isFirst, int points) {
        if (isFirst) {
            mScore -= points;
            if (mScore >= 0) {
                mScoreBar.setProgress(mScore);
            } else {
                mScoreBar.setProgress(0);
            }
            mScoreText.setText(String.format("%d", mScore));
            mScoreChangeText.setText("-" + points);
            mScoreChangeText.setTextColor(getResources().getColor(R.color.FireBrick, null));
        } else {
            mScore2 -= points;
            if (mScore >= 0) {
                mScoreBar2.setProgress(mScore2);
            } else {
                mScoreBar2.setProgress(0);
            }
            mScoreText2.setText(String.format("%d", mScore2));
            mScoreChangeText2.setText("-" + points);
            mScoreChangeText2.setTextColor(getResources().getColor(R.color.FireBrick, null));
        }
        callReveal(true);
    }

    public void callReveal(boolean isFirst) {
        PuzzleFragmentInterface player1 = ((PuzzleFragmentInterface) (getSupportFragmentManager().findFragmentByTag("player1")));
        PuzzleFragmentInterface player2 = ((PuzzleFragmentInterface) (getSupportFragmentManager().findFragmentByTag("player2")));
        player1.revealAnswer();
        player2.revealAnswer();
        if (isFirst) {
            player2.disableControls();
        } else {
            player1.disableControls();
        }
    }

    public void endRound() {
        Intent intent = new Intent(this, Round1Activity.class);
        finish();
        startActivity(intent);
    }
}