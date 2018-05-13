package hk.edu.cuhk.cse.tempusespatium;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alex Poon on 2/4/2018.
 */

public class TopicSearcherActivity extends AppCompatActivity {

    OkHttpClient mClient;
    String mQuestionLang = null;

    /* */
    ArrayList<String> mSelectedTopic;
    /* */

    // English ↓
    Map<String, String> mTopics;
    LinkedHashMap<String, String> mArts;
    // English ↑

    // Deutsch ↓
    List<String> mTopicsUntrimmedDe;
    List<String> mTopicsDe;
    // Deutsch ↑

    AdapterView.OnItemClickListener mOnItemClickListener;

    private static HashMap<String, String> items;

    static {
        items = new HashMap<>();
        items.put("Attacks", "Q81672");
        items.put("Battles", "Q178561");
        items.put("Coup d'état", "Q45382");
    }

    private final ArrayList<String> selectedItems = new ArrayList<>(items.values());


    private static String[] choiceOptions = {"Name (in the language you picked)", "Capital", "Population"};
    private final ArrayList<String> selectedOptions = new ArrayList<>(Arrays.asList(choiceOptions));


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_searcher);

        BootstrapButton dateGameButton = (BootstrapButton) findViewById(R.id.date_game_button);
        final String[] selectableItems = items.keySet().toArray(new String[items.keySet().size()]);
        Arrays.sort(selectableItems);
        boolean[] fill = new boolean[selectableItems.length];
        Arrays.fill(fill, true);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select the covered events")
                .setMultiChoiceItems(selectableItems, fill, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(items.get(selectableItems[indexSelected]));
                        } else if (selectedItems.contains(items.get(selectableItems[indexSelected]))) {
                            selectedItems.remove(items.get(selectableItems[indexSelected]));
                        }
                    }
                })
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .create();
        Typeface baker_signet = ResourcesCompat.getFont(this, R.font.baker_signet_bt);
        LinearLayout customLinearLayout = new LinearLayout(this);
        final CheckBox customTag = new CheckBox(this);
        customTag.setTypeface(baker_signet);
        customTag.setText(Html.fromHtml("Custom: <small>(e.g. Q7944 Earthquakes)</small> "));
        TextView q = new TextView(this);
        q.setText("Q");
        q.setTextColor(getColor(android.R.color.black));
        q.setTypeface(baker_signet);
        final EditText editText = new EditText(this);
        editText.setTypeface(baker_signet);
        editText.setHint("Wikidata item identifier");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        customLinearLayout.addView(customTag);
        customLinearLayout.addView(q);
        customLinearLayout.addView(editText);

        dialog.getListView().addFooterView(customLinearLayout);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog2) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int size = selectedItems.size();
                        if (customTag.isChecked()) {
                            if (editText.getText().toString().trim().length() == 0) {
                                Snackbar snack = Snackbar.make(dialog.getListView(), "Please enter an identifier!", Snackbar.LENGTH_LONG);
                                View view = snack.getView();
                                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                                params.gravity = Gravity.TOP;
                                view.setLayoutParams(params);
                                snack.show();
                                return;
                            } else {
                                selectedItems.add("Q" + editText.getText().toString());
                                size++;
                            }
                        }
                        if (size != 0) {
                            dialog.dismiss();
                        } else {
                            Snackbar snack = Snackbar.make(dialog.getListView(), "Select at least 1 item!", Snackbar.LENGTH_LONG);
                            View view = snack.getView();
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                            params.gravity = Gravity.TOP;
                            view.setLayoutParams(params);
                            snack.show();
                        }
                    }
                });
            }
        });
        dateGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); // must be placed after show()
            }
        });


        BootstrapButton choiceGameButton = (BootstrapButton) findViewById(R.id.choice_game_button);
        boolean[] fillOptions = new boolean[choiceOptions.length];
        Arrays.fill(fillOptions, true);

        final AlertDialog dialogOptions = new AlertDialog.Builder(this)
                .setTitle("Select the covered attributes")
                .setMultiChoiceItems(choiceOptions, fillOptions, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            selectedOptions.add(choiceOptions[indexSelected]);
                        } else if (selectedOptions.contains(choiceOptions[indexSelected])) {
                            selectedOptions.remove(choiceOptions[indexSelected]);
                        }
                    }
                })
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .create();
        dialogOptions.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog2) {
                Button b = dialogOptions.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedOptions.size() != 0) {
                            dialogOptions.dismiss();
                        } else {
                            Snackbar snack = Snackbar.make(dialogOptions.getListView(), "Select at least 1 item!", Snackbar.LENGTH_LONG);
                            View view = snack.getView();
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                            params.gravity = Gravity.TOP;
                            view.setLayoutParams(params);
                            snack.show();
                        }
                    }
                });
            }
        });
        choiceGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOptions.show();
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.langSpinner);
        TopicSearcherDropdownAdapter adapter = new TopicSearcherDropdownAdapter(this, android.R.layout.simple_spinner_item, android.R.id.text1, getResources().getStringArray(R.array.gameplay_locale_native));
