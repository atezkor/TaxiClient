package uz.yangilanish.client.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import uz.yangilanish.client.R;
import uz.yangilanish.client.models.Driver;


public class CarMarker extends Marker {

    @SuppressLint("UseCompatLoadingForDrawables")
    public CarMarker(Context context, MapView mapView, Driver driver) {
        super(mapView);

        this.mInfoWindow = null;
        this.setIcon(context.getResources().getDrawable(R.drawable.im_map_car));

        GeoPoint startPoint = new GeoPoint(driver.getLatitude(), driver.getLongitude());
        this.setPosition(startPoint);
        this.setRotation(-driver.getBearing());
    }
}
