package hk.edu.cuhk.cse.tempusespatium;

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

import com.squareup.picasso.Picasso;

/**
 * Created by Alex Poon on 1/16/2018.
 */

public class PuzzleFlagsFragment extends Fragment implements PuzzleFragmentInterface {

    private boolean mFirst;
    private int mCorrectCountryIndex;
    private int mUserAnswerIndex = Integer.MAX_VALUE;

//    private String mCountry, mAnthem, mFlagUrl;

    private String[] mCountries, mFlagURLs;

    @Override
    public boolean isRevealed() {
        return isRevealed;
    }

    private boolean isRevealed = false;

    public PuzzleFlagsFragment() {

    }

    public PuzzleFlagsFragment(boolean first, int correctCountryIndex, String[] countries, String[] flagURLs) {
        mFirst = first;
        mCorrectCountryIndex = correctCountryIndex;
        mCountries = countries;
        mFlagURLs = flagURLs;
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
    public int[] revealAnswer(boolean isEarlier) {
        // TODO: Disable the other player.

        isRevealed = true;
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
        if (mCorrectCountryIndex == mUserAnswerIndex) {
            if (mFirst) {
                return new int[]{10, -5};
            } else {
                return new int[]{-5, -10};
            }
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
            if (mFirst) {
                return new int[]{-20, 0};
            } else {
                return new int[]{0, -20};
            }
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