//        TopicSearcherDropdownAdapter adapter=new TopicSearcherDropdownAdapter(this, 0, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.locale));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                autoCompleteTextView.setOnItemSelectedListener(null);
                if (i == 1) {
                    espanol();
                    mQuestionLang = "es";
                } else if (i == 2) {
                    deutsch();
                    mQuestionLang = "de";
                } else if (i == 3) {
                    francais();
                    mQuestionLang = "fr";
                } else if (i == 4) {
                    ukrajinska();
                    mQuestionLang = "uk";
                } else {
                    english();
                    mQuestionLang = "en";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                english();
            }
        });
    }

    private void english() {
        mSelectedTopic = new ArrayList<>();
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
                            // TODO:
//                            TextView textView = (TextView) findViewById(R.id.result);
//                            textView.setText(body[0]);
                            try {
                                Document doc = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder().parse(new InputSource(new StringReader(body[0])));
                                XPathExpression staticXPath = XPathFactory.newInstance()
                                        .newXPath().compile("//*[@id=\"mw-content-text\"]/div/table/tr/td[1]/a");
                                NodeList test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);
                                for (int i = 0; i < test.getLength(); i++) {
                                    //URL
                                    Log.i("Test", test.item(i).getAttributes().getNamedItem("href").getTextContent());
                                    //Topics
                                    mTopics.put(test.item(i).getTextContent().replaceFirst("Wikipedia:(WikiProject )?", "").replaceFirst("/(Popular|Most-viewed|Favourite) pages", "").replaceFirst("/(Popular|Article hits)", "").replaceFirst("( task force| work group)", "").replaceFirst("Taskforces/(BPH/)?", "").replaceFirst("/", " > "), "https://en.wikipedia.org" + test.item(i).getAttributes().getNamedItem("href").getTextContent());
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
                                    String selectedTopic = adapter.getItem(i);


                                    /* */
                                    int randomInt, randomInt2;
                                    Random random = new Random();
                                    do {
                                        randomInt = random.nextInt(adapter.getCount());
                                    } while (randomInt == i);
                                    String selectedTopicAlt1 = adapter.getItem(randomInt);

                                    do {
                                        randomInt2 = random.nextInt(adapter.getCount());
                                    } while (randomInt2 == i || randomInt2 == randomInt);
                                    String selectedTopicAlt2 = adapter.getItem(randomInt2);

                                    mSelectedTopic.add(selectedTopic);
                                    mSelectedTopic.add(selectedTopicAlt1);
                                    mSelectedTopic.add(selectedTopicAlt2);
                                    /* */


                                    final Request request1 = new Request.Builder()
                                            .url(mTopics.get(mSelectedTopic.get(0)))
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
                                                    mArts.put(test.item(i).getTextContent(), "https://en.wikipedia.org" + test.item(i).getAttributes().getNamedItem("href").getTextContent());
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
                                                    TagContainerLayout tagView = (TagContainerLayout) findViewById(R.id.result);
                                                    Set<String> keySet = mArts.keySet();
                                                    tagView.setTags(keySet.toArray(new String[keySet.size()]));
                                                    tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
                                                        @Override
                                                        public void onTagClick(int position, String text) {
                                                            WebViewDialog webViewDialog = new WebViewDialog(TopicSearcherActivity.this, "https://" + mQuestionLang + ".wikipedia.org/wiki/" + text);
                                                            webViewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            webViewDialog.show();
                                                        }

                                                        @Override
                                                        public void onTagLongClick(int position, String text) {

                                                        }

                                                        @Override
                                                        public void onTagCrossClick(int position) {

                                                        }
                                                    });
                                                    // TODO:
