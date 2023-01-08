package uz.yangilanish.client.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import uz.yangilanish.client.R;
import uz.yangilanish.client.utils.CacheData;


public class UserMarker extends Marker {

    @SuppressLint("UseCompatLoadingForDrawables")
    public UserMarker(Context context, MapView mapView) {
        super(mapView);

        this.mInfoWindow = null;
        this.setIcon(context.getResources().getDrawable(R.drawable.ic_user_location_pin));

        Location user = CacheData.getGpsLocation();
        if (user == null) {
            return;
        }

        setPosition(new GeoPoint(user.getLatitude(), user.getLongitude()));
        setRotation(-user.getBearing());
    }
}
