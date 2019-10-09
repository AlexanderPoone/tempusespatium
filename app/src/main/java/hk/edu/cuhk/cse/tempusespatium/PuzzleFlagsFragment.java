package hk.edu.cuhk.cse.tempusespatium;

import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static hk.edu.cuhk.cse.tempusespatium.Constants.getTextColorBasedOnBgColor;
//import com.squareup.picasso.Picasso;

/**
 * Created by Alex Poon on 1/16/2018.
 */

public class PuzzleFlagsFragment extends Fragment implements PuzzleFragmentInterface {

    private boolean mFirst;
    private int mCorrectCountryIndex, mColor;
    private int mUserAnswerIndex = Integer.MAX_VALUE;

//    private String mCountry, mAnthem, mFlagUrl;

    private String[] mCountries, mFlagURLs;
    private String mCapital, mCapitalPic;

    private RequestBuilder<PictureDrawable> requestBuilder;

    @Override
    public boolean isRevealed() {
        return isRevealed;
    }

    private boolean isRevealed = false;

    private ImageView flagA, flagB, flagC, flagD;

    private int mGridsReady = 0;
//private FrameLayout mFrame;
    public PuzzleFlagsFragment() {

    }

    private void loadIncrement() {
        mGridsReady++;
        if (mGridsReady == 4) {
//    mFrame.setVisibility(View.VISIBLE);
            flagA.setVisibility(View.VISIBLE);
            flagB.setVisibility(View.VISIBLE);
            flagC.setVisibility(View.VISIBLE);
            flagD.setVisibility(View.VISIBLE);
        }
    }

