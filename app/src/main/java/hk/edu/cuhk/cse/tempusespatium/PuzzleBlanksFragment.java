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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
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
    private WebView mWebView;

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
                                blank += "_____";
                                blank += token.substring(token.length() - 1);
                                blank += " ";
                                result += blank;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = (WebView) view.findViewById(R.id.webview);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fetchWp("https://en.wikipedia.org/wiki/Emu_War");
//                    fetchWp("https://en.wikipedia.org/wiki/Gallery_of_sovereign_state_flags");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public PuzzleBlanksFragment(boolean first) {
        mFirst = first;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blanks, container, false);
        return view;
    }

    @Override
    public int[] revealAnswer() {
        return new int[]{0, -10};
    }

    @Override
    public void disableControls() {

    }

}
