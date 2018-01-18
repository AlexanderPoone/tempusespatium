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

import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_LOCALE_SET;
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

            if (true) {
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getFragmentManager(),"android.support.v7.preference.PreferenceFragment.DIALOG");
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

        ListPreference portrait = new ListPreference(getContext());
        portrait.setTitle(getString(R.string.pref_difficulty));
        portrait.setSummary(getString(R.string.pref_difficulty));
        portrait.setKey("135");
        portrait.setDialogIcon(R.drawable.ic_view_squares_white_24dp);
        portrait.setDialogTitle(getString(R.string.pref_tab_c_portrait));
        portrait.setValueIndex(2);
        portrait.setEntries(new String[] {"1", "2", "3", "4", "5", "6"});
        portrait.setEntryValues(new String[] {"1", "2", "3", "4", "5", "6"});
        portrait.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt(SHAREDPREFS_DIFFICULTY, Integer.parseInt(newValue.toString()));
                editor.putInt(SHAREDPREFS_LOCALE_SET, 1);
                editor.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
                return false;
            }
        });
//        portrait.setPersistent(true)

        PreferenceCategory tab_c = new PreferenceCategory(getContext());
        tab_c.setTitle(getString(R.string.pref_tab_c));
        mainScreen.addPreference(tab_c);
        tab_c.addPreference(portrait);
        tab_c.addPreference(landscape);

        return mainScreen;
    }
}
