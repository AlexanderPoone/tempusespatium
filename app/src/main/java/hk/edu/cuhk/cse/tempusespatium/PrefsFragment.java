package hk.edu.cuhk.cse.tempusespatium;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.text.Html;
import android.text.Spanned;

import static hk.edu.cuhk.cse.tempusespatium.Constants.DIFFICULTY_HARD;
import static hk.edu.cuhk.cse.tempusespatium.Constants.DIFFICULTY_INSANE;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_DIFFICULTY;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_NAME;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_THEME_PLAYER_1;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_THEME_PLAYER_2;

/**
 * Created by Alex Poon on 1/17/2018.
 */

public class PrefsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferenceScreen(createScreen());
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof LocalePreference) {
            DialogFragment dialogFragment;
            dialogFragment = PDFC.newInstance(preference.getKey());

            if (true) { // TODO: ??
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
            }

        } else super.onDisplayPreferenceDialog(preference);
    }

    private PreferenceScreen createScreen() {
        PreferenceScreen mainScreen = getPreferenceManager().createPreferenceScreen(getContext());

        Preference appVersion = new Preference(getContext());
        appVersion.setTitle(getString(R.string.pref_version));
        appVersion.setSummary("0.0.0.1 α");
        appVersion.setEnabled(false);

        LocalePreference localePreference = new LocalePreference(getContext());
        localePreference.setTitle(getString(R.string.pref_general_locale));

        Preference player1ThemePreference = new Preference(getContext());
        player1ThemePreference.setTitle(getString(R.string.pref_player_1_theme));
        String[] hues = getResources().getStringArray(R.array.colorSystem);
        final ColorArrayAdapter listAdapter = new ColorArrayAdapter(getContext(), android.R.layout.simple_list_item_1, hues);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dark);
        builder.setTitle(R.string.pref_player_1_theme)
                .setIcon(R.drawable.ic_palette_white_24dp)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int code = listAdapter.getCode(which);
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(SHAREDPREFS_THEME_PLAYER_1, code);
                        editor.apply();
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
        final AlertDialog realDialogPlayer1 = builder.create();

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dark);
        builder2.setTitle(R.string.pref_player_2_theme)
                .setIcon(R.drawable.ic_palette_white_24dp)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int code = listAdapter.getCode(which);
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(SHAREDPREFS_THEME_PLAYER_2, code);
                        editor.apply();
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
        final AlertDialog realDialogPlayer2 = builder2.create();


        player1ThemePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                realDialogPlayer1.show();
                return false;
            }
        });
        Preference player2ThemePreference = new Preference(getContext());
        player2ThemePreference.setTitle(getString(R.string.pref_player_2_theme));
        player2ThemePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                realDialogPlayer2.show();
                return false;
            }
        });

        PreferenceCategory general = new PreferenceCategory(getContext());
        general.setTitle(getString(R.string.pref_general));
        mainScreen.addPreference(general);
        general.addPreference(appVersion);
        general.addPreference(localePreference);
        general.addPreference(player1ThemePreference);
        general.addPreference(player2ThemePreference);


        ListPreference difficulty = new ListPreference(getContext());
        difficulty.setTitle(getString(R.string.pref_difficulty));
        int difficultyInt = getActivity().getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE).getInt(SHAREDPREFS_DIFFICULTY, DIFFICULTY_HARD);
        if (difficultyInt == DIFFICULTY_INSANE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                difficulty.setSummary(Html.fromHtml(getString(R.string.pref_difficulty_insane), Html.FROM_HTML_MODE_COMPACT));
            } else {
                difficulty.setSummary(Html.fromHtml(getString(R.string.pref_difficulty_insane)));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                difficulty.setSummary(Html.fromHtml(getString(R.string.pref_difficulty_hard), Html.FROM_HTML_MODE_COMPACT));
            } else {
                difficulty.setSummary(Html.fromHtml(getString(R.string.pref_difficulty_hard)));
            }        }
        difficulty.setKey("135");
//        difficulty.setDialogIcon(R.drawable.ic_view_squares_white_24dp);
//        difficulty.setDialogTitle(getString(R.string.pref_tab_c_portrait));
        difficulty.setValueIndex(2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            difficulty.setEntries(new Spanned[]{Html.fromHtml(getString(R.string.pref_difficulty_hard), Html.FROM_HTML_MODE_COMPACT), Html.fromHtml(getString(R.string.pref_difficulty_insane), Html.FROM_HTML_MODE_COMPACT)});
        } else {
            difficulty.setEntries(new Spanned[]{Html.fromHtml(getString(R.string.pref_difficulty_hard)), Html.fromHtml(getString(R.string.pref_difficulty_insane))});
        }
        difficulty.setEntryValues(new String[]{"0", "1"});
        difficulty.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SHAREDPREFS_DIFFICULTY, Integer.parseInt(newValue.toString()));
                editor.apply();
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                getActivity().finish();
                startActivity(intent);
                return false;
            }
        });
//        portrait.setPersistent(true)


        PreferenceCategory gameplay = new PreferenceCategory(getContext());
        gameplay.setTitle(getString(R.string.pref_gameplay));
        mainScreen.addPreference(gameplay);
        gameplay.addPreference(difficulty);

        return mainScreen;
    }
}
