package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnlin.numberpicker.NumberPicker;

/**
 * Created by Alex Poon on 1/16/2018.
 */

public class PuzzleDateFragment extends Fragment implements PuzzleFragmentInterface {

    private boolean mFirst;
    NumberPicker mYearPicker, mMonthPicker;

    public PuzzleDateFragment() {
    }

    public PuzzleDateFragment(boolean first) {
        mFirst = first;
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
        mYearPicker = (NumberPicker) view.findViewById(R.id.year);
        mMonthPicker = (NumberPicker) view.findViewById(R.id.month);
    }

    @Override
    public int[] revealAnswer() {
        if (mFirst) {
            return new int[]{10, 0};
        } else {
            return new int[]{0, 10};
        }
    }
}
