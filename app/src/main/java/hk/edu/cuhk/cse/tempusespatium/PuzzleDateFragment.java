package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Picasso;

/**
 * Created by Alex Poon on 1/16/2018.
 */

public class PuzzleDateFragment extends Fragment implements PuzzleFragmentInterface {

    private boolean mFirst;
    private NumberPicker mYearPicker, mMonthPicker;
    private String mHistoricEvent, mPicUrl;
    private int mYear, mMonth, mRandMinYear, mRandMaxYear;

    private boolean isRevealed = false;

    public boolean isRevealed() {
        return isRevealed;
    }

    public PuzzleDateFragment() {
    }

    public PuzzleDateFragment(boolean first, String historicEvent, int year, int month, String picUrl, int randMinYear, int randMaxYear) {
        mFirst = first;
        mHistoricEvent = historicEvent;
        mYear = year;
        mMonth = month;
        mPicUrl = picUrl;
        mRandMinYear = randMinYear;
        mRandMaxYear = randMaxYear;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.questionHistory);
        textView.setText(getString(R.string.question_history, mHistoricEvent));
        ImageView imageView = (ImageView) view.findViewById(R.id.questionHistoryPic);
        Picasso.with(getContext()).load(mPicUrl).into(imageView);

        mYearPicker = (NumberPicker) view.findViewById(R.id.year);
        mYearPicker.setMinValue(mRandMinYear);
        mYearPicker.setMaxValue(mRandMaxYear);
        mYearPicker.setValue(mRandMinYear);

        mMonthPicker = (NumberPicker) view.findViewById(R.id.month);

        BootstrapButton submitButton = (BootstrapButton) view.findViewById(R.id.hist_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Round1Activity) getActivity()).callReveal(mFirst);
            }
        });

        BootstrapButton clearButton = (BootstrapButton) view.findViewById(R.id.hist_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYearPicker.setValue(mRandMinYear);
                mMonthPicker.setValue(1);
            }
        });
    }

    @Override
    public int[] revealAnswer() {
        // TODO: Disable the other player.

        isRevealed = true;
        int playerYear = mYearPicker.getValue();
        int playerMonth = mMonthPicker.getValue();
        mYearPicker.setBackgroundColor(getResources().getColor(R.color.FireBrick, null));
        mYearPicker.setValue(mYear);
        mMonthPicker.setBackgroundColor(getResources().getColor(R.color.IndianRed, null));
        mMonthPicker.setValue(mMonth);
        mYearPicker.setEnabled(false);
        mMonthPicker.setEnabled(false);

        if (playerYear == mYear) {
            if (playerMonth == mMonth) {
                if (mFirst) {
                    return new int[]{15, 0};
                } else {
                    return new int[]{0, 15};
                }
            } else {
                if (mFirst) {
                    return new int[]{5, 0};
                } else {
                    return new int[]{0, 5};
                }
            }
        } else {
            if (mFirst) {
                return new int[]{-20, 0};
            } else {
                return new int[]{0, -20};
            }
        }
    }

    @Override
    public void disableControls() {

    }
}
