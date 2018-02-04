package hk.edu.cuhk.cse.tempusespatium;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alex Poon on 2/4/2018.
 */

public class TopicSearcherActivity extends AppCompatActivity {

    OkHttpClient mClient;
    List<String> mUrls;
    List<String> mTopics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_searcher);

        mUrls = new ArrayList<>();
        mTopics = new ArrayList<>();

        // TODO: !!!!!!!!!!!!!! https://en.wikipedia.org/wiki/Category:WikiProjects_by_topic !!!!!!!!!!!!!!!!!!!!!!!!
        // Talk, file talk...
        BootstrapButton searchButton = (BootstrapButton) findViewById(R.id.topic_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jump = new Intent(getBaseContext(), Round1Activity.class);
                startActivity(jump);
                finish();
            }
        });

        String base_url = "https://en.wikipedia.org/wiki/Wikipedia:Lists_of_popular_pages_by_WikiProject";
        mClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(base_url)
                .build();
        final Response[] response = {null};
        final String[] body = {""};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response[0] = mClient.newCall(request).execute();
                    body[0] = response[0].body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = (TextView) findViewById(R.id.result);
                            textView.setText(body[0]);
                            try {
                                Document doc = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder().parse(new InputSource(new StringReader(body[0])));
                                XPathExpression staticXPath = XPathFactory.newInstance()
                                        .newXPath().compile("//*[@id=\"mw-content-text\"]/div/table/tr/td[1]/a");
                                NodeList test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);
                                for (int i = 0; i < test.getLength(); i++) {
                                    //URL
                                    mUrls.add("https://en.wikipedia.org" + test.item(i).getAttributes().item(0).getTextContent());
                                    Log.i("Test", test.item(i).getAttributes().item(0).getTextContent());
                                    //Topics
                                    mTopics.add(test.item(i).getAttributes().item(1).getTextContent().replaceFirst("Wikipedia:(WikiProject )?", "").replaceFirst("/Popular pages", "").replaceFirst("( task force| work group)", "").replaceFirst("Taskforces/(BPH/)?", "").replaceFirst("/", " > "));
//                                    Log.i("Test", test.item(i).getAttributes().item(1).getTextContent());
                                }

                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (XPathExpressionException e) {
                                e.printStackTrace();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(TopicSearcherActivity.this,android.R.layout.select_dialog_item, mTopics);
                            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                            autoCompleteTextView.setAdapter(adapter);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
