package uz.yangilanish.client.ui.taximeter;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import uz.yangilanish.client.R;
import uz.yangilanish.client.databinding.ActivityTaximeterBinding;
import uz.yangilanish.client.events.CloseActivityBus;
import uz.yangilanish.client.events.TimerBus;
import uz.yangilanish.client.models.Booking;
import uz.yangilanish.client.models.Client;
import uz.yangilanish.client.models.Driver;
import uz.yangilanish.client.ui.components.CarMarker;
import uz.yangilanish.client.ui.components.UserMarker;
import uz.yangilanish.client.utils.CacheData;
import uz.yangilanish.client.utils.NumberFormat;


public class TaximeterActivity extends TaximeterActivityAction {

    private ActivityTaximeterBinding binding;

    private MapView osmMap;
    private Polyline polyline;
    private IMapController mapController;

    private int taximeterPrice = 0;
    private boolean notifiedTaximeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), CacheData.getDefaultPreferences());
        binding = ActivityTaximeterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setDefaultLocale();
        EventBus.getDefault().register(this);
        setContentActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new CloseActivityBus());
    }

    private void setContentActivity() {
        osmMap = binding.osmMap;

        progressBar = binding.rlProgressBar;
        spinKitLoader = binding.spinKitView;

        Driver driver = CacheData.getDriver();
        binding.btnCurrentLocation.setOnClickListener(this::currentLocationClick);
        binding.btnCancelBooking.setOnClickListener(v -> showCancelBookingDialog());
        binding.btnCall.setOnClickListener(v -> callDriver(driver));

        notifiedTaximeter = false;
        taximeterPrice = driver.getTaximeterPayment();
        Booking booking = CacheData.getBooking();
        if (booking == null || booking.getStatus() != Booking.STATUS_IN_PLACE) {
            hideTaximeter();
        } else {
            showTaximeter();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Driver.Tariff tariff = CacheData.getTariffList().stream()
                    .filter(t -> t.getId() == driver.getTariffId())
                    .findAny()
                    .orElse(null);
            driver.setTariff(tariff);
        }

        setScreenContent(driver);
        setOsmMap(driver);
    }

    private void setScreenContent(Driver driver) {
        Client client = CacheData.getClient();
        String bonus = NumberFormat.price((int) client.getBonus());
        binding.clientBonus.setText(String.format(getString(R.string.bonus_text), bonus));

        binding.tvPayment.setText(NumberFormat.price(driver.getTaximeterPayment()));
        binding.bookingAddress.setText(CacheData.getCurrentAddress().getName());
        Driver.Car car = driver.getCar();
        binding.carModel.setText(car.getModel());
        binding.carNumber.setText(car.getNumber());
        binding.smsCode.setText(String.valueOf(client.getBonusSmsCode()));
    }

    private void setOsmMap(Driver driver) {
        double latitude = CacheData.getCompany().getLatitude(), longitude = CacheData.getCompany().getLongitude();
        if (driver != null) {
            latitude = driver.getLatitude();
            longitude = driver.getLongitude();
        }

        osmMap.setMultiTouchControls(true);
        osmMap.setTileSource(TileSourceFactory.MAPNIK);
        osmMap.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        osmMap.setMinZoomLevel(10.0d);
        osmMap.setMaxZoomLevel(18.0d);

        GeoPoint currentLocation = new GeoPoint(latitude, longitude);

        mapController = osmMap.getController();
        mapController.setZoom(16.5d);
        mapController.setCenter(currentLocation);

        osmMap.addOnFirstLayoutListener((v, left, top, right, bottom) -> { // TODO Check calling count
            new UpdateRoadTask(getApplicationContext(), driver, result -> {
                polyline = RoadManager.buildRoadOverlay(result);
                polyline.getOutlinePaint().setStrokeWidth(10.0f);
                osmMap.getOverlays().add(polyline);
                osmMap.invalidate();
            }).execute("routing");
        });
    }

    public void addMarkerToMap() {
        osmMap.getOverlayManager().clear();

        Booking booking = CacheData.getBooking();
        if (!(booking == null || booking.getStatus() == Booking.STATUS_IN_PLACE || polyline == null)) {
            UserMarker marker = new UserMarker(this, osmMap);
            osmMap.getOverlays().add(marker);
            osmMap.getOverlays().add(polyline);
        }

        Driver driver = CacheData.getDriver();
        if (driver != null) {
            CarMarker marker = new CarMarker(this, osmMap, driver);
            osmMap.getOverlays().add(marker);
        }

        osmMap.invalidate();
    }

    private void showTaximeter() {
        binding.priceLayout.setVisibility(View.VISIBLE);
        binding.viewDivider.setVisibility(View.VISIBLE);
        binding.tvFareTitle.setVisibility(View.VISIBLE);
        binding.llSmsCode.setVisibility(View.VISIBLE);
    }

    private void hideTaximeter() {
        binding.priceLayout.setVisibility(View.GONE);
        binding.viewDivider.setVisibility(View.GONE);
        binding.tvFareTitle.setVisibility(View.GONE);
        binding.llSmsCode.setVisibility(View.GONE);
    }

    public void currentLocationClick(View v) {
        Driver driver = CacheData.getDriver();
        GeoPoint geoPoint = new GeoPoint(driver.getLatitude(), driver.getLongitude());
        mapController.animateTo(geoPoint);
        mapController.setZoom(16.5d);
    }

    private void setTaximeterData(Booking booking) {
        if (booking.getStatus() < Booking.STATUS_IN_PLACE) {
            hideTaximeter();
            return;
        }

        showTaximeter();

        int payment = CacheData.getDriver().getTaximeterPayment();
        binding.tvPayment.setText(NumberFormat.price(payment));
        if (payment != taximeterPrice) {
            binding.taximeterPriceRound.setText(NumberFormat.additionalPayment((payment - taximeterPrice)));
            taximeterPrice = payment;
        }
    }

    /* Events */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TimerBus bus) {
        Booking booking = CacheData.getBooking();
        if (booking == null || booking.getStatus() == Booking.STATUS_FINISHED) {
            showFinishBookingDialog(CacheData.getDriver().getTaximeterPayment());
            EventBus.getDefault().unregister(this);
            return;
        }

        if (!notifiedTaximeter) {
            notifiedTaximeter = true;
            notifyAboutTaximeter();
        }

        setTaximeterData(booking);
        addMarkerToMap();
    }
}