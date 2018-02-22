package hk.edu.cuhk.cse.tempusespatium;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    String mSelectedTopic;

    // English ↓
    Map<String, String> mTopics;
    LinkedHashMap<String, String> mArts;
    // English ↑

    // Deutsch ↓
    List<String> mTopicsUntrimmedDe;
    List<String> mTopicsDe;
    // Deutsch ↑

    AdapterView.OnItemClickListener mOnItemClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_searcher);

        Spinner spinner = (Spinner) findViewById(R.id.langSpinner);
        TopicSearcherDropdownAdapter adapter = new TopicSearcherDropdownAdapter(this, android.R.layout.simple_spinner_item, android.R.id.text1, getResources().getStringArray(R.array.locale_native));
//        TopicSearcherDropdownAdapter adapter=new TopicSearcherDropdownAdapter(this, 0, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.locale));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                autoCompleteTextView.setOnItemSelectedListener(null);
                if (i == 4) {
                    deutsch();
                } else {
                    english();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                english();
            }
        });
    }

    private void english() {
        mTopics = new TreeMap<>();

        // TODO: !!!!!!!!!!!!!! https://en.wikipedia.org/wiki/Category:WikiProjects_by_topic !!!!!!!!!!!!!!!!!!!!!!!!
        // Talk, file talk...
        BootstrapButton clearButton = (BootstrapButton) findViewById(R.id.topic_clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                autoCompleteTextView.setText("");
                autoCompleteTextView.showDropDown();
            }
        });

        final BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
        submitButton.setEnabled(false);

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
                                    Log.i("Test", test.item(i).getAttributes().item(0).getTextContent());
                                    //Topics
                                    mTopics.put(test.item(i).getTextContent().replaceFirst("Wikipedia:(WikiProject )?", "").replaceFirst("/(Popular|Most-viewed|Favourite) pages", "").replaceFirst("/Popular", "").replaceFirst("( task force| work group)", "").replaceFirst("Taskforces/(BPH/)?", "").replaceFirst("/", " > "), "https://en.wikipedia.org" + test.item(i).getAttributes().item(0).getTextContent());
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
                            final ArrayAdapter<String> adapter = new ArrayAdapter<>(TopicSearcherActivity.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(mTopics.keySet()));
                            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                            autoCompleteTextView.setAdapter(adapter);
                            autoCompleteTextView.performCompletion();
                            autoCompleteTextView.showDropDown();
                            mOnItemClickListener = new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    submitButton.setEnabled(false);

                                    mArts = new LinkedHashMap<>();
                                    mSelectedTopic = adapter.getItem(i);
                                    final Request request1 = new Request.Builder()
                                            .url(mTopics.get(mSelectedTopic))
                                            .build();
                                    final Response[] response1 = {null};
                                    final String[] body1 = {""};
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                response1[0] = mClient.newCall(request1).execute();
                                                body1[0] = response1[0].body().string();

                                                Document doc = DocumentBuilderFactory.newInstance()
                                                        .newDocumentBuilder().parse(new InputSource(new StringReader(body1[0])));
                                                XPathExpression staticXPath = XPathFactory.newInstance()
                                                        .newXPath().compile("//*[@id=\"mw-content-text\"]/div/table/tr/td[2]/a");
                                                NodeList test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);
                                                for (int i = 0; i < test.getLength(); i++) {
                                                    mArts.put(test.item(i).getTextContent(), "https://en.wikipedia.org" + test.item(i).getAttributes().item(0).getTextContent());
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
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    TextView textView = (TextView) findViewById(R.id.result);
                                                    textView.setText(mArts.keySet().toString());
                                                    textView.setMovementMethod(new ScrollingMovementMethod());

//                                                    BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
                                                    submitButton.setEnabled(true);
                                                    submitButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent jump = new Intent(getBaseContext(), Round1Activity.class);
                                                            jump.putExtra("topic", mSelectedTopic);
                                                            jump.putExtra("arts", mArts);
                                                            jump.putExtra("supportList", new ArrayList<>(mArts.keySet()));
                                                            startActivity(jump);
                                                            finish();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                    thread1.start();
                                }
                            };
                            autoCompleteTextView.setOnItemClickListener(mOnItemClickListener);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void francais() {
    }

    private void deutsch() {
        mTopicsDe = new ArrayList<>();
        mTopicsUntrimmedDe = new ArrayList<>();

        // TODO: !!!!!!!!!!!!!! https://en.wikipedia.org/wiki/Category:WikiProjects_by_topic !!!!!!!!!!!!!!!!!!!!!!!!
        // Talk, file talk...
        BootstrapButton clearButton = (BootstrapButton) findViewById(R.id.topic_clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                autoCompleteTextView.setText("");
                autoCompleteTextView.showDropDown();
            }
        });

        final BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
        submitButton.setEnabled(false);

        String base_url = "https://de.wikipedia.org/wiki/Wikipedia:Exzellente_Artikel";
        String base_url2 = "https://de.wikipedia.org/wiki/Wikipedia:Lesenswerte_Artikel";

        // NOT href="/wiki/Datei:Loudspeaker.svg" !!!

        mClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(base_url)
                .build();
        final Request request2 = new Request.Builder()
                .url(base_url2)
                .build();
        final Response[] response = {null, null};
        final String[] body = {"", ""};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response[0] = mClient.newCall(request).execute();
                    body[0] = response[0].body().string();
                    response[1] = mClient.newCall(request2).execute();
                    body[1] = response[1].body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = (TextView) findViewById(R.id.result);
                            textView.setText(body[0]);
                            NodeList test = null;
                            Node uberschrift = null;
                            try {
                                Document doc = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder().parse(new InputSource(new StringReader(body[0])));
                                XPathExpression staticXPath = XPathFactory.newInstance()
                                        .newXPath().compile("//*[@id=\"mw-content-text\"]/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/p");
//                                                    .newXPath().compile("//*[@id=\"mw-content-text\"]/div/table/tr/td/table/tr/td/table/tr/td/p");
                                //*[@id="mw-content-text"]/div/table/tbody/tr[2]/td/table[2]/tbody/tr[3]/td/table[2]/tbody/tr/td[1]/p[1]/a[4]
                                //*[@id="mw-content-text"]/div/table/tr/td/table/tr/td/table/tr/td/p/b/text()
                                //*[@id="mw-content-text"]/div/table/tr/td/table/tr/td/table/tr/td/p/a[not(.//img)]
                                test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);

                                for (int i = 0; i < test.getLength(); i++) {
                                    XPathExpression fett = XPathFactory.newInstance().newXPath().compile("b");
                                    uberschrift = (Node) fett.evaluate(test.item(i), XPathConstants.NODE);

                                    // 2
                                    mTopicsUntrimmedDe.add(uberschrift.getTextContent());

                                    mTopicsDe.add(uberschrift.getTextContent().substring(0, uberschrift.getTextContent().length() - 1).replaceFirst("^ ", ""));
//                                    Log.i("Uberschrift", uberschrift.getTextContent().substring(0, uberschrift.getTextContent().length() - 1));
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
                            List<String> tmp=new ArrayList<>(mTopicsDe);
                            Collections.sort(tmp);
                            final ArrayAdapter<String> adapter = new ArrayAdapter<>(TopicSearcherActivity.this, android.R.layout.simple_dropdown_item_1line, tmp);
                            final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                            autoCompleteTextView.setAdapter(adapter);
                            autoCompleteTextView.showDropDown();
                            final NodeList finalTest = test;
                            mOnItemClickListener = new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    submitButton.setEnabled(false);
                                    mArts = new LinkedHashMap<>();
                                    mSelectedTopic = adapter.getItem(i);

                                    //URL
                                    try {
                                        XPathExpression verweis = XPathFactory.newInstance().newXPath().compile("a[not(.//img)]");
                                        NodeList artikeln = (NodeList) verweis.evaluate(finalTest.item(mTopicsDe.lastIndexOf(mSelectedTopic)), XPathConstants.NODESET);

                                        for (int j = 0; j < artikeln.getLength(); j++) {
//                                            Log.i("Artikel", artikeln.item(j).getTextContent());
//                                            Log.i("URL", artikeln.item(j).getAttributes().getNamedItem("href").getTextContent());
                                            mArts.put(artikeln.item(j).getTextContent(), "https://de.wikipedia.org" + artikeln.item(j).getAttributes().getNamedItem("href").getTextContent());
                                        }

                                        // 2
                                        Document doc2 = DocumentBuilderFactory.newInstance()
                                                .newDocumentBuilder().parse(new InputSource(new StringReader(body[1])));
                                        XPathExpression staticXPath2 = XPathFactory.newInstance()
                                                .newXPath().compile(String.format("//*[@id=\"mw-content-text\"]/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/p[b/text()=\"%s\"]/a[not(.//img)]", mTopicsUntrimmedDe.get(mTopicsDe.lastIndexOf(mSelectedTopic))));
                                        NodeList artikeln2 = (NodeList) staticXPath2.evaluate(doc2, XPathConstants.NODESET);
                                        if (artikeln2.getLength() > 0) {
                                            for (int k = 0; k < artikeln2.getLength(); k++) {
                                                mArts.put(artikeln2.item(k).getTextContent(), "https://de.wikipedia.org" + artikeln2.item(k).getAttributes().getNamedItem("href").getTextContent());
                                                Log.i("Lucky you!", "2 Found! " + artikeln2.item(k).getTextContent());
                                            }
                                        }

                                    } catch (XPathExpressionException e) {
                                        e.printStackTrace();
                                    } catch (SAXException e) {
                                        e.printStackTrace();
                                    } catch (ParserConfigurationException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    TextView textView = (TextView) findViewById(R.id.result);
                                    textView.setText(mArts.keySet().toString());
                                    textView.setMovementMethod(new ScrollingMovementMethod());

//                                    BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
                                    submitButton.setEnabled(true);
                                    submitButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent jump = new Intent(getBaseContext(), Round1Activity.class);
                                            jump.putExtra("topic", mSelectedTopic);
                                            jump.putExtra("arts", mArts);
                                            jump.putExtra("supportList", new ArrayList<>(mArts.keySet()));
                                            startActivity(jump);
                                            finish();
                                        }
                                    });
                                }
                            };
                            autoCompleteTextView.setOnItemClickListener(mOnItemClickListener);
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
