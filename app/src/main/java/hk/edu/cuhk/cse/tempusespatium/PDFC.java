package hk.edu.cuhk.cse.tempusespatium;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_LOCALE;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_LOCALE_SET;
import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_NAME;


public class PDFC extends PreferenceDialogFragmentCompat {

    private SharedPreferences mSharedPref;

    public static PDFC newInstance(String key) {
        final PDFC fragment = new PDFC();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        String[] supTxt = getContext().getResources().getStringArray(R.array.locale_native);
        LocAdapter locAdapter = new LocAdapter(getContext(), 0, android.R.layout.simple_spinner_item, supTxt);
        builder.setAdapter(locAdapter, null);
        builder.setIcon(getContext().getResources().getDrawable(R.drawable.ic_language_white_24dp, null));
        builder.setTitle(getContext().getResources().getString(R.string.pref_general_locale)); //Title of the DIALOG, NOT Preference!
        builder.setPositiveButton(null, null);
//        builder.setNegativeButton(null, null);
        builder.setCancelable(false);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {

    }

    public class LocAdapter extends ArrayAdapter<String> {

        public LocAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String[] supTxt = getContext().getResources().getStringArray(R.array.locale_native);
            String[] subTxt = getContext().getResources().getStringArray(R.array.locale);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.pref_locale, parent, false);
            ImageView icon = (ImageView) row.findViewById(R.id.flagHolder);
            final Locale selectedLocale;
//            Typeface vt = Typeface.createFromAsset(activityAsContext.getAssets(), "fonts/VT323.ttf");
            TextView label = (TextView) row.findViewById(R.id.langTxt0);
            switch (position) {
                case 0:
                    icon.setImageResource(R.drawable.flag_hk);
                    break;
                case 1:
                    icon.setImageResource(R.drawable.flag_gb);
                    break;
                case 2:
                    icon.setImageResource(R.drawable.flag_cat);
                    break;
                case 3:
                    icon.setImageResource(R.drawable.flag_es);
                    break;
                case 4:
                    icon.setImageResource(R.drawable.flag_de);
                    break;
                case 5:
                    icon.setImageResource(R.drawable.flag_fr);
                    break;
                case 6:
                    icon.setImageResource(R.drawable.flag_jp);
                    break;
                case 7:
                    icon.setImageResource(R.drawable.flag_ua);
                    break;
            }
            label.setText(supTxt[position]);
            TextView subLabel = (TextView) row.findViewById(R.id.langTxt1);
            subLabel.setText(subTxt[position]);
            if (position % 2 == 0) {
                row.setBackgroundColor(getContext().getResources().getColor(R.color.CanonicalAubergine));
            } else {
                row.setBackgroundColor(getContext().getResources().getColor(R.color.UbuntuOrange));
            }
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSharedPref = getContext().getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE); //activityAsContext.MODE_PRIVATE?
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    String locale;
                    switch (position) {
                        case 0:
                            locale = "zh-HK";
                            break;
                        case 1:
                            locale = "en-GB";
                            break;
                        case 2:
                            locale = "ca";
                            break;
                        case 3:
                            locale = "es-ES";
                            break;
                        case 4:
                            locale = "de-DE";
                            break;
                        case 5:
                            locale = "fr-FR";
                            break;
                        case 6:
                            locale = "ja";
                            break;
                        case 7:
                            locale = "uk";
                            break;
                        default:
                            locale = "zh-HK";
                            break;
                    }
                    editor.putString(SHAREDPREFS_LOCALE, locale);
                    editor.putInt(SHAREDPREFS_LOCALE_SET, 1);
                    editor.apply();

                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    getActivity().setResult(Activity.RESULT_OK, null);
                    getActivity().finish();
                    startActivity(intent);
                }
            });
            return row;
        }
    }
}
