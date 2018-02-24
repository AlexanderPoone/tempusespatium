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
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygon;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;

/**
 * Created by Alex Poon on 10/17/2017.
 */


//TODO: Handle music pause when game is paused.
public class PuzzleMapFragment extends Fragment implements OnMapReadyCallback, PuzzleFragmentInterface {

    private boolean mFirst;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private OkHttpClient mClient;

    private String mCountry, mAnthem, mAnthemUrl;

    private Marker mUserMarker = null;
    private Marker mActualMarker = null;
    private LatLng mActualCoords = null;
    private Circle mAcceptanceRange = null;

    @Override
    public boolean isRevealed() {
        return isRevealed;
    }

    private boolean isRevealed = false;

    private static MediaPlayer mMediaPlayer;

    public PuzzleMapFragment() {

    }

    public PuzzleMapFragment(boolean first, String country, String anthem, String anthemUrl) {
        mFirst = first;
        mCountry = country;
        mAnthem = anthem;
        mAnthemUrl = anthemUrl;
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

                mMediaPlayer.setDataSource(mAnthemUrl);
//                mMediaPlayer.setDataSource(MenuActivity.Anthems.valueOf("Israel".toUpperCase()).url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.prepareAsync();
        }
//        else {
//            mMediaPlayer.start();
//        }
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Round1Activity) getActivity()).callReveal(mFirst);
            }
        });

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
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);

