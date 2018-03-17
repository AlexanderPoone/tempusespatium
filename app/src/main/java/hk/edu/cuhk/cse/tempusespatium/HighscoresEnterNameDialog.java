package hk.edu.cuhk.cse.tempusespatium;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;

/**
 * Created by Alex Poon on 1/26/2018.
 */

public class HighscoresEnterNameDialog extends Dialog implements View.OnClickListener {
    private boolean mFirst;

    public HighscoresEnterNameDialog(@NonNull Context context) {
        super(context);
    }

    public HighscoresEnterNameDialog(@NonNull Context context, boolean first) {
        super(context);
        mFirst=first;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.highscores_enter_name_dialog);
        RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.add_name_layout);
        if (mFirst) {
            relativeLayout.setRotation(180);
        }
        AwesomeTextView rulesCaption = (AwesomeTextView) findViewById(R.id.highscores_caption);
        Typeface skylark_irc = ResourcesCompat.getFont(getContext(), R.font.skylark_itc_tt);
        BootstrapButton done=(BootstrapButton) findViewById(R.id.done_entering_text);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
}
