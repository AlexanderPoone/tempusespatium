package hk.edu.cuhk.cse.tempusespatium;

import android.content.Context;

/**
 * Created by Alex Poon on 1/17/2018.
 */

class Constants {
    //    static final int ACCEPTANCE_RADIUS_METRES = 50000;
    static final String SHAREDPREFS_NAME = "settings";
    static final String SHAREDPREFS_LOCALE = "LOCALE";
    static final String SHAREDPREFS_LOCALE_SET = "LOCALE_SET";
    static final String SHAREDPREFS_DIFFICULTY = "DIFFICULTY";
    static final String SHAREDPREFS_THEME_PLAYER_1 = "THEME_PLAYER_1";
    static final String SHAREDPREFS_THEME_PLAYER_2 = "THEME_PLAYER_2";
    static final int DIFFICULTY_HARD = 0;
    static final int DIFFICULTY_INSANE = 1;
    static final int ROUND_1_END_THRESHOLD = 360;
    static final int REQUEST_EXIT = 0;

    static int getTextColorBasedOnBgColor(Context context, int bgColor) {
        int a = bgColor >> 24;
        int r = (bgColor - (a << 24)) >> 16;
        int g = (bgColor - (a << 24) - (r << 16)) >> 8;
        int b = bgColor - (a << 24) - (r << 16) - (g << 8);
        if ((r * 0.299 + g * 0.587 + b * 0.114) < 128)
            return context.getResources().getColor(R.color.White, null);
        else return context.getResources().getColor(R.color.DarkSlateGray, null);
    }
}
