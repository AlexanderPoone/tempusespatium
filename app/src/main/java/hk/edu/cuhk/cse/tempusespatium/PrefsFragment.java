package hk.edu.cuhk.cse.tempusespatium;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import static hk.edu.cuhk.cse.tempusespatium.Constants.DIFFICULTY_HARD;
import static hk.edu.cuhk.cse.tempusespatium.Constants.DIFFICULTY_INSANE;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_DIFFICULTY;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_NAME;

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

        PreferenceCategory general = new PreferenceCategory(getContext());
        general.setTitle(getString(R.string.pref_general));
        mainScreen.addPreference(general);
        general.addPreference(appVersion);
        general.addPreference(localePreference);

        ListPreference difficulty = new ListPreference(getContext());
        difficulty.setTitle(getString(R.string.pref_difficulty));
        int difficultyInt = getActivity().getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE).getInt(SHAREDPREFS_DIFFICULTY, DIFFICULTY_HARD);
        if (difficultyInt == DIFFICULTY_INSANE) {
            difficulty.setSummary(getString(R.string.pref_difficulty_insane));
        } else {
            difficulty.setSummary(getString(R.string.pref_difficulty_hard));
        }
        difficulty.setKey("135");
//        difficulty.setDialogIcon(R.drawable.ic_view_squares_white_24dp);
//        difficulty.setDialogTitle(getString(R.string.pref_tab_c_portrait));
        difficulty.setValueIndex(2);
        difficulty.setEntries(new String[]{getString(R.string.pref_difficulty_hard), getString(R.string.pref_difficulty_insane)});
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
