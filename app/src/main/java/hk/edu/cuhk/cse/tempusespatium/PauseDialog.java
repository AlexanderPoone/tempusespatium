package hk.edu.cuhk.cse.tempusespatium;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

/**
 * Created by Alex Poon on 1/26/2018.
 */

public class PauseDialog extends Dialog implements View.OnClickListener {
    public PauseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rules_pause);
    }

    @Override
    public void onClick(View view) {

    }
}
