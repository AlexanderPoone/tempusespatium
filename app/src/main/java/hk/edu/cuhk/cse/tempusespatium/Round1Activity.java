package hk.edu.cuhk.cse.tempusespatium;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static hk.edu.cuhk.cse.tempusespatium.StopWords.STOP_WORDS;

//import android.os.CountDownTimer;

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

    String mQuestionLang;
    HashMap<String, String> mArts;
    List<String> mCurrentTopic, mArtsSupportList;

    Handler mHandler;
    boolean mPauseTimer = false;
    CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_exterior);
        Intent intent = getIntent();
        mQuestionLang = intent.getStringExtra("lang");
        mCurrentTopic = intent.getStringArrayListExtra("topic");

        mArts = (HashMap<String, String>) intent.getSerializableExtra("arts");
        mArtsSupportList = intent.getStringArrayListExtra("supportList");
        Log.i("URL", mArts.get(mArtsSupportList.get(0)));

        BootstrapButton pauseButton = (BootstrapButton) findViewById(R.id.pauseGame);
        BootstrapButton pauseButton2 = (BootstrapButton) findViewById(R.id.pauseGame2);
        View.OnClickListener pauseClickedListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseGame();
            }
        };
        pauseButton.setOnClickListener(pauseClickedListener);
        pauseButton2.setOnClickListener(pauseClickedListener);

        mScore = mScore2 = 0;
        mDonutTime = (DonutProgress) findViewById(R.id.donutTime);
        mDonutTime2 = (DonutProgress) findViewById(R.id.donutTime2);
        mScoreBar = (BootstrapProgressBar) findViewById(R.id.progressBar);
        mScoreBar2 = (BootstrapProgressBar) findViewById(R.id.progressBar2);
        mScoreText = (TextView) findViewById(R.id.pointsText);
        mScoreText2 = (TextView) findViewById(R.id.pointsText2);
        mScoreChangeText = (TextView) findViewById(R.id.addDeduct);
        mScoreChangeText2 = (TextView) findViewById(R.id.addDeduct2);

        mScoreText.setText(getResources().getString(R.string.bar_points, 0));
        mScoreText2.setText(getResources().getString(R.string.bar_points, 0));

        randomPuzzle();
    }

    void pauseGame() {
        //TODO: SoundPool
        final PauseDialog pauseDialog = new PauseDialog(Round1Activity.this);
        pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.setCancelable(false);
        pauseDialog.show();
        BootstrapButton resume_button = (BootstrapButton) pauseDialog.findViewById(R.id.paused_resume);
        resume_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPauseTimer = false;
                pauseDialog.dismiss();
                mCountDownTimer.resume();
            }
        });
        BootstrapButton rules_button = (BootstrapButton) pauseDialog.findViewById(R.id.paused_rules);
        rules_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RulesDialog rulesDialog = new RulesDialog(Round1Activity.this);
                rulesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                rulesDialog.show();
            }
        });
        BootstrapButton main_menu_button = (BootstrapButton) pauseDialog.findViewById(R.id.paused_quit);
        main_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent(getBaseContext(), MenuActivity.class);
                startActivity(returnIntent);
                finish();
            }
        });
        mPauseTimer = true;
        mCountDownTimer.pause();
//        mHandler.removeCallbacks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
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
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN;
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
                generateRelevancePuzzle();
                break;
            default:
                generateRelevancePuzzle();
