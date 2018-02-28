package hk.edu.cuhk.cse.tempusespatium;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alex Poon on 2/15/2018.
 */

public class TopicSearcherDropdownAdapter extends ArrayAdapter<String> {

    private Context mContext;

    public TopicSearcherDropdownAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String[] supTxt = mContext.getResources().getStringArray(R.array.locale_native);
        String[] subTxt = mContext.getResources().getStringArray(R.array.locale);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.topic_lang_inflated, parent, false);
        ImageView icon = (ImageView) row.findViewById(R.id.flagHolder);
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
            row.setBackgroundColor(mContext.getResources().getColor(R.color.CanonicalAubergine, null));
        } else {
            row.setBackgroundColor(mContext.getResources().getColor(R.color.UbuntuOrange, null));
        }
//        row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (position) {
//                    case 0:
//                        locale = "zh-HK";
//                        break;
//                    case 1:
//                        locale = "en-GB";
//                        break;
//                    case 2:
//                        locale = "ca";
//                        break;
//                    case 3:
//                        locale = "es-ES";
//                        break;
//                    case 4:
//                        locale = "de-DE";
//                        break;
//                    case 5:
//                        locale = "fr-FR";
//                        break;
//                    case 6:
//                        locale = "ja";
//                        break;
//                    case 7:
//                        locale = "uK";
//                        break;
//                    default:
//                        locale = "zh-HK";
//                        break;
//                }
//            }
//        });
        return row;
    }


}
