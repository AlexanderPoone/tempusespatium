package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private BlanksChromeClient mBlanksChromeClient;

    void fetchWp(String url) throws IOException, NullPointerException {
        mClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mClient.newCall(request).execute();

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
                            StringTokenizer stringTokenizer = new StringTokenizer(test.item(i).getTextContent(), " ");
                            while (stringTokenizer.hasMoreTokens()) {
                                String token = stringTokenizer.nextToken();
                                String blank = " ";
                                blank += token.substring(0, 1);
                                blank += "<input type=\"text\" name=\"";
                                blank += Integer.toString(mNumOfFields);
                                blank += "\" size=\"";
                                blank += Integer.toString(token.length() - 2);
                                blank += "\">";
                                blank += token.substring(token.length() - 1);
                                blank += " ";
                                result += blank;
                                mHiddenText.add(token.substring(1, token.length() - 2));
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

    public PuzzleBlanksFragment(boolean first, String wikipediaArt) {
        mFirst = first;
        mWikipediaArt = wikipediaArt;
        mNumOfFields = 0;
        mHiddenText = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blanks, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mBlanksChromeClient = new BlanksChromeClient();
        mWebView.setWebChromeClient(mBlanksChromeClient);

        BootstrapButton submitButton = (BootstrapButton) view.findViewById(R.id.submit_blanks);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Round1Activity) getActivity()).callReveal(mFirst);
            }
        });
        BootstrapButton clearButton = (BootstrapButton) view.findViewById(R.id.clear_blanks);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String JS = String.format(new Locale("en"),
                        "javascript:document.activeElement.value='';");
                mWebView.loadUrl(JS);
            }
        });


        Thread thread = new Thread(new Runnable() {
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
        thread.start();
    }

    @Override
    public int[] revealAnswer() {
        int correctNum = 0;
        for (int i = 0; i < mNumOfFields; i++) {
            String JS = String.format(new Locale("en"),
                    "javascript:var field = document.getElementByName('%d');" +
                            "if (field.value.toLowerCase() == '%s') { " +
                            "field.setAttribute('style', 'background-color: yellowGreen;'); " +
                            "alert('1'); } else { " +
                            "field.setAttribute('style', 'background-color: coral;'); " +
                            "alert('0'); } " +
                            "field.value = '%s'; " +
                            "field.setAttribute('readonly', true);",
                    i, mHiddenText.get(i).toLowerCase(), mHiddenText.get(i));
            mWebView.loadUrl(JS);
            correctNum += Integer.parseInt(mBlanksChromeClient.getmVal());                  // May have sync problems?
        }
        int pointsChange = correctNum * 20 - mNumOfFields * -15;
        if (mFirst) return new int[]{pointsChange, 0};
        else return new int[]{0, pointsChange};

//        mWebView.loadUrl("javascript:document.getElementById('username').value = ;");
        // document.getElementsByTagName("p")[0].setAttribute("readonly", true);
    }

    @Override
    public void disableControls() {
        // TODO: Remove the send button.
    }

}
