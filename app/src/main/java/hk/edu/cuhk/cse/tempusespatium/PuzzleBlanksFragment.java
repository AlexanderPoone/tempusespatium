package hk.edu.cuhk.cse.tempusespatium;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alex Poon on 1/22/2018.
 */

public class PuzzleBlanksFragment extends Fragment implements PuzzleFragmentInterface {

    private OkHttpClient mClient;
    private boolean mFirst;
    private String mWikipediaArt;
    private WebView mWebView;
    private int mNumOfFields;
    private List<String> mHiddenText;
    private String mLang;

    private Character azertyBuffer;

//    private BlanksChromeClient mBlanksChromeClient;

    @Override
    public boolean isRevealed() {
        return isRevealed;
    }

    private boolean isRevealed = false;

    void fetchWp(String url) throws IOException, NullPointerException {
        mClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
//        Response response = mClient.newCall(request).execute();

        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(e.getMessage(), e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: Get html from Wikipedia
                String result = null;
                try {
                    result = response.body().string();
                    Document doc = DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder().parse(new InputSource(new StringReader(result)));

//                    XPathExpression paraXPath = XPathFactory.newInstance()
//                            .newXPath().compile("//div/p[1]");
//                    XPathExpression imgPathXPath = XPathFactory.newInstance()
//                            .newXPath().compile("//*[@id='mw-content-text']/div/table[1]/tr[1]/td[1]/table/tr[1]/td/a/img/@src");
                    XPathExpression staticXPath = XPathFactory.newInstance()
                            .newXPath().compile("//div/p[1]/text()|//div/p[1]/b/text()|//div/p[1]/a/text()");
                    NodeList test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);

                    result = "";
                    for (int i = 0; i < test.getLength(); i++) {
                        if (test.item(i).getParentNode().getNodeName().equals("p")) {
                            result += test.item(i).getTextContent();
                        } else if (test.item(i).getParentNode().getNodeName().equals("b")) {
                            result += "<strong>";
                            result += test.item(i).getTextContent();
                            result += "</strong>";
                        } else {
                            StringTokenizer stringTokenizer = new StringTokenizer(test.item(i).getTextContent(), " -");
                            while (stringTokenizer.hasMoreTokens()) {
                                String token = stringTokenizer.nextToken();
                                if (token.length() < 3) { //token.substring(1, token.length() - 1).isEmpty()
                                    result += " " + token + " ";
                                    continue;
                                }
                                String blank = " ";
                                blank += token.substring(0, 1);
                                blank += "<input type=\"text\" name=\"b";
                                blank += Integer.toString(mNumOfFields);
                                blank += "\" size=\"";
                                blank += Integer.toString(token.length() - 2);
                                blank += "\">";
                                blank += token.substring(token.length() - 1);
                                blank += " ";
                                result += blank;
                                mHiddenText.add(token.substring(1, token.length() - 1));
                                mNumOfFields++;
                            }
                        }
                    }
//                    for (int i = 0; i < result_n.getLength(); i++)
//                    {
//                        Log.i("Test", result_n.item(i).getTextContent());
//                    }

//                    result = result.replaceAll("\\[.*?\\]", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final StringBuilder content = new StringBuilder();
                content.append("<html><head></head><body ");
                if (mFirst) content.append("bgcolor=\"#FFF8E7\"");
                else content.append("bgcolor=\"lavender\"");
                content.append(">");
                content.append(result);
                content.append("</body></html>");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadData(content.toString(), "text/html", null);
                    }
                });
            }
        });
    }

    public PuzzleBlanksFragment() {
    }

    public PuzzleBlanksFragment(boolean first, String wikipediaArt, String lang) {
        mFirst = first;
        mWikipediaArt = wikipediaArt;
        mNumOfFields = 0;
        mHiddenText = new ArrayList<>();
        mLang = lang;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blanks, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Keyboard keyboard;
        KeyboardView keyboardView;

        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);

//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mBlanksChromeClient = new BlanksChromeClient();
//                mWebView.setWebChromeClient(mBlanksChromeClient);
        mWebView.addJavascriptInterface(this, "android");
