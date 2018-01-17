package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

/**
 * Created by Alex Poon on 1/16/2018.
 */

public class PuzzleFlagsFragment extends Fragment {
    String mCountry;
    String mFlagURL;

    PuzzleMapFragment(String country, String flagURL) {
        mCountry=country;
        mFlagURL=flagURL;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.with(getContext()).load(mFlagURL).into();
        "Flag of  ".replace("^Flag of  ", "");

    }
}