    public PuzzleFlagsFragment(boolean first, int correctCountryIndex, String[] countries, String[] flagURLs, String capital, String capitalPic, int color) {
        mFirst = first;
        mCorrectCountryIndex = correctCountryIndex;
        mCountries = countries;
        mFlagURLs = flagURLs;
        mCapital = capital;
        mCapitalPic = capitalPic;
        mColor = color;
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
        requestBuilder = Glide.with(this)
                .as(PictureDrawable.class)
                .transition(withCrossFade(1500))
                .listener(new SvgSoftwareLayerSetter());
        View view = inflater.inflate(R.layout.fragment_flags, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

        TextView cName = (TextView) view.findViewById(R.id.flag_country_name);
        if (mCapital != null) cName.setText(Html.fromHtml(mCapital));
        else cName.setText(mCountries[mCorrectCountryIndex]);
        cName.setTextColor(getTextColorBasedOnBgColor(getContext(), mColor));


         flagA = (ImageView) view.findViewById(R.id.aImageView);
        flagA.setVisibility(View.INVISIBLE);
        flagB = (ImageView) view.findViewById(R.id.bImageView);
        flagB.setVisibility(View.INVISIBLE);
        flagC = (ImageView) view.findViewById(R.id.cImageView);
        flagC.setVisibility(View.INVISIBLE);
        flagD = (ImageView) view.findViewById(R.id.dImageView);
        flagD.setVisibility(View.INVISIBLE);

//        mFrame = (FrameLayout) view.findViewById(R.id.flags_frame);
//        mFrame.setVisibility(View.INVISIBLE);

        /*mFlagURLs[0] = mFlagURLs[0].replaceAll("\u200B", "");
        mFlagURLs[1] = mFlagURLs[1].replaceAll("\u200B", "");
        mFlagURLs[2] = mFlagURLs[2].replaceAll("\u200B", "");
        mFlagURLs[3] = mFlagURLs[3].replaceAll("\u200B", "");*/

//        MultiTransformation multi = new MultiTransformation(
//                new PixelationFilterTransformation(25),
//                new SwirlFilterTransformation(25, 120, new PointF(50,40)));

        requestBuilder.load(mFlagURLs[0]).transition(withCrossFade(1500)).listener(new RequestListener<PictureDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<PictureDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(PictureDrawable resource, Object model, Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {
                loadIncrement();
                return false;
            }
        }).into(flagA);
        requestBuilder.load(mFlagURLs[1]).transition(withCrossFade(1500)).listener(new RequestListener<PictureDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<PictureDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(PictureDrawable resource, Object model, Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {
                loadIncrement();
                return false;
            }
        }).into(flagB);
        requestBuilder.load(mFlagURLs[2]).transition(withCrossFade(1500)).listener(new RequestListener<PictureDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<PictureDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(PictureDrawable resource, Object model, Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {
                loadIncrement();
                return false;
            }
        }).into(flagC);
        requestBuilder.load(mFlagURLs[3]).transition(withCrossFade(1500)).listener(new RequestListener<PictureDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<PictureDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(PictureDrawable resource, Object model, Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {
                loadIncrement();
                return false;
            }
        }).into(flagD);

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

        isRevealed = true;
        disableControls();

        int[] actual = {R.id.aActual, R.id.bActual, R.id.cActual, R.id.dActual};
        for (int i = 0; i < actual.length; i++) {
            TextView textView = (TextView) getView().findViewById(actual[i]);
            textView.setText(mCountries[i]);
        }

        ImageView imageView = (ImageView) getView().findViewById(R.id.watermark);
        Glide.with(getContext()).load(mCapitalPic).into(imageView);


//        switch (mCorrectCountryIndex) {
//            case 0:
//                RelativeLayout relA = (RelativeLayout) getView().findViewById(R.id.aRelLayout);
//                relA.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
//                ImageView indA = (ImageView) getView().findViewById(R.id.aIndicator);
//                indA.setImageResource(R.drawable.ic_check_white_24dp);
//                indA.setBackgroundColor(getResources().getColor(R.color.AndroidGreen, null));
//                break;
//            case 1:
//                RelativeLayout relB = (RelativeLayout) getView().findViewById(R.id.bRelLayout);
//                relB.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
//                ImageView indB = (ImageView) getView().findViewById(R.id.bIndicator);
//                indB.setImageResource(R.drawable.ic_check_white_24dp);
//                indB.setBackgroundColor(getResources().getColor(R.color.AndroidGreen, null));
//                break;
//            case 2:
//                RelativeLayout relC = (RelativeLayout) getView().findViewById(R.id.cRelLayout);
//                relC.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
//                ImageView indC = (ImageView) getView().findViewById(R.id.cIndicator);
//                indC.setImageResource(R.drawable.ic_check_white_24dp);
//                indC.setBackgroundColor(getResources().getColor(R.color.AndroidGreen, null));
//                break;
//            case 3:
//                RelativeLayout relD = (RelativeLayout) getView().findViewById(R.id.dRelLayout);
//                relD.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
//                ImageView indD = (ImageView) getView().findViewById(R.id.dIndicator);
//                indD.setImageResource(R.drawable.ic_check_white_24dp);
//                indD.setBackgroundColor(getResources().getColor(R.color.AndroidGreen, null));
//                break;
//        }
        if (mCorrectCountryIndex == mUserAnswerIndex) {
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
            if (mFirst) {
                return new int[]{10, -5};
            } else {
                return new int[]{-5, 10};
            }
        } else {
            switch (mCorrectCountryIndex) {
                case 0:
                    RelativeLayout relA = (RelativeLayout) getView().findViewById(R.id.aRelLayout);
                    relA.setBackground(getResources().getDrawable(R.drawable.rounded_choice_should_be, null));
                    ImageView indA = (ImageView) getView().findViewById(R.id.aIndicator);
                    indA.setImageResource(R.drawable.ic_reveal_white_24dp);
                    indA.setBackgroundColor(getResources().getColor(R.color.Amber, null));
                    break;
                case 1:
                    RelativeLayout relB = (RelativeLayout) getView().findViewById(R.id.bRelLayout);
                    relB.setBackground(getResources().getDrawable(R.drawable.rounded_choice_should_be, null));
                    ImageView indB = (ImageView) getView().findViewById(R.id.bIndicator);
                    indB.setImageResource(R.drawable.ic_reveal_white_24dp);
                    indB.setBackgroundColor(getResources().getColor(R.color.Amber, null));
                    break;
                case 2:
                    RelativeLayout relC = (RelativeLayout) getView().findViewById(R.id.cRelLayout);
                    relC.setBackground(getResources().getDrawable(R.drawable.rounded_choice_should_be, null));
                    ImageView indC = (ImageView) getView().findViewById(R.id.cIndicator);
                    indC.setImageResource(R.drawable.ic_reveal_white_24dp);
                    indC.setBackgroundColor(getResources().getColor(R.color.Amber, null));
                    break;
                case 3:
                    RelativeLayout relD = (RelativeLayout) getView().findViewById(R.id.dRelLayout);
                    relD.setBackground(getResources().getDrawable(R.drawable.rounded_choice_should_be, null));
                    ImageView indD = (ImageView) getView().findViewById(R.id.dIndicator);
                    indD.setImageResource(R.drawable.ic_reveal_white_24dp);
                    indD.setBackgroundColor(getResources().getColor(R.color.Amber, null));
                    break;
            }
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