//            }
//        });
//        thread.run();


        if (mLang.equals("fr")) {
            keyboard = new Keyboard(getContext(), R.xml.diminished_azerty_avec_touches_mortes);

            // Lookup the KeyboardView
            keyboardView = (KeyboardView) view.findViewById(R.id.azerty);
        } else if (mLang.equals("de")) {
            keyboard = new Keyboard(getContext(), R.xml.diminished_qwertz);
            keyboardView = (KeyboardView) view.findViewById(R.id.qwertz);
        } else {
            keyboard = new Keyboard(getContext(), R.xml.diminished_qwerty);
            keyboardView = (KeyboardView) view.findViewById(R.id.qwerty);
        }

        keyboardView.setVisibility(View.VISIBLE);

        // Attach the keyboard to the view
        keyboardView.setKeyboard(keyboard);

        if (mLang.equals("fr")) {
            keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
                @Override
                public void onPress(int i) {

                }

                @Override
                public void onRelease(int i) {

                }

                @Override
                public void onKey(int i, int[] keyCodes) {
                    switch (i) {
                        case 8:
                            String JS = "javascript:(function() {" +
                                    "if (window.myReadOnly == 1) return;" +
                                    "var ele=document.activeElement;" +
                                    "var position=ele.value.slice(0, ele.selectionStart).length;" +
                                    "ele.value = ele.value.substr(0, position-1) + ele.value.substr(position);" +
                                    "ele.focus();" +
                                    "if (position != 0) ele.setSelectionRange(position-1, position-1);})()";
                            mWebView.loadUrl(JS);
                            break;
                        case Keyboard.KEYCODE_DELETE:
                            String JS_ = "javascript:(function() {" +
                                    "if (window.myReadOnly == 1) return;" +
                                    "var ele=document.activeElement;" +
                                    "var position=ele.value.slice(0, ele.selectionStart).length;" +
                                    "ele.value = ele.value.substr(0, position) + ele.value.substr(position+1);" +
                                    "ele.focus();" +
                                    "ele.setSelectionRange(position, position);})()";
                            mWebView.loadUrl(JS_);
                            break;
                        case 127:
                            azertyBuffer = 0;
                            break;
                        case 128:
                            azertyBuffer = 1;
                            break;
                        case 129:
                            azertyBuffer = 2;
                            break;
                        case 130:
                            azertyBuffer = 3;
                            break;
                        default:
                            if (azertyBuffer != null) {
                                switch (azertyBuffer) {
                                    case 0:   // Aigu
                                        switch (i) {
                                            case 65:
                                                i = 193;
                                                break;
                                            case 69:
                                                i = 201;
                                                break;
                                            case 97:
                                                i = 224;
                                                break;
                                            case 101:
                                                i = 232;
                                                break;
                                        }
                                        break;
                                    case 1:   // Circonflexe
                                        switch (i) {
                                            case 65:
                                                i = 194;
                                                break;
                                            case 69:
                                                i = 202;
                                                break;
                                            case 73:
                                                i = 206;
                                                break;
                                            case 79:
                                                i = 212;
                                                break;
                                            case 97:
                                                i = 226;
                                                break;
                                            case 101:
                                                i = 234;
                                                break;
                                            case 105:
                                                i = 238;
                                                break;
                                            case 111:
                                                i = 244;
                                                break;
                                        }
                                        break;
                                    case 2:   // Tréma
                                        switch (i) {
                                            case 69:
                                                i = 203;
                                                break;
                                            case 73:
                                                i = 207;
                                                break;
                                            case 85:
                                                i = 220;
                                                break;
                                            case 101:
                                                i = 235;
                                                break;
                                            case 105:
                                                i = 239;
                                                break;
                                            case 117:
                                                i = 252;
                                                break;
                                        }
                                        break;
                                    case 3:   // Grave
                                        switch (i) {
                                            case 65:
                                                i = 192;
                                                break;
                                            case 69:
                                                i = 200;
                                                break;
                                            case 85:
                                                i = 217;
                                                break;
                                            case 97:
                                                i = 224;
                                                break;
                                            case 101:
                                                i = 232;
                                                break;
                                            case 117:
                                                i = 249;
                                                break;
                                        }
                                        break;
                                }
                                azertyBuffer = null;
                            }
                            String JS__ = String.format(new Locale("fr"),
                                    "javascript:(function() {" +
                                            "if (window.myReadOnly == 1) return;" +
                                            "var ele=document.activeElement;" +
                                            "var position=ele.value.slice(0, ele.selectionStart).length;" +
                                            "ele.value = ele.value.substr(0, position) + '%c' + ele.value.substr(position);" +
                                            "ele.focus();" +
                                            "ele.setSelectionRange(position+1, position+1);})()", (char) i);
                            mWebView.loadUrl(JS__);
                            break;
                    }
                }

                @Override
                public void onText(CharSequence text) {

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
            });
        } else {
            keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
                @Override
                public void onPress(int i) {

                }

                @Override
                public void onRelease(int i) {

                }

                @Override
                public void onKey(int i, int[] ints) {
                    switch (i) {
                        case 8:
                            String JS = "javascript:(function() {" +
                                    "if (window.myReadOnly == 1) return;" +
                                    "var ele=document.activeElement;" +
                                    "var position=ele.value.slice(0, ele.selectionStart).length;" +
                                    "ele.value = ele.value.substr(0, position-1) + ele.value.substr(position);" +
                                    "ele.focus();" +
                                    "if (position != 0) ele.setSelectionRange(position-1, position-1);})()";
                            mWebView.loadUrl(JS);
                            break;
                        case Keyboard.KEYCODE_DELETE:
                            String JS_ = "javascript:(function() {" +
                                    "if (window.myReadOnly == 1) return;" +
                                    "var ele=document.activeElement;" +
                                    "var position=ele.value.slice(0, ele.selectionStart).length;" +
                                    "ele.value = ele.value.substr(0, position) + ele.value.substr(position+1);" +
                                    "ele.focus();" +
                                    "ele.setSelectionRange(position, position);})()";
                            mWebView.loadUrl(JS_);
                            break;
                        default:
                            String JS__ = String.format(new Locale("en"),
                                    "javascript:(function() {" +
                                            "if (window.myReadOnly == 1) return;" +
                                            "var ele=document.activeElement;" +
                                            "var position=ele.value.slice(0, ele.selectionStart).length;" +
                                            "ele.value = ele.value.substr(0, position) + '%c' + ele.value.substr(position);" +
                                            "ele.focus();" +
                                            "ele.setSelectionRange(position+1, position+1);})()", (char) i);
                            mWebView.loadUrl(JS__);
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
            });
        }

        BootstrapButton submitButton = (BootstrapButton) view.findViewById(R.id.submit_blanks);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Round1Activity) getActivity()).callReveal(mFirst);
            }
        });
        BootstrapButton clearButton = (BootstrapButton) view.findViewById(R.id.clear_blanks);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//
