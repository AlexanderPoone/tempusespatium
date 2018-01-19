package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

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
 * Created by Alex Poon on 10/17/2017.
 */

public class PuzzleMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private OkHttpClient mClient;

    private Marker mMarker = null;

    String run(String url) throws IOException, NullPointerException {
        mClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // TODO: Get html from Wikipedia
        // TODO: https://raw.githubusercontent.com/matej-pavla/Google-Maps-Examples/master/BoundariesExample/geojsons/world.countries.geo.json

        try {
            String html = run("https://en.wikipedia.org/wiki/Gallery_of_sovereign_state_flags");
            Log.wtf("WTF: ", html);
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new InputSource(new StringReader(html)));

            XPathExpression xpath = XPathFactory.newInstance()
                    .newXPath().compile("//*[@id='mw-content-text']/div/table[1]/tr[1]/td[1]/table/tr[1]/td/a/img/@src");

            String result = (String) xpath.evaluate(doc, XPathConstants.STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_anthem, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BootstrapButton submitButton = (BootstrapButton) view.findViewById(R.id.submit0);

        BootstrapButton clearButton = (BootstrapButton) view.findViewById(R.id.clear0);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarker.remove();
                mMarker = null;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMarker != null) {
                    mMarker.remove();
                }
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (!mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                getContext(), R.raw.minimalist))) {
            Log.e(PuzzleMapFragment.class.getSimpleName(), "Style parsing failed.");
        }
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0.0, 0.0), 0.0f);
//        mMap.moveCamera(cameraUpdate);
    }

    public void revealAnswer() {
        mMap.addPolygon(new PolygonOptions()
                .add()
                .strokeColor(R.color.AndroidGreen)
                .fillColor(R.color.YellowGreen));
    }
}