//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0.0, 0.0), 0.0f);
//        mMap.moveCamera(cameraUpdate);
    }

    //TODO:GooGLE Maps label
    @Override
    public int[] revealAnswer() {
        isRevealed = true;

//        mMediaPlayer.stop();
//        mMediaPlayer.release();

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mUiSettings.setAllGesturesEnabled(false);

        // Ask if the location user pointer belongs to the right country
        Geocoder geoCoder = new Geocoder(getContext(), new Locale("en", "gb"));
        List<Address> matches;
        String country = "";
        if (mUserMarker != null) {
            try {
                matches = geoCoder.getFromLocation(mUserMarker.getPosition().latitude, mUserMarker.getPosition().longitude, 1);
                Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
                country = bestMatch.getCountryName();
                //
                if (country.equals("Macedonia (FYROM)")) country = "FYROM";
                //
                TextView debugUserSelects = (TextView) getView().findViewById(R.id.debug_user_selects);
                debugUserSelects.setText(String.format("You chose:\n%s", country));
                Log.i("Country", country);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // Display the country boundaries
        // TODO: Maybe https://developers.google.com/maps/documentation/javascript/examples/layer-data-simple
        String json = null;
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.world, getContext());
            for (GeoJsonFeature feature : layer.getFeatures()) {
                String featureName = feature.getProperty("name");
//                http://googlemaps.github.io/android-maps-utils/javadoc/com/google/maps/android/geojson/GeoJsonFeature.html
                if (featureName.equalsIgnoreCase(mCountry) && feature.hasGeometry()) {
//                    List<LatLng> coords = null;
                    if (feature.getGeometry().getGeometryType().equals("Polygon")) {
//                        coords=((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0);
                        mMap.addPolygon(new PolygonOptions()
                                .addAll(((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0))
                                .fillColor(getResources()
                                        .getColor(R.color.MidnightBlue, null) + (0x77 << 24)));
//                        double minLat = Double.MAX_VALUE, minLng = Double.MAX_VALUE;
//                        double maxLat = Double.MIN_VALUE, maxLng = Double.MIN_VALUE;
//                        for (int i = 0; i < ((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).size(); i++) {
//                            if (((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i).latitude < minLat) {
//                                minLat = ((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i).latitude;
//                            }
//                            if (((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i).latitude > maxLat) {
//                                maxLat = ((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i).latitude;
//                            }
//                            if (((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i).longitude < minLng) {
//                                minLng = ((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i).longitude;
//                            }
//                            if (((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i).longitude > maxLng) {
//                                maxLng = ((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i).longitude;
//                            }
//                        }
//                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng((minLat + maxLat) / 2, (minLng + maxLng) / 2), 3.5f);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (int i = 0; i < ((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).size(); i++) {
                            builder.include(((GeoJsonPolygon) feature.getGeometry()).getCoordinates().get(0).get(i));
                        }
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 10);
                        mMap.animateCamera(cameraUpdate);
//                        mMap.setLatLngBoundsForCameraTarget(
//                                new LatLngBounds(((GeoJsonPolygon) feature.getGeometry()).getOuterBoundaryCoordinates().get(0),
//                                        ((GeoJsonPolygon) feature.getGeometry()).getOuterBoundaryCoordinates().get(1))
//                        );
                    } else if (feature.getGeometry().getGeometryType().equals("MultiPolygon")) {
//                        coords = ((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(0).getCoordinates().get(0);
                        int len = ((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().size();
                        double minLat = Double.MAX_VALUE, minLng = Double.MAX_VALUE;
                        double maxLat = Double.MIN_VALUE, maxLng = Double.MIN_VALUE;
                        for (int i = 0; i < len; i++) {
                            mMap.addPolygon(new PolygonOptions()
                                    .addAll(((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0))
                                    .fillColor(getResources()
                                            .getColor(R.color.MidnightBlue, null) + (0x77 << 24)));
                            for (int j = 0; j < ((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).size(); j++) {
                                if (((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).get(j).latitude < minLat) {
                                    minLat = ((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).get(j).latitude;
                                }
                                if (((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).get(j).latitude > maxLat) {
                                    maxLat = ((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).get(j).latitude;
                                }
                                if (((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).get(j).longitude < minLng) {
                                    minLng = ((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).get(j).longitude;
                                }
                                if (((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).get(j).longitude > maxLng) {
                                    maxLng = ((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(i).getCoordinates().get(0).get(j).longitude;
                                }
                            }
//                            mMap.setLatLngBoundsForCameraTarget(
//                                    new LatLngBounds(((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(0).getOuterBoundaryCoordinates().get(0),
//                                            ((GeoJsonMultiPolygon) feature.getGeometry()).getPolygons().get(0).getOuterBoundaryCoordinates().get(1))
//                            );
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng((minLat + maxLat) / 2, (minLng + maxLng) / 2), 3.5f);
                            mMap.moveCamera(cameraUpdate);
                        }
                    }

//                    Log.i("Ha!", feature.getGeometry().getGeometryType());
                }
//                mMap.addPolygon(new PolygonOptions()
//                        .add()
//                        .fillColor(getResources()
//                        .getColor(R.color.MidnightBlue, null)));
            }
//            layer.addLayerToMap();
//            Log.e("Testing...", layer.getFeatures().iterator().next().getPolygonOptions().toString());


//            InputStream is = getActivity().getAssets().open("world.countries.geo.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//
//            JSONObject jsonObject = new JSONObject(json);
//            JSONArray jsonArray = jsonObject.getJSONArray("features");
//            Random random = new Random();
//            JSONObject randomCountry = jsonArray.getJSONObject(random.nextInt(jsonArray.length()));
//            String cname = randomCountry.getJSONObject("properties").getString("name");
//            String geometryType = randomCountry.getJSONObject("geometry").getString("type");
//            JSONArray areas = randomCountry.getJSONObject("geometry").getJSONArray("coordinates");
//
//            if (geometryType.equals("Polygon")) {
//                PolygonOptions polygonOptions = new PolygonOptions()
//                        .fillColor(0x77A4C639)
//                        .strokeColor(R.color.AndroidGreen);
//                for (int i = 0; i < areas.length(); i++) {
//                    List<LatLng> regions = new ArrayList<>();
//                    JSONArray coordinates = areas.getJSONArray(i);
//                    for (int j = 0; j < coordinates.length(); j++) {
//                        regions.add(new LatLng(coordinates.getJSONArray(j).getDouble(0),
//                                coordinates.getJSONArray(j).getDouble(1)));
//                    }
//                    if (i == 0) polygonOptions.addAll(regions);
//                    else polygonOptions.addHole(regions);
//                }
//                mMap.addPolygon(polygonOptions);
//            } else if (geometryType.equals("MultiPolygon")) {
//                //TODO: United States name is not right
//                for (int i = 0; i < areas.length(); i++) {
//                    PolygonOptions polygonOptions = new PolygonOptions()
//                            .fillColor(0x77A4C639)
//                            .strokeColor(R.color.AndroidGreen);
//                    List<LatLng> regions = new ArrayList<>();
//                    JSONArray coordinates = areas.getJSONArray(i);
//                    for (int j = 0; j < coordinates.length(); j++) {
//                        regions.add(new LatLng(coordinates.getJSONArray(j).getDouble(0),
//                                coordinates.getJSONArray(j).getDouble(1)));
//                    }
//                    polygonOptions.addAll(regions);
//                    mMap.addPolygon(polygonOptions);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//        // Centroid ?
//        // TODO: Remove placeholder
//        mActualCoords = new LatLng(48.804404, 2.123162);
//        // TODO: Remove placeholder
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mActualCoords, 5.5f);
//        mMap.moveCamera(cameraUpdate);
//
//        mActualMarker = mMap.addMarker(new MarkerOptions()
//                .position(mActualCoords)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.correct_pointer)));
//
//
//        PatternItem DASH = new Dash(50);
//        PatternItem GAP = new Gap(10);
//        List<PatternItem> PATTERN_POLYLINE_DASH = Arrays.asList(GAP, DASH);
//        mAcceptanceRange = mMap.addCircle(new CircleOptions().center(mActualCoords)
//                .fillColor(0x77A4C639)
//                .strokeColor(getResources().getColor(R.color.ForestGreen, null))
//                .strokePattern(PATTERN_POLYLINE_DASH)
//                .radius(ACCEPTANCE_RADIUS_METRES));


        // Return score change
        if (mUserMarker != null) {
            if (mCountry.equalsIgnoreCase(country)) {
                if (mFirst) {
                    return new int[]{20, 0};
                } else {
                    return new int[]{0, 20};
                }
            } else {
                if (mFirst) {
                    return new int[]{-10, 0};
                } else {
                    return new int[]{0, -10};
                }
            }
        } else {
            if (mFirst) {
                return new int[]{-20, 0};
            } else {
                return new int[]{0, -20};
            }
        }
    }

    @Override
    public void disableControls() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
