package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

/**
 * Created by softfeta on 3/15/18.
 */

public class EndGameFragment extends Fragment {
    boolean mWon;

    public EndGameFragment() {

    }

    public EndGameFragment(boolean won) {
        mWon=won;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LottieAnimationView lottieAnimationView= (LottieAnimationView) view.findViewById(R.id.endgame_animation);
//        lottieAnimationView.setAnimation(R.raw.);
    }
}