//            case 1:
//                generateFlagsPuzzle();
//                break;
//            case 2:
//                generateMapPuzzle();
//                break;
//            case 3:
//                generateDatePuzzle();
//                break;
//            case 4:
//                generateBlanksPuzzle();
//                break;
        }
        mScoreChangeText.setText("");
        mScoreChangeText2.setText("");
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

    String getArticleRelevancePuzzle(int i) {
        Random random = new Random();
        String selectedArt = mArtsSupportList.get(random.nextInt(mArtsSupportList.size() / 5)); //mArtsSupportList.length
        String selectedUrl = mArts.remove(selectedArt);
        mArtsSupportList.remove(selectedArt);

//        switch (i) {
//            case 0:
//                String selectedArt = mArtsSupportList.get(random.nextInt(mArtsSupportList.size() / 5)); //mArtsSupportList.length
//                String selectedUrl = mArts.remove(selectedArt);
//                mArtsSupportList.remove(selectedArt);
//                break;
//            case 1:
//                String selectedArtAlt1 = mArtsSupportListAlt1.get(random.nextInt(mArtsSupportListAlt1.size() / 5)); //mArtsSupportList.length
//                String selectedUrlAlt1 = mArtsAlt1.remove(selectedArtAlt1);
//                mArtsSupportListAlt1.remove(selectedArtAlt1);
//                break;
//            case 2:
//                String selectedArtAlt2 = mArtsSupportListAlt2.get(random.nextInt(mArtsSupportListAlt2.size() / 5)); //mArtsSupportList.length
//                String selectedUrlAlt2 = mArtsAlt2.remove(selectedArtAlt2);
//                mArtsSupportListAlt2.remove(selectedArtAlt2)
//                break;
//        }
        return selectedUrl;
    }

    public void generateRelevancePuzzle() {
        if (mArtsSupportList.size() == 0) {
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Integer>> bagOfWords = new ArrayList<>(3);

                for (int i = 0; i < 3; i++) {
                    TreeMap<String, Integer> tmp = new TreeMap<>();

                    OkHttpClient mClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(getArticleRelevancePuzzle(i))
                            .build();

                    Response response;
//                    StringTokenizer stringTokenizer = null;
                    Document doc = null;
                    XPathExpression staticXPath = null;


                    Pattern pattern = Pattern.compile("\\b\\p{L}{4,}\\b"); //{L} //(?=\S)\p{L}{4,} //\b\S{7,}\b
                    Matcher matcher = null;
                    NodeList test = null;

                    try {
                        response = mClient.newCall(request).execute();

                        doc = DocumentBuilderFactory.newInstance()
                                .newDocumentBuilder().parse(new InputSource(new StringReader(response.body().string())));
                        staticXPath = XPathFactory.newInstance()
                                .newXPath().compile("//*[@id=\"mw-content-text\"]/div/p");
                        test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);

//                        stringTokenizer = new StringTokenizer(response.body().string(), " \t\n\r\f,.:;?![]'");
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    }

//                    int iteration = 1;

                    for (int j = 0; j < test.getLength(); j++) {
                        matcher = pattern.matcher(test.item(j).getTextContent());
                        Log.d("Debug", test.item(j).getTextContent());

//                    while (stringTokenizer.hasMoreElements() && iteration < 200) {
//                        iteration++;
//                        String token = stringTokenizer.nextToken();
//                        Pattern pattern = Pattern.compile("\\p{L}{4,}");
//                        Matcher matcher = pattern.matcher(token);
//                        if (!matcher.matches()) {
//                            continue;
//                        }
                        while (matcher.find()) {
                            String stem = null;
                            switch (mQuestionLang) {
                                case "en":
                                    Log.d("word", matcher.group());
                                    EnglishStemmer englishStemmer = new EnglishStemmer();
                                    englishStemmer.setCurrent(matcher.group());
                                    if (englishStemmer.stem()) {
                                        stem = englishStemmer.getCurrent();
                                    }
                                    break;
                                case "fr":
                                    FrenchStemmer frenchStemmer = new FrenchStemmer();
                                    frenchStemmer.setCurrent(matcher.group());
                                    if (frenchStemmer.stem()) {
                                        stem = frenchStemmer.getCurrent();
                                    }
                                    break;
                                case "de":
                                    GermanStemmer germanStemmer = new GermanStemmer();
                                    germanStemmer.setCurrent(matcher.group());
                                    if (germanStemmer.stem()) {
                                        stem = germanStemmer.getCurrent();
                                    }
                                    break;
                            }
                            if (Arrays.asList(STOP_WORDS.get(mQuestionLang)).contains(stem)) {
                                continue;
                            }
                            if (tmp.containsKey(stem)) {
                                tmp.put(stem, tmp.get(stem) + 1);
                            } else {
                                tmp.put(stem, 1);
                            }
                        }
//                        if (iteration == test.getLength()) break;
//                        matcher = pattern.matcher(test.item(iteration).getTextContent());
                    }
                    sortByValue(tmp);
                    bagOfWords.add(sortByValue(tmp));
                    Log.d("debug", bagOfWords.get(0).toString());
                }

                final Map<String, List<String>> relevance = new HashMap<>(mCurrentTopic.size()); // {'cat1': ['', '', '']}, 'cat2': ['', ''], 'cat3', ['', '']}
                for (int i = 0; i < mCurrentTopic.size(); i++) {
                    List<String> tmp = new ArrayList<>();
                    Iterator<String> it = bagOfWords.get(i).keySet().iterator();
                    tmp.add(it.next());
                    tmp.add(it.next());
                    if (i == 0) tmp.add(it.next());
                    relevance.put(mCurrentTopic.get(i), tmp);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PuzzleRelevanceFragment relevanceFragment0 = new PuzzleRelevanceFragment(true, relevance);
                        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
                        transaction0.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                        transaction0.replace(R.id.player1FragmentContainer, relevanceFragment0, "player1");
                        int commit = transaction0.commit();

                        PuzzleRelevanceFragment relevanceFragment1 = new PuzzleRelevanceFragment(false, relevance);
                        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                        transaction1.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                        transaction1.replace(R.id.player2FragmentContainer, relevanceFragment1, "player2");
                        int commit1 = transaction1.commit();
                    }
                });
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                "OFFSET " + random.nextInt(16), null);
        cursor.moveToNext();
        String historicEvent = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_HISTORIC_EVENT));
        int year = cursor.getInt(cursor.getColumnIndex(DBAssetHelper.COLUMN_YEAR));
        int month = cursor.getInt(cursor.getColumnIndex(DBAssetHelper.COLUMN_MONTH));
        String picUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_PIC_URL));

        int randMinYear = year - (random.nextInt(50) + 50);
        int randMaxYear = year + (random.nextInt(50) + 50);

        PuzzleDateFragment dateFragment0 = new PuzzleDateFragment(true, historicEvent, year, month, picUrl, randMinYear, randMaxYear);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        transaction0.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction0.replace(R.id.player1FragmentContainer, dateFragment0, "player1");
        int commit = transaction0.commit();

        PuzzleDateFragment dateFragment1 = new PuzzleDateFragment(false, historicEvent, year, month, picUrl, randMinYear, randMaxYear);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
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
                        DBAssetHelper.COLUMN_SIMILAR_FLAG_2 + ", " +
                        DBAssetHelper.COLUMN_CAPITAL + ", " +
                        DBAssetHelper.COLUMN_CAPITAL_PIC + " " +
                        "FROM geog " +
                        "LIMIT 1 " +
                        "OFFSET " + (random.nextInt(196) + 0)
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
        String capital = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_CAPITAL));
        String capitalPic = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_CAPITAL_PIC));

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
                " <> '" + country.replace("'", "\\'") + "' " +
                "LIMIT 1 " +
                "OFFSET " + (random.nextInt(195) + 0), null);
        cursor.moveToNext();
        tmpUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL));
        do {
            tmpIndex = random.nextInt(4);
        } while (flagURLs[tmpIndex] != null);
        flagURLs[tmpIndex] = tmpUrl;
        cursor.close();
        sqLiteAssetHelper.close();
        sqLiteDatabase.close();

        int r = random.nextInt(9);
        if (r < 4) capital = null;                  // 3/10 chance easier

        PuzzleFlagsFragment flagFragment0 = new PuzzleFlagsFragment(true, correctCountryIndex, countries, flagURLs, capital, capitalPic);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        transaction0.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction0.replace(R.id.player1FragmentContainer, flagFragment0, "player1");
        int commit = transaction0.commit();

        PuzzleFlagsFragment flagFragment1 = new PuzzleFlagsFragment(false, correctCountryIndex, countries, flagURLs, capital, capitalPic);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction1.replace(R.id.player2FragmentContainer, flagFragment1, "player2");
        int commit1 = transaction1.commit();

        // TODO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            switchAnim();
        }

        countDown(flagFragment0, flagFragment1, 5000);
    }

    public void switchAnim() {
        FrameLayout frameLayout1 = (FrameLayout) findViewById(R.id.player1FragmentContainer);
        FrameLayout frameLayout2 = (FrameLayout) findViewById(R.id.player2FragmentContainer);
        final SpringAnimation springAnimation1 = new SpringAnimation(frameLayout1, DynamicAnimation.TRANSLATION_Y);
        final SpringAnimation springAnimation2 = new SpringAnimation(frameLayout2, DynamicAnimation.TRANSLATION_Y);
        springAnimation1.start();
        springAnimation2.start();
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
        transaction0.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction0.replace(R.id.player1FragmentContainer, mapFragment0, "player1");
//        transaction0.addToBackStack(null);
        // Commit the transaction
        int commit = transaction0.commit();

        // Create new fragment and transaction
        PuzzleMapFragment mapFragment1 = new PuzzleMapFragment(false, country, anthem, anthemUrl);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction1.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction1.replace(R.id.player2FragmentContainer, mapFragment1, "player2");
//        transaction1.addToBackStack(null);
        // Commit the transaction
        int commit1 = transaction1.commit();

        countDown(mapFragment0, mapFragment1, 12000);
    }

    public void generateBlanksPuzzle() {
        // TODO: !!!!!!!!!!!!!! https://en.wikipedia.org/wiki/Category:WikiProjects_by_topic !!!!!!!!!!!!!!!!!!!!!!!!
//        String wikipediaArt = "https://en.wikipedia.org/wiki/Emu_War";
//        wikipediaArt = "https://de.wikipedia.org/wiki/Eisenach";

        if (mArtsSupportList.size() == 0) {
            return;
        }
        Random random = new Random();
        String selectedArt = mArtsSupportList.get(random.nextInt(mArtsSupportList.size() / 5)); //mArtsSupportList.length
        String selectedUrl = mArts.remove(selectedArt);
        mArtsSupportList.remove(selectedArt);

        PuzzleBlanksFragment blanksFragment0 = new PuzzleBlanksFragment(true, selectedUrl);
        FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
        transaction0.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction0.replace(R.id.player1FragmentContainer, blanksFragment0, "player1");
        int commit = transaction0.commit();

        PuzzleBlanksFragment blanksFragment1 = new PuzzleBlanksFragment(false, selectedUrl);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction1.replace(R.id.player2FragmentContainer, blanksFragment1, "player2");
        int commit1 = transaction1.commit();

        countDown(blanksFragment0, blanksFragment1, 12000);
    }

    public void countDown(final PuzzleFragmentInterface f1, final PuzzleFragmentInterface f2, final int millis) {
        mDonutTime.setMax(millis / 1000);
        mDonutTime2.setMax(millis / 1000);
        mCountDownTimer = new CountDownTimer(millis, 1000) {
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

                //TODO: Remove placeholder.
                callReveal(null);
//                addOrDeductPoints(true, -20);
//                addOrDeductPoints(false, -20);
                //TODO: Remove placeholder.
            }
        }.start();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void addOrDeductPoints(boolean isFirst, int points) {
        if (isFirst) {
            mScore += points;
            if (mScore >= 0) {
                mScoreBar.setProgress(mScore);
            } else {
                mScoreBar.setProgress(0);
            }
            mScoreText.setText(getString(R.string.bar_points, mScore));
            if (points > 0) {
                mScoreChangeText.setText(String.format("+%d", points));
                mScoreChangeText.setTextColor(getResources().getColor(R.color.AndroidGreen, null));
            } else {
                mScoreChangeText.setText(Integer.toString(points));
                mScoreChangeText.setTextColor(getResources().getColor(R.color.FireBrick, null));
            }
        } else {
            mScore2 += points;
            if (mScore2 >= 0) {
                mScoreBar2.setProgress(mScore2);
            } else {
                mScoreBar2.setProgress(0);
            }
            mScoreText2.setText(getString(R.string.bar_points, mScore2));
            if (points > 0) {
                mScoreChangeText2.setText(String.format("+%d", points));
                mScoreChangeText2.setTextColor(getResources().getColor(R.color.AndroidGreen, null));
            } else {
                mScoreChangeText2.setText(Integer.toString(points));
                mScoreChangeText2.setTextColor(getResources().getColor(R.color.FireBrick, null));
            }
        }
    }

    public void callReveal(Boolean callerIsFirst) {
        mCountDownTimer.cancel();
        mDonutTime.setDonut_progress("0");
        mDonutTime.setText("0");
        mDonutTime2.setDonut_progress("0");
        mDonutTime2.setText("0");

        PuzzleFragmentInterface player1 = ((PuzzleFragmentInterface) (getSupportFragmentManager().findFragmentByTag("player1")));
        PuzzleFragmentInterface player2 = ((PuzzleFragmentInterface) (getSupportFragmentManager().findFragmentByTag("player2")));

        int change[];

//        if (callerIsFirst == null) {
//            change = player1.revealAnswer();
//            addOrDeductPoints(true, change[0]);
//            change = player2.revealAnswer();
//            addOrDeductPoints(false, change[1]);
//        } else {
        if (!player1.isRevealed()) {
            change = player1.revealAnswer();
            if (callerIsFirst == null) addOrDeductPoints(true, change[0]);
            else if (callerIsFirst) {
                addOrDeductPoints(true, change[0]);
                addOrDeductPoints(false, change[1]);
            }
        }
        if (!player2.isRevealed()) {
            change = player2.revealAnswer();
            if (callerIsFirst == null) addOrDeductPoints(false, change[1]);
            else if (!callerIsFirst) {
                addOrDeductPoints(true, change[0]);
                addOrDeductPoints(false, change[1]);
            }
        }
        /*
        TODO: Wait for 5 seconds then replace the fragment.
        */
        if (!mPauseTimer) { // TODO: La logique.
            if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
            mHandler = new Handler();
            final int delay = 5000; //5 seconds

            mHandler.postDelayed(new Runnable() {
                public void run() {
                    randomPuzzle();
                }
            }, delay);
        }
//        }
    }

    public void endRound() {
        Intent intent = new Intent(this, Round1Activity.class);
        finish();
        startActivity(intent);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}