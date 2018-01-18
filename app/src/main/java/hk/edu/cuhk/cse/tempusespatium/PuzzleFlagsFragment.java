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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

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

public class PuzzleFlagsFragment extends Fragment {

    private String mCountry;
    private String mFlagURL;
    private OkHttpClient mClient;


    PuzzleMapFragment(String country, String flagURL) {
        mCountry=country;
        mFlagURL=flagURL;
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
        return super.onCreateView(inflater, container, savedInstanceState);
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

        TextView cName = (TextView) view.findViewById(R.id.flag_country_name);
        cName.setText(mCountry);

        ImageView flagA = (ImageView) view.findViewById(R.id.aImageView);
        ImageView flagB = (ImageView) view.findViewById(R.id.bImageView);
        ImageView flagC = (ImageView) view.findViewById(R.id.cImageView);
        ImageView flagD = (ImageView) view.findViewById(R.id.dImageView);

        Picasso.with(getContext()).load(mFlagURL).into(flagA);
        Picasso.with(getContext()).load(mFlagURL).into(flagB);
        Picasso.with(getContext()).load(mFlagURL).into(flagC);
        Picasso.with(getContext()).load(mFlagURL).into(flagD);

//        "Flag of  ".replace("^Flag of  ", "");

    }
}
