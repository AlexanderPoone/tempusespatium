package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alex Poon on 1/16/2018.
 */

public class PuzzleFlagsFragment extends Fragment implements PuzzleFragmentInterface {

    private boolean mFirst;
    private int mCorrectCountryIndex;
    private String[] mCountries;
    private String[] mFlagURLs;

    private int mUserAnswerIndex;
    private OkHttpClient mClient;

    public PuzzleFlagsFragment() {

    }

    public PuzzleFlagsFragment(boolean first) {
        mFirst = first;
    }

//    String run(String url) throws IOException, NullPointerException {
//        mClient = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        Response response = mClient.newCall(request).execute();
//        return response.body().string();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flags, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        // TODO: Get html from Wikipedia
//        try {
//            String html = run("https://en.wikipedia.org/wiki/Gallery_of_sovereign_state_flags");
//            Log.wtf("WTF: ", html);
//            Document doc = DocumentBuilderFactory.newInstance()
//                    .newDocumentBuilder().parse(new InputSource(new StringReader(html)));
//
//            XPathExpression imgPathXPath = XPathFactory.newInstance()
//                    .newXPath().compile("//*[@id='mw-content-text']/div/table[1]/tr[1]/td[1]/table/tr[1]/td/a/img/@src");
//
//            String result = (String) imgPathXPath.evaluate(doc, XPathConstants.STRING);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        mCountries = new String[4];
        mFlagURLs = new String[4];
        Random random = new Random();
        mCorrectCountryIndex = random.nextInt();

        TextView cName = (TextView) view.findViewById(R.id.flag_country_name);
        cName.setText(mCountries[mCorrectCountryIndex]);

        ImageView flagA = (ImageView) view.findViewById(R.id.aImageView);
        ImageView flagB = (ImageView) view.findViewById(R.id.bImageView);
        ImageView flagC = (ImageView) view.findViewById(R.id.cImageView);
        ImageView flagD = (ImageView) view.findViewById(R.id.dImageView);

        Picasso.with(getContext()).load(mFlagURLs[0]).into(flagA);
        Picasso.with(getContext()).load(mFlagURLs[1]).into(flagB);
        Picasso.with(getContext()).load(mFlagURLs[2]).into(flagC);
        Picasso.with(getContext()).load(mFlagURLs[3]).into(flagD);
    }

    @Override
    public int[] revealAnswer() {
        switch (mCorrectCountryIndex) {
            case 0:
                RelativeLayout relA = (RelativeLayout) getView().findViewById(R.id.aRelLayout);
                relA.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
                break;
            case 1:
                RelativeLayout relB = (RelativeLayout) getView().findViewById(R.id.bRelLayout);
                relB.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
                break;
            case 2:
                RelativeLayout relC = (RelativeLayout) getView().findViewById(R.id.cRelLayout);
                relC.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
                break;
            case 3:
                RelativeLayout relD = (RelativeLayout) getView().findViewById(R.id.dRelLayout);
                relD.setBackground(getResources().getDrawable(R.drawable.rounded_choice_correct, null));
                break;
        }
        if (mUserAnswerIndex == mCorrectCountryIndex) {
            return new int[]{10, -10};
        } else {
            switch (mCorrectCountryIndex) {
                case 0:
                    RelativeLayout relA = (RelativeLayout) getView().findViewById(R.id.aRelLayout);
                    relA.setBackground(getResources().getDrawable(R.drawable.rounded_choice_incorrect, null));
                    break;
                case 1:
                    RelativeLayout relB = (RelativeLayout) getView().findViewById(R.id.bRelLayout);
                    relB.setBackground(getResources().getDrawable(R.drawable.rounded_choice_incorrect, null));
                    break;
                case 2:
                    RelativeLayout relC = (RelativeLayout) getView().findViewById(R.id.cRelLayout);
                    relC.setBackground(getResources().getDrawable(R.drawable.rounded_choice_incorrect, null));
                    break;
                case 3:
                    RelativeLayout relD = (RelativeLayout) getView().findViewById(R.id.dRelLayout);
                    relD.setBackground(getResources().getDrawable(R.drawable.rounded_choice_incorrect, null));
                    break;
            }
            return new int[]{10, -10};
        }
    }
}