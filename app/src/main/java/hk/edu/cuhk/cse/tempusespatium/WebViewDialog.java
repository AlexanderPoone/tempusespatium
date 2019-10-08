package hk.edu.cuhk.cse.tempusespatium;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static hk.edu.cuhk.cse.tempusespatium.Constants.SHAREDPREFS_NAME;
import static java.lang.Thread.sleep;

/**
 * Created by Alex Poon on 1/26/2018.
 */

public class WebViewDialog extends Dialog implements View.OnClickListener {
    private String mUrl;
    private String JS;
    private WebView webView;
    private RelativeLayout mRelativeLayout;
    private TextView mThemeLabel;
    private SharedPreferences sharedPreferences;
    private static final String LEARNING_THEME_KEY = "LEARNING_THEME";

    WebViewDialog(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getContext().getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE);
        final int savedTheme = sharedPreferences.getInt(LEARNING_THEME_KEY, 0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview_dialog);

        TextView webview_loading = findViewById(R.id.webview_loading);
        mThemeLabel = findViewById(R.id.lbl_theme);

        mRelativeLayout = findViewById(R.id.webview_rel_layout);
        webview_loading.setText(R.string.loading);
        switch (savedTheme) {
            case 0:
                mRelativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.PapayaWhip));
                break;
            case 1:
                mRelativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.White));
                break;
            case 2:
                mRelativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.SmokeyBlack));
                webview_loading.setTextColor(getContext().getColor(R.color.Seashell));
                mThemeLabel.setTextColor(getContext().getColor(R.color.Seashell));
                break;
        }


        Button close = (Button) findViewById(R.id.close_learn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });



        webView = (WebView) findViewById(R.id.webview);
        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(mUrl);
        JS = "javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var script = document.createElement('script');" +
                "script.crossorigin = 'anonymous';" +
                "script.type = 'text/javascript';" +
                "script.integrity = 'sha256-pasqAKBDmFT4eHoN2ndd6lN370kFiGUFyTiUHWhU7k8=';" +
                "script.src = 'https://code.jquery.com/jquery-3.4.1.slim.min.js';" +
                "parent.appendChild(script);" +
                " })()";
        webView.loadUrl(JS);

        CountDownTimer cdt = new CountDownTimer(2000, 2000) {
            @Override
            public long pause() {
                return super.pause();
            }

            @Override
            public long resume() {
                return super.resume();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                JS = "javascript:(function() { $(document).ready(function(){ $('.ambox').hide();$('.reference').hide();$('.hatnote').hide();$('#mw-mf-page-center > header > form').hide();$('span.mw-editsection > a').hide();$('#content > div.pre-content.heading-holder > nav').hide();$('#mw-mf-page-center > footer').hide();$('.mw-ui-icon').hide();$('a:not(.image)').replaceWith(function(){return $('<span style=\"color:olive\">').append($(this).html());}); }); })()";
                webView.loadUrl(JS);
                switch (savedTheme) {
                    case 0:
                        solarized();
                        break;
                    case 1:
                        white();
                        break;
                    case 2:
                        dark();
                        break;
                }
                webView.setVisibility(View.VISIBLE);
            }
        };
        cdt.start();
        final Button solarized = findViewById(R.id.btn_solarized);
        final Button white = findViewById(R.id.btn_white);
        Button dark = findViewById(R.id.btn_dark);
        solarized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solarized();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(LEARNING_THEME_KEY, 0);
                editor.apply();
            }
        });
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                white();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(LEARNING_THEME_KEY, 1);
                editor.apply();
            }
        });
        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dark();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(LEARNING_THEME_KEY, 2);
                editor.apply();
            }
        });

    }
    private void solarized() {
        JS = "javascript:(function() { $(document).ready(function(){ $('#content').css('background-color','PapayaWhip');$('h1, h2, span, p, div').css('color','black'); }); })()";
        webView.loadUrl(JS);
        mThemeLabel.setTextColor(getContext().getColor(R.color.Black));
        mRelativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.PapayaWhip));
    }

    private void white() {
        JS = "javascript:(function() { $(document).ready(function(){ $('#content').css('background-color','white');$('h1, h2, span, p, div').css('color','black'); }); })()";
        webView.loadUrl(JS);
        mThemeLabel.setTextColor(getContext().getColor(R.color.Black));
        mRelativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.White));
    }

    private void dark() {
        JS = "javascript:(function() { $(document).ready(function(){ $('#content').css('background-color','#100C08');$('h1, h2, span, p, div').css('color','Seashell');$('.infobox h1, .infobox h2, .infobox span, .infobox p, .infobox div, .infobox').css('color','black'); }); })()";
        webView.loadUrl(JS);
        mThemeLabel.setTextColor(getContext().getColor(R.color.Seashell));
        mRelativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.SmokeyBlack));
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
