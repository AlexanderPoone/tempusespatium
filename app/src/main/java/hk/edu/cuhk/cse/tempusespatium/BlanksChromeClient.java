package hk.edu.cuhk.cse.tempusespatium;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Alex Poon on 1/31/2018.
 */

public class BlanksChromeClient extends WebChromeClient {

    private String mVal;

    public String getmVal() {
        return mVal;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        mVal=message;
        result.confirm();
        return true;
    }
}
