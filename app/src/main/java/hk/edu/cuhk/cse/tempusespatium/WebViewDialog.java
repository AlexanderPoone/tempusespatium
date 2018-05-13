package hk.edu.cuhk.cse.tempusespatium;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by Alex Poon on 1/26/2018.
 */

public class WebViewDialog extends Dialog implements View.OnClickListener {
    String mUrl;

    public WebViewDialog(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview_dialog);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(mUrl);
        Button close = (Button) findViewById(R.id.close_learn);
        close.setOnClickListener(new View.OnClickListener() {
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