//        mWebView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//            }
//        });
//        mWebView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//                return false;
//            }
//        });

        mWebView.setFocusable(false);
//        mWebView.setFocusableInTouchMode(true);
//        clearButton.setFocusable(false);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String JS = String.format(new Locale("en"),
                        "javascript:(function() {" +
                                "if (window.myReadOnly == 1) return;" +
                                "var ele=document.activeElement; ele.value='';})()");
                mWebView.loadUrl(JS);
            }
        });


        Thread thread_ = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fetchWp(mWikipediaArt);
//                    fetchWp("https://en.wikipedia.org/wiki/Gallery_of_sovereign_state_flags");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread_.start();
    }


    private int mVal;

    @JavascriptInterface
    public void onData(int value) {
        Log.i("Triggered", Integer.toString(value));
        Log.i("TAG", Thread.currentThread().getName());
        mVal = value;
//        result.confirm();
        mCountDownLatch.countDown();
    }

    @Override
    public int[] revealAnswer() {
        isRevealed = true;
        int pointsChange;
        int correctNum = 0;
        for (int i = 0; i < mNumOfFields; i++) {
            Log.i("Loop", Integer.toString(i));
            final String JS = String.format(new Locale("en"),
                    "javascript:(function() {" +
                            "window.myReadOnly = 1;" +
                            "var field = document.getElementsByName('b%d')[0];" +
                            "if (field.value.toLowerCase() == '%s') { " +
                            "field.setAttribute('style', 'background-color: yellowGreen;'); " +
                            "android.onData(1); } else { " +
                            "field.setAttribute('style', 'background-color: coral;'); " +
                            "android.onData(0); } " +
                            "field.value = '%s'; " +
                            "field.setAttribute('readonly', true);})()",
                    i, mHiddenText.get(i).toLowerCase(), mHiddenText.get(i));
            mWebView.loadUrl(JS);
            try {
                mCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mCountDownLatch = new CountDownLatch(1);
            correctNum += mVal;
        }
        pointsChange = correctNum * 2 - (mNumOfFields - correctNum) * 1;
        if (mFirst) return new int[]{pointsChange, 0};
        else return new int[]{0, pointsChange};

//        if (mFirst) return new int[]{pointsChange[0], 0};
//        else return new int[]{0, pointsChange[0]};

//        mWebView.loadUrl("javascript:document.getElementById('username').value = ;");
        // document.getElementsByTagName("p")[0].setAttribute("readonly", true);

    }

    @Override
    public void disableControls() {
        // TODO: Remove the send button.
    }

    private CountDownLatch mCountDownLatch = new CountDownLatch(1);

//    /**
//     * Created by Alex Poon on 1/31/2018.
//     */
//
//    public class BlanksChromeClient extends WebChromeClient {
//
//        private String mVal;
//
//        public String getmVal() {
//            return mVal;
//        }
//
//        @Override
//        public boolean onJsAlert(WebView view, String url, final String message, final JsResult result) {
//            Log.i("Triggered", message);
//            Log.i("TAG", Thread.currentThread().getName());
//            mVal = message;
//            result.confirm();
//            mCountDownLatch.countDown();
//            return true;
//        }
//    }
}
