package hk.edu.cuhk.cse.tempusespatium;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Alex Poon on 1/18/2018.
 */

public class MapAnswerPopup implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater mInflater;

    public MapAnswerPopup(LayoutInflater layoutInflater) {
        mInflater = layoutInflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