//                                                    TextView textView = (TextView) findViewById(R.id.result);
//                                                    textView.setText(mArts.keySet().toString());
//                                                    textView.setMovementMethod(new ScrollingMovementMethod());

//                                                    BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
                                                    submitButton.setEnabled(true);
                                                    submitButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent jump = new Intent(getBaseContext(), Round1Activity.class);
                                                            jump.putExtra("lang", mQuestionLang);
                                                            jump.putExtra("topic", mSelectedTopic);
                                                            jump.putExtra("arts", mArts);

                                                            jump.putExtra("artsAlt1", mArts);
                                                            jump.putExtra("artsAlt2", mArts);
                                                            jump.putExtra("supportList", new ArrayList<>(mArts.keySet()));

                                                            jump.putExtra("dateGameList", selectedItems);
                                                            jump.putExtra("choiceGameList", selectedOptions);
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
        // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Article_d%27importance_maximum
        // Preferee: https://fr.wikipedia.org/wiki/Sp%C3%A9cial:ArbreCat%C3%A9gorie/Article_d%27importance_maximum
        mSelectedTopic = new ArrayList<>();
        mTopics = new TreeMap<>();

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

        String base_url = "https://fr.wikipedia.org/wiki/Sp%C3%A9cial:ArbreCat%C3%A9gorie/Article_d%27importance_maximum";
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
//                            TextView textView = (TextView) findViewById(R.id.result);
//                            textView.setText(body[0]);
                            try {
                                Document doc = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder().parse(new InputSource(new StringReader(body[0])));
                                XPathExpression staticXPath = XPathFactory.newInstance()
                                        .newXPath().compile("//*[@id=\"mw-content-text\"]/div[3]/div/div[2]/div/div/a");
                                NodeList test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);
                                for (int i = 0; i < test.getLength(); i++) {
                                    //URL
                                    //Topics
                                    String tmp = test.item(i).getTextContent().replaceFirst("Article (du projet |de |d')?", "").replaceFirst(" d'importance maximum", "").replaceFirst("( )?sur (l'|l’|le |la |les )?", "");
                                    mTopics.put(tmp.substring(0, 1).toUpperCase() + tmp.substring(1), "https://fr.wikipedia.org" + test.item(i).getAttributes().getNamedItem("href").getTextContent());
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
                                    String selectedTopic = adapter.getItem(i);


                                    /* */
                                    int randomInt, randomInt2;
                                    Random random = new Random();
                                    do {
                                        randomInt = random.nextInt(adapter.getCount());
                                    } while (randomInt == i);
                                    String selectedTopicAlt1 = adapter.getItem(randomInt);

                                    do {
                                        randomInt2 = random.nextInt(adapter.getCount());
                                    } while (randomInt2 == i || randomInt2 == randomInt);
                                    String selectedTopicAlt2 = adapter.getItem(randomInt2);

                                    mSelectedTopic.add(selectedTopic);
                                    mSelectedTopic.add(selectedTopicAlt1);
                                    mSelectedTopic.add(selectedTopicAlt2);
                                    /* */


                                    final Request request1 = new Request.Builder()
                                            .url(mTopics.get(mSelectedTopic.get(0)))
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
                                                        .newXPath().compile("//*[@id='mw-pages']/div[2]/div/div/ul/li/a");
                                                NodeList test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);
                                                for (int i = 0; i < test.getLength(); i++) {
                                                    mArts.put(test.item(i).getTextContent().replaceFirst("Discussion:", ""), "https://fr.wikipedia.org" + test.item(i).getAttributes().getNamedItem("href").getTextContent().replaceFirst("Discussion:", ""));
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
                                                    TagContainerLayout tagView = (TagContainerLayout) findViewById(R.id.result);
                                                    Set<String> keySet = mArts.keySet();
                                                    tagView.setTags(keySet.toArray(new String[keySet.size()]));
                                                    tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
                                                        @Override
                                                        public void onTagClick(int position, String text) {
                                                            WebViewDialog webViewDialog = new WebViewDialog(TopicSearcherActivity.this, "https://" + mQuestionLang + ".wikipedia.org/wiki/" + text);
                                                            webViewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            webViewDialog.show();
                                                        }

                                                        @Override
                                                        public void onTagLongClick(int position, String text) {

                                                        }

                                                        @Override
                                                        public void onTagCrossClick(int position) {

                                                        }
                                                    });
