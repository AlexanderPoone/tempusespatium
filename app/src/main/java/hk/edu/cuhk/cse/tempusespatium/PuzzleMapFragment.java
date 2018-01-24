package hk.edu.cuhk.cse.tempusespatium;

import android.location.Address;
import android.location.Geocoder;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static hk.edu.cuhk.cse.tempusespatium.Constants.ACCEPTANCE_RADIUS_METRES;

/**
 * Created by Alex Poon on 10/17/2017.
 */

public class PuzzleMapFragment extends Fragment implements OnMapReadyCallback, PuzzleFragmentInterface {

    private boolean mFirst;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private OkHttpClient mClient;

    private Marker mUserMarker = null;
    private Marker mActualMarker = null;
    private LatLng mActualCoords = null;
    private Circle mAcceptanceRange = null;

    private static MediaPlayer mMediaPlayer;

    public PuzzleMapFragment() {

    }

    public PuzzleMapFragment(boolean first) {
        mFirst = first;
    }

    String run(String url) throws IOException, NullPointerException {
        mClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    private void playSong() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            try {
                mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA).build());
                mMediaPlayer.setDataSource(MenuActivity.Anthems.valueOf("Israel".toUpperCase()).url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.prepareAsync();
        } else {
            mMediaPlayer.start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playSong();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BootstrapButton submitButton = (BootstrapButton) view.findViewById(R.id.submit0);

        BootstrapButton clearButton = (BootstrapButton) view.findViewById(R.id.clear0);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserMarker != null) mUserMarker.remove();
                mUserMarker = null;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mUserMarker != null) {
                    mUserMarker.remove();
                }
                mUserMarker = mMap.addMarker(new MarkerOptions()
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.rocket_pointer))
                        .position(latLng));
            }
        });
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (!mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                getContext(), R.raw.minimalist))) {
            Log.e(PuzzleMapFragment.class.getSimpleName(), "Style parsing failed.");
        }
        mUiSettings.setMapToolbarEnabled(false);

//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0.0, 0.0), 0.0f);
//        mMap.moveCamera(cameraUpdate);
    }

    @Override
    public int[] revealAnswer() {
        mMediaPlayer.stop();
        mMediaPlayer.release();

        // Ask if the location user pointer belongs to the right country
        Geocoder geoCoder = new Geocoder(getContext());
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(mUserMarker.getPosition().latitude, mUserMarker.getPosition().longitude, 1);
            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
            bestMatch.getCountryName();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Display the country boundaries
        // TODO: Maybe https://developers.google.com/maps/documentation/javascript/examples/layer-data-simple
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("world.countries.geo.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("features");
            Random random = new Random();
            JSONObject randomCountry = jsonArray.getJSONObject(random.nextInt(jsonArray.length()));
            String cname = randomCountry.getJSONObject("properties").getString("name");
            String geometryType = randomCountry.getJSONObject("geometry").getString("type");
            JSONArray areas = randomCountry.getJSONObject("geometry").getJSONArray("coordinates");

            if (geometryType.equals("Polygon")) {
                PolygonOptions polygonOptions = new PolygonOptions()
                        .fillColor(0x77A4C639)
                        .strokeColor(R.color.AndroidGreen);
                for (int i = 0; i < areas.length(); i++) {
                    List<LatLng> regions = new ArrayList<>();
                    JSONArray coordinates = areas.getJSONArray(i);
                    for (int j = 0; j < coordinates.length(); j++) {
                        regions.add(new LatLng(coordinates.getJSONArray(j).getDouble(0),
                                coordinates.getJSONArray(j).getDouble(1)));
                    }
                    if (i == 0) polygonOptions.addAll(regions);
                    else polygonOptions.addHole(regions);
                }
                mMap.addPolygon(polygonOptions);
            } else if (geometryType.equals("MultiPolygon")) {
                //TODO: United States name is not right
                for (int i = 0; i < areas.length(); i++) {
                    PolygonOptions polygonOptions = new PolygonOptions()
                            .fillColor(0x77A4C639)
                            .strokeColor(R.color.AndroidGreen);
                    List<LatLng> regions = new ArrayList<>();
                    JSONArray coordinates = areas.getJSONArray(i);
                    for (int j = 0; j < coordinates.length(); j++) {
                        regions.add(new LatLng(coordinates.getJSONArray(j).getDouble(0),
                                coordinates.getJSONArray(j).getDouble(1)));
                    }
                    polygonOptions.addAll(regions);
                    mMap.addPolygon(polygonOptions);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Centroid ?
        // TODO: Remove placeholder
        mActualCoords = new LatLng(48.804404, 2.123162);
        // TODO: Remove placeholder
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mActualCoords, 5.5f);
        mMap.moveCamera(cameraUpdate);

        mActualMarker = mMap.addMarker(new MarkerOptions()
                .position(mActualCoords)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.correct_pointer)));


        PatternItem DASH = new Dash(50);
        PatternItem GAP = new Gap(10);
        List<PatternItem> PATTERN_POLYLINE_DASH = Arrays.asList(GAP, DASH);
        mAcceptanceRange = mMap.addCircle(new CircleOptions().center(mActualCoords)
                .fillColor(0x77A4C639)
                .strokeColor(getResources().getColor(R.color.ForestGreen, null))
                .strokePattern(PATTERN_POLYLINE_DASH)
                .radius(ACCEPTANCE_RADIUS_METRES));


        // Return score change
        if (mFirst) {
            return new int[]{10, 0};
        } else {
            return new int[]{0, 10};
        }
    }

    @Override
    public void disableControls() {

    }
}
