package uz.yangilanish.client.ui.taximeter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import uz.yangilanish.client.BuildConfig;
import uz.yangilanish.client.models.Driver;
import uz.yangilanish.client.utils.CacheData;


public class UpdateRoadTask extends AsyncTask<Object, Void, Road> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;

    private final TrackLoadListener listener;

    private final Driver driver;

    public UpdateRoadTask(Context context, Driver driver, TrackLoadListener listener) {
        super();
        this.context = context;
        this.listener = listener;
        this.driver = driver;
    }

    @Override
    public Road doInBackground(Object... params) {
        RoadManager roadManager = new OSRMRoadManager(context, BuildConfig.APPLICATION_ID);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();

        if (driver != null) {
            Location location = CacheData.getMapLocation();
            GeoPoint startPoint = new GeoPoint(driver.getLatitude(), driver.getLongitude());
            GeoPoint endPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

            waypoints.add(startPoint);
            waypoints.add(endPoint);
        }

        return roadManager.getRoad(waypoints);
    }

    public void onPostExecute(Road result) {
        if (result != null) {
            listener.onTrackLoad(result);
        }
    }
}
