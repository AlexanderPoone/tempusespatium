package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alex Poon on 1/16/2018.
 */

public class PuzzleAnagramFragment extends Fragment implements PuzzleFragmentInterface {


    /*
    TODO: Drag and drop
    May use BootstrapTags
    https://www.vocabulary.com/lists/191545
    use String length to: generateBlanks(int num)
    https://developer.android.com/guide/topics/ui/drag-drop.html

*/
    private boolean mFirst;

    @Override
    public boolean isRevealed() {
        return isRevealed;
    }

    private boolean isRevealed = false;

    public PuzzleAnagramFragment() {
    }

    public PuzzleAnagramFragment(boolean first) {
        mFirst = first;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flags, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int[] revealAnswer(boolean isEarlier) {
        isRevealed = true;

        if (mFirst) {
            return new int[]{10, 0};
        } else {
            return new int[]{0, 10};
        }
    }

    @Override
    public void disableControls() {

    }
}