//                                                    TextView textView = (TextView) findViewById(R.id.result);
//                                                    textView.setText(mArts.keySet().toString());
//                                                    textView.setMovementMethod(new ScrollingMovementMethod());

//                                                    BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
                                                    submitButton.setEnabled(true);
                                                    submitButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent jump = new Intent(getBaseContext(), Round1Activity.class);
                                                            jump.putExtra("lang", mQuestionLang);
                                                            jump.putExtra("topic", mSelectedTopic);
                                                            jump.putExtra("arts", mArts);

                                                            jump.putExtra("artsAlt1", mArts);
                                                            jump.putExtra("artsAlt2", mArts);
                                                            jump.putExtra("supportList", new ArrayList<>(mArts.keySet()));

                                                            jump.putExtra("dateGameList", selectedItems);
                                                            jump.putExtra("choiceGameList", selectedOptions);
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
            // replaceFirst("Article (du projet |de |d' )?", "").replaceFirst(" d'importance maximum‎").
            // replaceFirst("sur (l'|le |la |les )", "")

            // char[] tmp = str.toCharArray();
            // tmp[0] = Character.toUpperCase(tmp[0]);
            // str = String.valueOf(tmp)

            //"//*[@id='mw-content-text']/div[3]/div/div[2]/div/div/a"

            //"//*[@id='mw-pages']/div[2]/div/div/ul/li/a"
            //Just remove Discussion: from all URLs!
            // replaceFirst("Discussion:", "")
        });
        thread.start();
    }

    private void deutsch() {
        mSelectedTopic = new ArrayList<>();
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
//                            TextView textView = (TextView) findViewById(R.id.result);
//                            textView.setText(body[0]);
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
                            List<String> tmp = new ArrayList<>(mTopicsDe);
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
                                    String selectedTopic = adapter.getItem(i);

                                    /* */
                                    int randomInt, randomInt2;
                                    Random random = new Random();
                                    do {
                                        randomInt = random.nextInt(adapter.getCount());
                                    } while (randomInt == i);
                                    String selectedTopicAlt1 = adapter.getItem(randomInt);

                                    do {
                                        randomInt2 = random.nextInt(adapter.getCount());
                                    } while (randomInt2 == i || randomInt2 == randomInt);
                                    String selectedTopicAlt2 = adapter.getItem(randomInt2);

                                    mSelectedTopic.add(selectedTopic);
                                    mSelectedTopic.add(selectedTopicAlt1);
                                    mSelectedTopic.add(selectedTopicAlt2);
                                    /* */


                                    //URL
                                    try {
                                        XPathExpression verweis = XPathFactory.newInstance().newXPath().compile("a[not(.//img)]");
                                        NodeList artikeln = (NodeList) verweis.evaluate(finalTest.item(mTopicsDe.lastIndexOf(selectedTopic)), XPathConstants.NODESET);

                                        for (int j = 0; j < artikeln.getLength(); j++) {
//                                            Log.i("Artikel", artikeln.item(j).getTextContent());
//                                            Log.i("URL", artikeln.item(j).getAttributes().getNamedItem("href").getTextContent());
                                            mArts.put(artikeln.item(j).getTextContent(), "https://de.wikipedia.org" + artikeln.item(j).getAttributes().getNamedItem("href").getTextContent());
                                        }

                                        // 2
                                        Document doc2 = DocumentBuilderFactory.newInstance()
                                                .newDocumentBuilder().parse(new InputSource(new StringReader(body[1])));
                                        XPathExpression staticXPath2 = XPathFactory.newInstance()
                                                .newXPath().compile(String.format("//*[@id=\"mw-content-text\"]/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/p[b/text()=\"%s\"]/a[not(.//img)]", mTopicsUntrimmedDe.get(mTopicsDe.lastIndexOf(selectedTopic))));
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
                                    TagContainerLayout tagView = (TagContainerLayout) findViewById(R.id.result);
                                    Set<String> keySet = mArts.keySet();
                                    tagView.setTags(keySet.toArray(new String[keySet.size()]));
                                    tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
                                        @Override
                                        public void onTagClick(int position, String text) {
                                            WebViewDialog webViewDialog = new WebViewDialog(TopicSearcherActivity.this, "https://" + mQuestionLang + ".wikipedia.org/wiki/" + text);
                                            webViewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            webViewDialog.show();
                                        }

                                        @Override
                                        public void onTagLongClick(int position, String text) {

                                        }

                                        @Override
                                        public void onTagCrossClick(int position) {

                                        }
                                    });
