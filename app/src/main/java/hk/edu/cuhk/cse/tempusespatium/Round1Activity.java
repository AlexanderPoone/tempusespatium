package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.Random;

/**
 * Created by Alex Poon on 10/17/2017.
 */

public class Round1Activity extends AppCompatActivity {

    int mScore, mScore2, mLastQuestionType;

    DonutProgress mDonutTime;
    DonutProgress mDonutTime2;
    ProgressBar mScoreBar;
    ProgressBar mScoreBar2;
    TextView mScoreText;
    TextView mScoreText2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_exterior);

        mDonutTime = (DonutProgress) findViewById(R.id.donutTime);
        mDonutTime2 = (DonutProgress) findViewById(R.id.donutTime2);
        mScoreBar = (ProgressBar) findViewById(R.id.progressBar);
        mScoreBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        mScoreText = (TextView) findViewById(R.id.pointsText);
        mScoreText2 = (TextView) findViewById(R.id.pointsText2);

        mScoreText2.setText(getResources().getString(R.string.bar_points, 10));
    }

    public void randomPuzzle() {
        Random random = new Random();
        int type;
        do {
            type = random.nextInt(4);           // Generate integer from 0 to 3.
        } while (type == mLastQuestionType);
        mLastQuestionType = type;


        switch (type) {
            case 0:
                generateAnagramPuzzle();
                break;
            case 1:
                generateFlagsPuzzle(random.nextInt(4)); // 0=A, 1=B, 2=C, 3=D
                break;
            case 2:
                generateMapPuzzle();
                break;
            case 3:
                generateDatePuzzle();
                break;
        }
    }

    public void generateAnagramPuzzle() {
        // Create new fragment and transaction
        PuzzleAnagramFragment anagramFragment0 = new PuzzleAnagramFragment(true);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction0.add(R.id.player1FragmentContainer, anagramFragment0);
//        transaction0.addToBackStack(null);
        // Commit the transaction
        int commit = transaction0.commit();

        // Create new fragment and transaction
        PuzzleAnagramFragment anagramFragment1 = new PuzzleAnagramFragment(false);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction1.add(R.id.player2FragmentContainer, anagramFragment1);
//        transaction1.addToBackStack(null);
        // Commit the transaction
        int commit1 = transaction1.commit();

        countDown(anagramFragment0, anagramFragment1, 5000);
    }

    public void generateDatePuzzle() {
        // TODO: Database stuff

        PuzzleDateFragment dateFragment0 = new PuzzleDateFragment(true);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        transaction0.add(R.id.player1FragmentContainer, dateFragment0);
        int commit = transaction0.commit();

        PuzzleDateFragment dateFragment1 = new PuzzleDateFragment(false);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.add(R.id.player2FragmentContainer, dateFragment1);
        int commit1 = transaction1.commit();

        countDown(dateFragment0, dateFragment1, 5000);
    }

    public void generateFlagsPuzzle(int abcdInt) {
        // TODO: Database stuff
//        "SELECT COUNT(*) FROM FLAGS;"
//        "LIKE"

        PuzzleFlagsFragment flagFragment0 = new PuzzleFlagsFragment(true);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        transaction0.add(R.id.player1FragmentContainer, flagFragment0);
        int commit = transaction0.commit();

        PuzzleFlagsFragment flagFragment1 = new PuzzleFlagsFragment(false);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.add(R.id.player2FragmentContainer, flagFragment1);
        int commit1 = transaction1.commit();

        countDown(flagFragment0, flagFragment1, 5000);
    }

    public void generateMapPuzzle() {
        // Create new fragment and transaction
        PuzzleMapFragment mapFragment0 = new PuzzleMapFragment(true);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction0.add(R.id.player1FragmentContainer, mapFragment0);
//        transaction0.addToBackStack(null);
        // Commit the transaction
        int commit = transaction0.commit();

        // Create new fragment and transaction
        PuzzleMapFragment mapFragment1 = new PuzzleMapFragment(false);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction1.add(R.id.player2FragmentContainer, mapFragment1);
//        transaction1.addToBackStack(null);
        // Commit the transaction
        int commit1 = transaction1.commit();

        countDown(mapFragment0, mapFragment1, 10000);
    }

    public void countDown(final PuzzleFragmentInterface f1, final PuzzleFragmentInterface f2, final int millis) {
        mDonutTime.setMax(millis / 1000);
        mDonutTime2.setMax(millis / 1000);
        new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long l) {
                String seconds = Integer.toString((int) l / 1000);
                String percentage = Integer.toString((int) l / millis);
                mDonutTime.setDonut_progress(percentage);
                mDonutTime.setText(seconds);
                mDonutTime2.setDonut_progress(percentage);
                mDonutTime2.setText(seconds);
            }

            @Override
            public void onFinish() {
                // TODO: Reveal answer.
                int pointsChange;
                f1.revealAnswer();
                f2.revealAnswer();
            }
        }.start();
    }
}