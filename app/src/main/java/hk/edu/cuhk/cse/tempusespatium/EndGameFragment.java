package hk.edu.cuhk.cse.tempusespatium;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by softfeta on 3/15/18.
 */

public class EndGameFragment extends Fragment {
    boolean mFirst;
    boolean mWon;

    public EndGameFragment() {

    }

    public EndGameFragment(boolean first, boolean won) {
        mFirst = first;
        mWon = won;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_endgame, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.endgame_animation);
        if (mFirst && mWon)
            Glide.with(this).asGif().load(R.raw.player_1_wins).into(imageView);
        else if (!mFirst && mWon)
            Glide.with(this).asGif().load(R.raw.player_2_wins).into(imageView);
        else
            Glide.with(this).asGif().load("https://i.imgur.com/0mKXcg1.gif").into(imageView);
        if (mWon) {
            HighscoresEnterNameDialog highscoresEnterNameDialog = new HighscoresEnterNameDialog(getContext(), mFirst);
            highscoresEnterNameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            highscoresEnterNameDialog.setCancelable(false);
            highscoresEnterNameDialog.show();
        }
//        LottieAnimationView lottieAnimationView= (LottieAnimationView) view.findViewById(R.id.endgame_animation);
//        lottieAnimationView.setAnimation(R.raw.);
    }
}