//                                    TextView textView = (TextView) findViewById(R.id.result);
//                                    textView.setText(mArts.keySet().toString());
//                                    textView.setMovementMethod(new ScrollingMovementMethod());

//                                    BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
                                    submitButton.setEnabled(true);
                                    submitButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent jump = new Intent(getBaseContext(), Round1Activity.class);
                                            jump.putExtra("lang", mQuestionLang);
                                            jump.putExtra("topic", mSelectedTopic);
                                            jump.putExtra("arts", mArts);

                                            jump.putExtra("artsAlt1", mArts);
                                            jump.putExtra("artsAlt2", mArts);

                                            jump.putExtra("supportList", new ArrayList<>(mArts.keySet()));
                                            jump.putExtra("dateGameList", selectedItems);
                                            jump.putExtra("choiceGameList", selectedOptions);
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

    private void espanol() {
        // https://es.wikipedia.org/wiki/Categor%C3%ADa:Wikiproyectos/Art%C3%ADculos
        // https://es.wikipedia.org/wiki/Especial:ÁrbolDeCategorías/Wikiproyectos/Artículos?target=&mode=categories&namespaces=&title=Especial%3AÁrbolDeCategorías


        // https://es.wikipedia.org/wiki/Wikipedia:Artículos_buenos
        // https://es.wikipedia.org/wiki/Wikipedia:Art%C3%ADculos_destacados
        // https://es.wikipedia.org/wiki/Wikipedia:Lista_de_art%C3%ADculos_que_toda_Wikipedia_deber%C3%ADa_tener
        mSelectedTopic = new ArrayList<>();
        mTopics = new TreeMap<>();

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

        String base_url = "https://es.wikipedia.org/wiki/Especial:ÁrbolDeCategorías/Wikiproyectos/Artículos?target=&mode=categories&namespaces=&title=Especial%3AÁrbolDeCategorías";
//        https://es.wikipedia.org/w/api.php?action=query&list=categorymembers&cmtitle=Categor%C3%ADa:Wikiproyectos/Art%C3%ADculos&cmtype=subcat&cmlimit=500&format=json
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
                    NodeList test = null;
                    Node link;
//                            TextView textView = (TextView) findViewById(R.id.result);
//                            textView.setText(body[0]);
                    try {
                        Document doc = DocumentBuilderFactory.newInstance()
                                .newDocumentBuilder().parse(new InputSource(new StringReader(body[0])));
                        XPathExpression staticXPath = XPathFactory.newInstance()
                                .newXPath().compile("//*[@id=\"mw-content-text\"]/div[3]/div/div[2]/div/div[1]");
                        test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);

                        for (int i = 0; i < test.getLength(); i++) {
                            //go to <a>
                            XPathExpression name = XPathFactory.newInstance().newXPath().compile("a");
                            link = (Node) name.evaluate(test.item(i), XPathConstants.NODE);

                            String tmp = link.getTextContent().replaceFirst("^Wikiproyecto:", "").replaceFirst("/Artículos", "");
                            String url = "https://es.wikipedia.org" + link.getAttributes().getNamedItem("href").getTextContent();
                            mTopics.put(tmp, url);

                            //go to second <span>
                            name = XPathFactory.newInstance().newXPath().compile("span[2]");
                            link = (Node) name.evaluate(test.item(i), XPathConstants.NODE);
                            if (link.getTextContent().contains("c")) {
                                Request request = new Request.Builder()
                                        .url(url)
                                        .build();

                                Response response = mClient.newCall(request).execute();
                                String tmpCont = response.body().string();
                                Document tmpDoc = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder().parse(new InputSource(new StringReader(tmpCont)));
                                name = XPathFactory.newInstance()
                                        .newXPath().compile("//*[@id=\"mw-subcategories\"]/div/ul/li/div/div/a");
                                NodeList cont = (NodeList) name.evaluate(tmpDoc, XPathConstants.NODESET);
                                for (int j = 0; j < cont.getLength(); j++) {
                                    mTopics.put(cont.item(j).getTextContent().replaceFirst("^Wikiproyecto:", "").replaceFirst("/Artículos.*$", ""), "https://es.wikipedia.org" + cont.item(j).getAttributes().getNamedItem("href").getTextContent());
                                }
                            }
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
                            final ArrayAdapter<String> adapter = new ArrayAdapter<>(TopicSearcherActivity.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(mTopics.keySet()));
                            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                            autoCompleteTextView.setAdapter(adapter);
                            autoCompleteTextView.performCompletion();
                            autoCompleteTextView.showDropDown();
                            mOnItemClickListener = new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                                        long l) {
                                    submitButton.setEnabled(false);

                                    mArts = new LinkedHashMap<>();
                                    String selectedTopic = adapter.getItem(i);


                                    /* */
                                    int randomInt, randomInt2;
                                    Random random = new Random();
                                    do {
                                        randomInt = random.nextInt(adapter.getCount());
                                    } while (randomInt == i);
                                    String selectedTopicAlt1 = adapter.getItem(randomInt);

                                    do {
                                        randomInt2 = random.nextInt(adapter.getCount());
                                    } while (randomInt2 == i || randomInt2 == randomInt);
                                    String selectedTopicAlt2 = adapter.getItem(randomInt2);

                                    mSelectedTopic.add(selectedTopic);
                                    mSelectedTopic.add(selectedTopicAlt1);
                                    mSelectedTopic.add(selectedTopicAlt2);
                                    /* */


                                    final Request request1 = new Request.Builder()
                                            .url(mTopics.get(mSelectedTopic.get(0)))
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
                                                        .newXPath().compile("//*[@id=\"mw-pages\"]/div/div/div/ul/li/a");
                                                NodeList test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);
                                                for (int i = 0; i < test.getLength(); i++) {
                                                    mArts.put(test.item(i).getTextContent().replaceFirst("Discusión:", ""), "https://es.wikipedia.org" + test.item(i).getAttributes().getNamedItem("href").getTextContent().replaceFirst("Discusi%C3%B3n:", ""));
                                                }
//                                                Log.d("amn",mArts.toString());
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
                                                    TagContainerLayout tagView = (TagContainerLayout) findViewById(R.id.result);
                                                    Set<String> keySet = mArts.keySet();
                                                    tagView.setTags(keySet.toArray(new String[keySet.size()]));
                                                    tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
                                                        @Override
                                                        public void onTagClick(int position, String text) {
                                                            WebViewDialog webViewDialog = new WebViewDialog(TopicSearcherActivity.this, "https://" + mQuestionLang + ".wikipedia.org/wiki/" + text);
                                                            webViewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            webViewDialog.show();
                                                        }

                                                        @Override
                                                        public void onTagLongClick(int position, String text) {

                                                        }

                                                        @Override
                                                        public void onTagCrossClick(int position) {

                                                        }
                                                    });
