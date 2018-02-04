package hk.edu.cuhk.cse.tempusespatium;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputConnection;

/**
 * Created by Alex Poon on 2/2/2018.
 */

public class IME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    KeyboardView mKeyboardView;

    public IME(KeyboardView keyboardView) {
        super();
        mKeyboardView = keyboardView;
    }

    @Override
    public View onCreateInputView() {
        return mKeyboardView;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {
        InputConnection inputConnection = getCurrentInputConnection();
        switch (i) {
            case 8:
                inputConnection.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_DELETE:
                inputConnection.deleteSurroundingText(0, 1);
                break;
            default:
                inputConnection.commitText(String.valueOf(i), 1);
                break;
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
