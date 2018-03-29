package hk.edu.cuhk.cse.tempusespatium;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

import java.io.IOException;
import java.util.Locale;

import static hk.edu.cuhk.cse.tempusespatium.Constants.REQUEST_EXIT;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_LOCALE;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_NAME;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MenuActivity extends AppCompatActivity {

    private SharedPreferences mSharedPref;
    private String mIso639_1;
    private static MediaPlayer mMediaPlayer;
    private static SoundPool mSoundPool;
    private SparseIntArray mPoolDict;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void setLocale(String locale) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Locale lc;
        if (locale.length() > 2) {
            lc = new Locale(locale.substring(0, 2), locale.substring(3, 5));
        } else {
            lc = new Locale(locale);
        }
        if (!configuration.locale.equals(lc)) {
            configuration.setLocale(lc);
            resources.updateConfiguration(configuration, null);
        }
    }

//    public enum Anthems {
//        CAMBODIA(R.string.anthem_kh, "https://upload.wikimedia.org/wikipedia/commons/a/af/United_States_Navy_Band_-_Nokoreach.ogg"),
//        CHILE(R.string.anthem_cl, "https://upload.wikimedia.org/wikipedia/commons/6/6e/United_States_Navy_Band_-_National_Anthem_of_Chile.ogg"),
//        FRANCE(R.string.anthem_fr, "https://upload.wikimedia.org/wikipedia/commons/3/30/La_Marseillaise.ogg"),
//        GERMANY(R.string.anthem_de, "https://upload.wikimedia.org/wikipedia/commons/a/a6/German_national_anthem_performed_by_the_US_Navy_Band.ogg"),
//        INDIA(R.string.anthem_in, "https://upload.wikimedia.org/wikipedia/commons/9/94/Jana_Gana_Mana_instrumental.ogg"),
//        ISRAEL(R.string.anthem_il, "https://upload.wikimedia.org/wikipedia/commons/2/26/Hatikvah_instrumental.ogg"),
//        JAPAN(R.string.anthem_jp, "https://upload.wikimedia.org/wikipedia/commons/a/a3/Kimi_ga_Yo_instrumental.ogg"),
//        SPAIN(R.string.anthem_es, "https://upload.wikimedia.org/wikipedia/commons/c/c8/Marcha_Real-Royal_March_by_US_Navy_Band.ogg"),
//        PORTUGAL(R.string.anthem_pt, "https://upload.wikimedia.org/wikipedia/commons/5/58/A_Portuguesa.ogg"),
//        UKRAINE(R.string.anthem_ua, "https://upload.wikimedia.org/wikipedia/commons/6/6d/National_anthem_of_Ukraine%2C_instrumental.oga");
//
//        private final int name;
//        final String url;
//
//        Anthems(int name, String url) {
//            this.name = name;
//            this.url = url;
//        }
//    }

    private void playSong() {
        if (mMediaPlayer == null)
        {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            try {
                mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA).build());
                mMediaPlayer.setDataSource("https://ia801407.us.archive.org/5/items/Wikipedia_201411/Wikipedia.ogg");
//                mMediaPlayer.setDataSource(Anthems.valueOf("Spain".toUpperCase()).url);
                mMediaPlayer.setLooping(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.prepareAsync();
        }
//        else {
//            mMediaPlayer.start();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Typeface baker_signet = ResourcesCompat.getFont(this, R.font.baker_signet_bt);
        Typeface ermis_pro = ResourcesCompat.getFont(this, R.font.ermis_pro_bold);


        mSharedPref = getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE);
        mIso639_1 = mSharedPref.getString(SHAREDPREFS_LOCALE, "zh-HK");
        setLocale(mIso639_1);

        setContentView(R.layout.activity_menu);

        playSong();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();
        mPoolDict = new SparseIntArray();
        mPoolDict.put(0, mSoundPool.load(this, R.raw.space_swoosh, 1));
        mPoolDict.put(1, mSoundPool.load(this, R.raw.beep_space_button, 1));
        mPoolDict.put(2, mSoundPool.load(this, R.raw.plunger_pop, 1));
        final BootstrapButton playButton = (BootstrapButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSoundPool.play(mPoolDict.get(0), .5f, .5f, 1, 0, 1.f);
                Intent jump = new Intent(getBaseContext(), TopicSearcherActivity.class);
                startActivity(jump);
                finish();
            }
        });

        final BootstrapButton rulesButton = (BootstrapButton) findViewById(R.id.rulesButton);
        // TODO: May as well be a blue circle at the top right corner.
        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSoundPool.play(mPoolDict.get(1), .5f, .5f, 1, 0, 1.f);
                RulesDialog rulesDialog = new RulesDialog(MenuActivity.this);
                rulesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                rulesDialog.show();
                //https://stackoverflow.com/questions/23672335/how-to-make-this-beautiful-dialog
                //https://stackoverflow.com/questions/41015691/custom-dialog-like-view
            }
        });

        final BootstrapButton highscoresButton = (BootstrapButton) findViewById(R.id.highscoresButton);
        highscoresButton.setBootstrapBrand(new Rattan());
        highscoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSoundPool.play(mPoolDict.get(1), .5f, .5f, 1, 0, 1.f);
                HighscoresDialog highscoresDialog = new HighscoresDialog(MenuActivity.this);
                highscoresDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                highscoresDialog.show();

            }
        });

        final BootstrapButton settingsButton = (BootstrapButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSoundPool.play(mPoolDict.get(1), .5f, .5f, 1, 0, 1.f);
                Intent refresh = new Intent(getBaseContext(), PrefsActivity.class);
                startActivityForResult(refresh, REQUEST_EXIT);
//                finish();
            }
        });

        final BootstrapButton quitButton = (BootstrapButton) findViewById(R.id.quitButton);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Really quit?")
                .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSoundPool.play(mPoolDict.get(2), .5f, .5f, 1, 0, 1.f);
                alertDialog.show();
            }
        });
        if (mIso639_1.equals("uk")) {
            TextView fullScreenContent=(TextView) findViewById(R.id.fullscreen_content);
            fullScreenContent.setTypeface(ermis_pro, Typeface.BOLD);
            fullScreenContent.setTextSize(39f);
            playButton.setTypeface(ermis_pro, Typeface.BOLD_ITALIC);
            rulesButton.setTypeface(ermis_pro, Typeface.BOLD);
            settingsButton.setTypeface(ermis_pro, Typeface.BOLD);
            highscoresButton.setTypeface(ermis_pro, Typeface.BOLD);
            quitButton.setTypeface(ermis_pro, Typeface.BOLD);
        } else {
            playButton.setTypeface(baker_signet, Typeface.BOLD_ITALIC);
            rulesButton.setTypeface(baker_signet, Typeface.BOLD);
            settingsButton.setTypeface(baker_signet, Typeface.BOLD);
            highscoresButton.setTypeface(baker_signet, Typeface.BOLD);
            quitButton.setTypeface(baker_signet, Typeface.BOLD);
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        /* Perpetual background animation */
        final ImageView bg0 = (ImageView) findViewById(R.id.bg0);
        final ImageView bg1 = (ImageView) findViewById(R.id.bg1);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(9000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = bg0.getWidth();
                final float translationX = width * progress;
                bg0.setTranslationX(translationX);
                bg1.setTranslationX(translationX - width);
            }
        });
        animator.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_EXIT) && (resultCode == RESULT_OK)) {
            this.finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
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

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        } catch (IllegalStateException e) {
        }
    }

    private static class Rattan implements BootstrapBrand {

        @Override
        public int defaultFill(Context context) {
            return context.getResources().getColor(R.color.Gamboge, null);
        }

        @Override
        public int defaultEdge(Context context) {
            return 0;
        }

        @Override
        public int defaultTextColor(Context context) {
            return context.getResources().getColor(android.R.color.white, null);
        }

        @Override
        public int activeFill(Context context) {
            return context.getResources().getColor(R.color.OrangeRed, null);
        }

        @Override
        public int activeEdge(Context context) {
            return 0;
        }

        @Override
        public int activeTextColor(Context context) {
            return context.getResources().getColor(android.R.color.white, null);
        }

        @Override
        public int disabledFill(Context context) {
            return 0;
        }

        @Override
        public int disabledEdge(Context context) {
            return 0;
        }

        @Override
        public int disabledTextColor(Context context) {
            return 0;
        }

        @Override
        public int getColor() {
            return 0;
        }
    }
}
