package hk.edu.cuhk.cse.tempusespatium;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.squareup.picasso.Picasso;

import java.util.Random;

import okhttp3.OkHttpClient;

/**
 * Created by Alex Poon on 1/16/2018.
 */

public class PuzzleFlagsFragment extends Fragment implements PuzzleFragmentInterface {

    private boolean mFirst;
    private int mCorrectCountryIndex;
    private int mUserAnswerIndex;

    private String mCountry, mAnthem, mFlagUrl;

    private String[] mCountries, mFlagURLs;

    public PuzzleFlagsFragment() {

    }

    public PuzzleFlagsFragment(boolean first) {
        mFirst = first;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
// TODO: https://raw.githubusercontent.com/matej-pavla/Google-Maps-Examples/master/BoundariesExample/geojsons/world.countries.geo.json

//        Cursor cursor = sqLiteDatabase.query(DBAssetHelper.TABLE_GEOG,
//                new String[]{"country", "anthem", "flagUrl", "similarFlag1", "similarFlag2"},
//                "country <> 'Bahamas'",
//                null,
//                null,
//                null,
//                null);
//        if (cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                List<Object> smallerAL = new ArrayList<>();
//                // Notice that there are cursor.getFloat() and cursor.getDouble() methods.
//                smallerAL.add(cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_COUNTRY)));
//                smallerAL.add(cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_ANTHEM)));
//                smallerAL.add(cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL)));
//                smallerAL.add(cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_SIMILAR_FLAG_1)));
//                smallerAL.add(cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_SIMILAR_FLAG_2)));
//                largerAL.add(smallerAL);
//            }
//        }
        View view = inflater.inflate(R.layout.fragment_flags, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCountries = new String[]{null, null, null, null};      //new String[4];
        mFlagURLs = new String[]{null, null, null, null};      //new String[4];
        Random random = new Random();
        SQLiteAssetHelper sqLiteAssetHelper = new DBAssetHelper(getContext());
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
        mCountry = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_COUNTRY));
        mAnthem = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_ANTHEM));
        mFlagUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL));
        similarFlag1 = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_SIMILAR_FLAG_1));
        similarFlag2 = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_SIMILAR_FLAG_2));

        mCorrectCountryIndex = random.nextInt(4);                // 0=A, 1=B, 2=C, 3=D
        mCountries[mCorrectCountryIndex] = mCountry;
        mFlagURLs[mCorrectCountryIndex] = mFlagUrl;

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
        } while (mFlagURLs[tmpIndex] != null);
        mFlagURLs[tmpIndex] = tmpUrl;

        cursor.moveToNext();
        tmpUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL));
        do {
            tmpIndex = random.nextInt(4);
        } while (mFlagURLs[tmpIndex] != null);
        mFlagURLs[tmpIndex] = tmpUrl;
        cursor.close();


        cursor = sqLiteDatabase.rawQuery("SELECT " +
                DBAssetHelper.COLUMN_FLAG_URL + " " +
                "FROM geog " +
                "WHERE " + DBAssetHelper.COLUMN_COUNTRY +
                " <> '" + mCountry + "' " +
                "LIMIT 1 " +
                "OFFSET " + (random.nextInt(194) + 0), null);
        cursor.moveToNext();
        tmpUrl = cursor.getString(cursor.getColumnIndex(DBAssetHelper.COLUMN_FLAG_URL));
        do {
            tmpIndex = random.nextInt(4);
        } while (mFlagURLs[tmpIndex] != null);
        mFlagURLs[tmpIndex] = tmpUrl;
        cursor.close();
        sqLiteDatabase.close();
        sqLiteAssetHelper.close();

        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

        TextView cName = (TextView) view.findViewById(R.id.flag_country_name);
        cName.setText(mCountries[mCorrectCountryIndex]);

        ImageView flagA = (ImageView) view.findViewById(R.id.aImageView);
        ImageView flagB = (ImageView) view.findViewById(R.id.bImageView);
        ImageView flagC = (ImageView) view.findViewById(R.id.cImageView);
        ImageView flagD = (ImageView) view.findViewById(R.id.dImageView);

        mFlagURLs[0] = mFlagURLs[0].replaceAll("\u200B", "");
        mFlagURLs[1] = mFlagURLs[1].replaceAll("\u200B", "");
        mFlagURLs[2] = mFlagURLs[2].replaceAll("\u200B", "");
        mFlagURLs[3] = mFlagURLs[3].replaceAll("\u200B", "");

        Picasso.with(getContext()).load(mFlagURLs[0]).into(flagA);
        Picasso.with(getContext()).load(mFlagURLs[1]).into(flagB);
        Picasso.with(getContext()).load(mFlagURLs[2]).into(flagC);
        Picasso.with(getContext()).load(mFlagURLs[3]).into(flagD);

        RelativeLayout relA = (RelativeLayout) getView().findViewById(R.id.aRelLayout);
        relA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserAnswerIndex = 0;
                ((Round1Activity) getActivity()).callReveal(mFirst);
            }
        });
        RelativeLayout relB = (RelativeLayout) getView().findViewById(R.id.bRelLayout);
        relB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserAnswerIndex = 1;
                ((Round1Activity) getActivity()).callReveal(mFirst);
            }
        });
        RelativeLayout relC = (RelativeLayout) getView().findViewById(R.id.cRelLayout);
        relC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserAnswerIndex = 2;
                ((Round1Activity) getActivity()).callReveal(mFirst);
            }
        });
        RelativeLayout relD = (RelativeLayout) getView().findViewById(R.id.dRelLayout);
        relD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserAnswerIndex = 3;
                ((Round1Activity) getActivity()).callReveal(mFirst);
            }
        });

    }

    @Override
    public int[] revealAnswer() {
        // TODO: Disable the other player.

        disableControls();

        switch (mCorrectCountryIndex) {
            case 0:
                RelativeLayout relA = (RelativeLayout) getView().findViewById(R.id.aRelLayout);
                relA.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
                ImageView indA = (ImageView) getView().findViewById(R.id.aIndicator);
                indA.setImageResource(R.drawable.ic_check_white_24dp);
                indA.setBackgroundColor(getResources().getColor(R.color.AndroidGreen, null));
                break;
            case 1:
                RelativeLayout relB = (RelativeLayout) getView().findViewById(R.id.bRelLayout);
                relB.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
                ImageView indB = (ImageView) getView().findViewById(R.id.bIndicator);
                indB.setImageResource(R.drawable.ic_check_white_24dp);
                indB.setBackgroundColor(getResources().getColor(R.color.AndroidGreen, null));
                break;
            case 2:
                RelativeLayout relC = (RelativeLayout) getView().findViewById(R.id.cRelLayout);
                relC.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
                ImageView indC = (ImageView) getView().findViewById(R.id.cIndicator);
                indC.setImageResource(R.drawable.ic_check_white_24dp);
                indC.setBackgroundColor(getResources().getColor(R.color.AndroidGreen, null));
                break;
            case 3:
                RelativeLayout relD = (RelativeLayout) getView().findViewById(R.id.dRelLayout);
                relD.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
                ImageView indD = (ImageView) getView().findViewById(R.id.dIndicator);
                indD.setImageResource(R.drawable.ic_check_white_24dp);
                indD.setBackgroundColor(getResources().getColor(R.color.AndroidGreen, null));
                break;
        }
        if (mUserAnswerIndex == mCorrectCountryIndex) {
            return new int[]{10, -10};
        } else {
            switch (mUserAnswerIndex) {
                case 0:
                    RelativeLayout relA = (RelativeLayout) getView().findViewById(R.id.aRelLayout);
                    relA.setBackground(getResources().getDrawable(R.drawable.rounded_choice_incorrect, null));
                    ImageView indA = (ImageView) getView().findViewById(R.id.aIndicator);
                    indA.setImageResource(R.drawable.ic_close_white_24dp);
                    indA.setBackgroundColor(getResources().getColor(R.color.Fraternity, null));
                    break;
                case 1:
                    RelativeLayout relB = (RelativeLayout) getView().findViewById(R.id.bRelLayout);
                    relB.setBackground(getResources().getDrawable(R.drawable.rounded_choice_incorrect, null));
                    ImageView indB = (ImageView) getView().findViewById(R.id.bIndicator);
                    indB.setImageResource(R.drawable.ic_close_white_24dp);
                    indB.setBackgroundColor(getResources().getColor(R.color.Fraternity, null));
                    break;
                case 2:
                    RelativeLayout relC = (RelativeLayout) getView().findViewById(R.id.cRelLayout);
                    relC.setBackground(getResources().getDrawable(R.drawable.rounded_choice_incorrect, null));
                    ImageView indC = (ImageView) getView().findViewById(R.id.cIndicator);
                    indC.setImageResource(R.drawable.ic_close_white_24dp);
                    indC.setBackgroundColor(getResources().getColor(R.color.Fraternity, null));
                    break;
                case 3:
                    RelativeLayout relD = (RelativeLayout) getView().findViewById(R.id.dRelLayout);
                    relD.setBackground(getResources().getDrawable(R.drawable.rounded_choice_incorrect, null));
                    ImageView indD = (ImageView) getView().findViewById(R.id.dIndicator);
                    indD.setImageResource(R.drawable.ic_close_white_24dp);
                    indD.setBackgroundColor(getResources().getColor(R.color.Fraternity, null));
                    break;
            }
            return new int[]{10, -10};
        }
    }

    @Override
    public void disableControls() {
        RelativeLayout relA = (RelativeLayout) getView().findViewById(R.id.aRelLayout);
        relA.setOnClickListener(null);
        RelativeLayout relB = (RelativeLayout) getView().findViewById(R.id.bRelLayout);
        relB.setOnClickListener(null);
        RelativeLayout relC = (RelativeLayout) getView().findViewById(R.id.cRelLayout);
        relC.setOnClickListener(null);
        RelativeLayout relD = (RelativeLayout) getView().findViewById(R.id.dRelLayout);
        relD.setOnClickListener(null);
    }
}