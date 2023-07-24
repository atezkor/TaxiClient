package uz.yangilanish.client.ui.main;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import uz.yangilanish.client.R;
import uz.yangilanish.client.act.actions.AuthAction;
import uz.yangilanish.client.data.shared.AppPref;
import uz.yangilanish.client.events.LogoutBus;
import uz.yangilanish.client.events.MoveLocationBus;
import uz.yangilanish.client.events.TakeBookingBus;
import uz.yangilanish.client.events.TimerBus;
import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.models.Driver;
import uz.yangilanish.client.ui.components.CarMarker;
import uz.yangilanish.client.ui.components.UserMarker;
import uz.yangilanish.client.ui.view.MapPin;
import uz.yangilanish.client.ui.view.ViewLanguage;
import uz.yangilanish.client.utils.CacheData;
import uz.yangilanish.client.utils.LocationCalculator;
import uz.yangilanish.client.utils.NumberFormat;


public class MapActivity extends MapActivityAction {

    private MapView osmMap;

    private Button btnBooking;
    private Button cancelBooking;

    private AppCompatTextView addressTitle;
    private AppCompatTextView currentAddress;
    private AppCompatTextView bonus;

    private MapPin mapPin;

    private PulsatorLayout pulseLoader;
    private RelativeLayout searchCarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOsmMapDefaultConfig();
        setContentView(R.layout.activity_map);

