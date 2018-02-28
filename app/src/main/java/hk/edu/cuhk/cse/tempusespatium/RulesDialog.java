package hk.edu.cuhk.cse.tempusespatium;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;

/**
 * Created by Alex Poon on 1/26/2018.
 */

public class RulesDialog extends Dialog implements View.OnClickListener {
    public RulesDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rules_dialog);
        AwesomeTextView rulesCaption = (AwesomeTextView) findViewById(R.id.rules_caption);
        Typeface skylark_irc = ResourcesCompat.getFont(getContext(), R.font.skylark_itc_tt);
        TextView rulesText = (TextView) findViewById(R.id.rules_text);
        rulesCaption.setTypeface(skylark_irc);
        rulesText.setTypeface(skylark_irc);
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
}