//                                                    TextView textView = (TextView) findViewById(R.id.result);
//                                                    textView.setText(mArts.keySet().toString());
//                                                    textView.setMovementMethod(new ScrollingMovementMethod());

//                                                    BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
                                                    submitButton.setEnabled(true);
                                                    submitButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent jump = new Intent(getBaseContext(), Round1Activity.class);
                                                            jump.putExtra("lang", mQuestionLang);
                                                            jump.putExtra("topic", mSelectedTopic);
                                                            jump.putExtra("arts", mArts);

                                                            jump.putExtra("artsAlt1", mArts);
                                                            jump.putExtra("artsAlt2", mArts);
                                                            jump.putExtra("supportList", new ArrayList<>(mArts.keySet()));

                                                            jump.putExtra("dateGameList", selectedItems);
                                                            jump.putExtra("choiceGameList", selectedOptions);
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
                } catch (IOException e)

                {
                    e.printStackTrace();
                }
            }

// replaceFirst("Article (du projet |de |d' )?", "").replaceFirst(" d'importance maximum‎").
// replaceFirst("sur (l'|le |la |les )", "")

// char[] tmp = str.toCharArray();
// tmp[0] = Character.toUpperCase(tmp[0]);
// str = String.valueOf(tmp)

//"//*[@id='mw-content-text']/div[3]/div/div[2]/div/div/a"