        setContentActivity();
        startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService();
    }

    private void setContentActivity() {
        viewNoGps = findViewById(R.id.view_no_gps);
        viewNoInternet = findViewById(R.id.view_no_internet);
        progressBar = findViewById(R.id.rl_progress_bar);

        osmMap = findViewById(R.id.osm_map);
        addressTitle = findViewById(R.id.tv_address_title);
        currentAddress = findViewById(R.id.tv_current_address);
        bonus = findViewById(R.id.tv_client_bonus);
        ImageView menuBar = findViewById(R.id.menu_bar);

        mapPin = findViewById(R.id.map_pin);
        btnBooking = findViewById(R.id.btn_booking);
        cancelBooking = findViewById(R.id.btn_cancel_booking);
        ImageView btnShare = findViewById(R.id.iv_share);
        FloatingActionButton btnCurrentLocation = findViewById(R.id.btn_current_location);

        addressList = findViewById(R.id.rv_address_list);
        carModelList = findViewById(R.id.rv_car_model_list);

        pulseLoader = findViewById(R.id.pulse_loader);
        searchCarLayout = findViewById(R.id.rl_search_car);

        menuBar.setOnClickListener(this::menuClick);
        btnShare.setOnClickListener(v -> shareClick());
        btnBooking.setOnClickListener(v -> showCreateBookingDialog());
        cancelBooking.setOnClickListener(v -> showCancelBookingDialog());
        btnCurrentLocation.setOnClickListener(v -> currentLocationClick());

        initAddressList();
        initCarModelList();
        setScreenContent();
        setOsmMap();
    }

    private void setScreenContent() {
        String amount = NumberFormat.price((int) CacheData.getClient().getBonus());
        bonus.setText(String.format(getString(R.string.bonus_text), amount));
        addressTitle.setText(getString(R.string.current_address));
        btnBooking.setText(getString(R.string.btn_booking));
        cancelBooking.setText(getString(R.string.btn_cancel));
        viewNoInternet.setScreenContent();
        viewNoGps.setScreenContent();
    }

    private void setOsmMap() {
        osmMap.setMultiTouchControls(true);
        osmMap.setTileSource(TileSourceFactory.MAPNIK);
        osmMap.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        osmMap.setMinZoomLevel(12.0d);
        osmMap.setMaxZoomLevel(18.0d);

        GeoPoint currentLocation = new GeoPoint(CacheData.getCompany().getLatitude(), CacheData.getCompany().getLongitude());

        mapController = osmMap.getController();
        mapController.setZoom(16.5d);
        mapController.setCenter(currentLocation);

        osmMap.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                mapPin.showMarker();
                setAddressName(osmMap);

                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                mapPin.showMarker();
                setAddressName(osmMap);

                return false;
            }
        });
    }

    private void setAddressName(MapView osmMap) {
        double latitude = osmMap.getMapCenter().getLatitude();
        double longitude = osmMap.getMapCenter().getLongitude();

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        CacheData.setMapLocation(location);

        Address address = LocationCalculator.getAddressByLocation(latitude, longitude);
        if (address != null) {
            CacheData.setCurrentAddress(address);
            currentAddress.setText(address.getName());
        }
    }

    // Put drivers to map
    public void addMarkerToMap() {
        // Clear all marker
        osmMap.getOverlays().clear();

        // Draw client
        osmMap.getOverlays().add(new UserMarker(this, osmMap));

        // Set car to Marker
        for (Driver driver : CacheData.getOnlineDrivers()) {
            CarMarker marker = new CarMarker(this, osmMap, driver);
            osmMap.getOverlays().add(marker);
            osmMap.invalidate();
        }
    }

    private void currentLocationClick() {
        CacheData.setMapLocation(CacheData.getGpsLocation());
        if (CacheData.getMapLocation() != null && CacheData.getGpsLocation() != null) {
            Location location = CacheData.getGpsLocation();

            GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
            mapController.setZoom(16.5d);
            mapController.animateTo(currentLocation);
        }
    }

    @Override
    protected void startAnimation() {
        if (!pulseLoader.isStarted() /*&& new Date().getTime() > searchCarShowingTime*/) {
            mapPin.setVisibility(View.GONE);
            btnBooking.setVisibility(View.GONE);
            cancelBooking.setVisibility(View.VISIBLE);
            searchCarLayout.setVisibility(View.VISIBLE);
            carModelListAdapter.setDisable(true);

            pulseLoader.start();
        }

        // animateCars();
    }

    @Override
    protected void stopAnimation() {
        if (pulseLoader.isStarted()) {
            // searchCarShowingTime = new Date().getTime() + 5000;

            mapPin.setVisibility(View.VISIBLE);
            btnBooking.setVisibility(View.VISIBLE);
            cancelBooking.setVisibility(View.GONE);
            searchCarLayout.setVisibility(View.GONE);
            carModelListAdapter.setDisable(false);

            pulseLoader.stop();
            hideBookingDialog();
        }
    }

    public void menuClick(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.inflate(R.menu.lang_menu);
        menu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.lang_uz) {
                changeLanguage(ViewLanguage.LANG_UZ);
                return true;
            }

            if (menuItem.getItemId() == R.id.lang_ru) {
                changeLanguage(ViewLanguage.LANG_RU);
                return true;
            }

            AuthAction.logout();
            openLoginActivity();
            closeCurrentActivity();
            return false;
        });

        menu.show();
    }

    private void changeLanguage(String lang) {
        AppPref.setLanguage(lang);
        setDefaultLocale();
        setScreenContent();

        carModelListAdapter.notifyItemRangeChanged(0, carModelListAdapter.getItemCount());
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TimerBus bus) {
        addMarkerToMap();
        mapPin.hideMarker();

        if (!internetIsEnabled()) {
            viewNoInternet.setVisibility(View.VISIBLE);
            return;
        }

        if (!gpsIsEnabled()) {
            viewNoGps.setVisibility(View.VISIBLE);
            return;
        }

        viewNoInternet.setVisibility(View.GONE);
        viewNoGps.setVisibility(View.GONE);
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MoveLocationBus bus) {
        if (mapController != null) {
            Location location = bus.getLocation();
            GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

            mapController.setZoom(16.5d);
            mapController.setCenter(currentLocation);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TakeBookingBus bus) {
        CacheData.setDriver(bus.getDriver());

        stopAnimation();
        openTaximeterActivity();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutBus bus) {
        AuthAction.logout();
        openSplashActivity();
    }
}