//"//*[@id='mw-pages']/div[2]/div/div/ul/li/a"
//Just remove Discussion: from all URLs!
// replaceFirst("Discussion:", "")
        });
        thread.start();
    }

    private void ukrajinska() {
        mSelectedTopic = new ArrayList<>();
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

        String base_url = "https://uk.wikipedia.org/wiki/%D0%92%D1%96%D0%BA%D1%96%D0%BF%D0%B5%D0%B4%D1%96%D1%8F:%D0%94%D0%BE%D0%B1%D1%80%D1%96_%D1%81%D1%82%D0%B0%D1%82%D1%82%D1%96";
        String base_url2 = "https://uk.wikipedia.org/wiki/%D0%92%D1%96%D0%BA%D1%96%D0%BF%D0%B5%D0%B4%D1%96%D1%8F:%D0%92%D0%B8%D0%B1%D1%80%D0%B0%D0%BD%D1%96_%D1%81%D1%82%D0%B0%D1%82%D1%82%D1%96";

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
//                            TextView textView = (TextView) findViewById(R.id.result);
//                            textView.setText(body[0]);
                            NodeList test = null;
                            Node uberschrift = null;
                            try {
                                Document doc = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder().parse(new InputSource(new StringReader(body[0])));
                                XPathExpression staticXPath = XPathFactory.newInstance()
                                        .newXPath().compile("//*[@id=\"mw-content-text\"]/div/table[2]/tr[2]/td/ul//li");

                                test = (NodeList) staticXPath.evaluate(doc, XPathConstants.NODESET);

                                for (int i = 0; i < test.getLength(); i++) {
                                    XPathExpression fett = XPathFactory.newInstance().newXPath().compile("b");
                                    uberschrift = (Node) fett.evaluate(test.item(i), XPathConstants.NODE);

                                    // 2
                                    if (uberschrift != null) {
                                        if (uberschrift.getTextContent().startsWith("Інш"))
                                            continue; // Skip 'others'
                                        mTopicsUntrimmedDe.add(uberschrift.getTextContent());

                                        mTopicsDe.add(uberschrift.getTextContent().substring(0, uberschrift.getTextContent().length() - 1).replaceFirst("^ ", ""));
                                        Log.i("Uberschrift", uberschrift.getTextContent().substring(0, uberschrift.getTextContent().length() - 1));
                                    }
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
                            List<String> tmp = new ArrayList<>(mTopicsDe);
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
                                    String selectedTopic = adapter.getItem(i);

                                    /* */
                                    int randomInt, randomInt2;
                                    Random random = new Random();
                                    do {
                                        randomInt = random.nextInt(adapter.getCount());
                                    } while (randomInt == i);
                                    String selectedTopicAlt1 = adapter.getItem(randomInt);

                                    do {
                                        randomInt2 = random.nextInt(adapter.getCount());
                                    } while (randomInt2 == i || randomInt2 == randomInt);
                                    String selectedTopicAlt2 = adapter.getItem(randomInt2);

                                    mSelectedTopic.add(selectedTopic);
                                    mSelectedTopic.add(selectedTopicAlt1);
                                    mSelectedTopic.add(selectedTopicAlt2);
                                    /* */


                                    //URL
                                    try {
                                        XPathExpression verweis = XPathFactory.newInstance().newXPath().compile("a");
                                        NodeList artikeln = (NodeList) verweis.evaluate(finalTest.item(mTopicsDe.lastIndexOf(selectedTopic)), XPathConstants.NODESET);

                                        for (int j = 0; j < artikeln.getLength(); j++) {
                                            Log.i("Artikel", artikeln.item(j).getTextContent());
                                            Log.i("URL", artikeln.item(j).getAttributes().getNamedItem("href").getTextContent());
                                            mArts.put(artikeln.item(j).getTextContent(), "https://uk.wikipedia.org" + artikeln.item(j).getAttributes().getNamedItem("href").getTextContent());
                                        }

                                        // 2
                                        Document doc2 = DocumentBuilderFactory.newInstance()
                                                .newDocumentBuilder().parse(new InputSource(new StringReader(body[1])));
                                        XPathExpression staticXPath2 = XPathFactory.newInstance()
                                                .newXPath().compile(String.format("//*[@id=\"mw-content-text\"]/div/table[2]/tr[2]/td/ul//li[b/text()=\"%s\"]/a", mTopicsUntrimmedDe.get(mTopicsDe.lastIndexOf(selectedTopic))));
                                        NodeList artikeln2 = (NodeList) staticXPath2.evaluate(doc2, XPathConstants.NODESET);
                                        if (artikeln2.getLength() > 0) {
                                            for (int k = 0; k < artikeln2.getLength(); k++) {
                                                mArts.put(artikeln2.item(k).getTextContent(), "https://uk.wikipedia.org" + artikeln2.item(k).getAttributes().getNamedItem("href").getTextContent());
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
                                    TagContainerLayout tagView = (TagContainerLayout) findViewById(R.id.result);
                                    Set<String> keySet = mArts.keySet();
                                    tagView.setTags(keySet.toArray(new String[keySet.size()]));
                                    tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
                                        @Override
                                        public void onTagClick(int position, String text) {
                                            WebViewDialog webViewDialog = new WebViewDialog(TopicSearcherActivity.this, "https://" + mQuestionLang + ".wikipedia.org/wiki/" + text);
                                            webViewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            webViewDialog.show();
                                        }

                                        @Override
                                        public void onTagLongClick(int position, String text) {

                                        }

                                        @Override
                                        public void onTagCrossClick(int position) {

                                        }
                                    });
//                                    TextView textView = (TextView) findViewById(R.id.result);
//                                    textView.setText(mArts.keySet().toString());
//                                    textView.setMovementMethod(new ScrollingMovementMethod());

//                                    BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.topic_submit_button);
                                    submitButton.setEnabled(true);
                                    submitButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent jump = new Intent(getBaseContext(), Round1Activity.class);
                                            jump.putExtra("lang", mQuestionLang);
                                            jump.putExtra("topic", mSelectedTopic);
                                            jump.putExtra("arts", mArts);

                                            jump.putExtra("artsAlt1", mArts);
                                            jump.putExtra("artsAlt2", mArts);

                                            jump.putExtra("supportList", new ArrayList<>(mArts.keySet()));
                                            jump.putExtra("dateGameList", selectedItems);
                                            jump.putExtra("choiceGameList", selectedOptions);

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
