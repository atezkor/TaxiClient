package uz.yangilanish.client.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.yangilanish.client.BuildConfig;
import uz.yangilanish.client.R;
import uz.yangilanish.client.data.api.ApiBuilder;
import uz.yangilanish.client.data.dto.booking.CancelBookingRequest;
import uz.yangilanish.client.data.dto.booking.CancelBookingResponse;
import uz.yangilanish.client.data.dto.booking.CreateBookingRequest;
import uz.yangilanish.client.data.dto.booking.CreateBookingResponse;
import uz.yangilanish.client.data.dto.response.BadResponse;
import uz.yangilanish.client.events.StartSocketServiceBus;
import uz.yangilanish.client.events.StopSocketServiceBus;
import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.models.CarModel;
import uz.yangilanish.client.services.MainService;
import uz.yangilanish.client.ui.layout.LayoutActivity;
import uz.yangilanish.client.ui.main.components.AddressListAdapter;
import uz.yangilanish.client.ui.main.components.CarModelListAdapter;
import uz.yangilanish.client.ui.main.components.OnItemClickListener;
import uz.yangilanish.client.utils.CacheData;
import uz.yangilanish.client.utils.NumberFormat;


public class MapActivityAction extends LayoutActivity {

    private AlertDialog bookingDialog;
    private AlertDialog cancelBookingDialog;

//    protected long searchCarShowingTime = new Date().getTime();

    protected IMapController mapController;

    protected RecyclerView addressList, carModelList;

    protected CarModelListAdapter carModelListAdapter;
    protected AddressListAdapter addressListAdapter;

    private Intent serviceIntent;

    protected void setOsmMapDefaultConfig() {
        Configuration.getInstance().load(getApplicationContext(), CacheData.getDefaultPreferences());
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    }

    protected void initAddressList() {
        if (CacheData.getClient().getLastBookingAddresses().size() == 0) {
            addressList.setVisibility(View.GONE);
        }

        OnItemClickListener adapterListener = new OnItemClickListener() {
            @Override
            public void onItemClick(Address address) {
                GeoPoint currentLocation = new GeoPoint(address.getLatitude(), address.getLongitude());
                mapController.setCenter(currentLocation);

                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(address.getLatitude());
                location.setLongitude(address.getLongitude());

                CacheData.setMapLocation(location);
                showCreateBookingDialog();
            }
        };

        addressListAdapter = new AddressListAdapter(this, adapterListener);
        addressList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addressList.setAdapter(addressListAdapter);
    }

    protected void initCarModelList() {
        carModelList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        carModelListAdapter = new CarModelListAdapter(this, null);
        carModelList.setAdapter(carModelListAdapter);
    }

    protected void startAnimation() {
    }

    protected void stopAnimation() {
    }

    /* Dialogs */
    protected void showCreateBookingDialog() {
        if (bookingDialog == null || !bookingDialog.isShowing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_booking, null);

            List<CarModel> carModelList = CacheData.getCarModelList();
            CarModel carModel = carModelList.get(0);

            String modelName = getString(R.string.any_model);
            int price = carModel.getPrice();
            CarModel model = carModelListAdapter.getItem();
            if (model != null) {
                modelName = model.getName();
                price = model.getPrice();
            }

            Address currentAddress = CacheData.getCurrentAddress();
            price += currentAddress.getAdditionalPayment();
            String payment = String.format(getString(R.string.initial_payment), NumberFormat.price(price));

            ((AppCompatTextView) dialogView.findViewById(R.id.tv_address)).setText(currentAddress.getName());
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_car_model)).setText(modelName);
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_payment)).setText(payment);
            dialogView.findViewById(R.id.iv_btn_close).setOnClickListener(v -> bookingDialog.dismiss());
            dialogView.findViewById(R.id.tv_btn_yes).setOnClickListener(v -> {
                bookingDialog.dismiss();
                createBooking(carModel.getId());
            });

            dialogBuilder.setView(dialogView);
            bookingDialog = dialogBuilder.create();
            bookingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            bookingDialog.show();
        }
    }

    protected void showCancelBookingDialog() {
        AlertDialog alertDialog = cancelBookingDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            AlertDialog.Builder cancelBookingDialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cancel_booking, null);


            Address address = CacheData.getCurrentAddress();
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_address_name)).setText(address.getName());
            CarModel model = carModelListAdapter.getItem();
            if (model != null) {
                ((AppCompatTextView) dialogView.findViewById(R.id.tv_car_model)).setText(model.getName());
            }

            dialogView.findViewById(R.id.trip_payment).setVisibility(View.GONE);
            dialogView.findViewById(R.id.btn_close).setOnClickListener(v -> cancelBookingDialog.dismiss());
            AppCompatButton btnYes = dialogView.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(v -> {
                cancelBookingDialog.dismiss();
                cancelBooking();
            });

            cancelBookingDialogBuilder.setView(dialogView);
            cancelBookingDialog = cancelBookingDialogBuilder.create();
            cancelBookingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancelBookingDialog.setCancelable(false);
            cancelBookingDialog.show();
        }
    }

    protected void hideBookingDialog() {
        if (bookingDialog != null && bookingDialog.isShowing()) {
            bookingDialog.dismiss();
        }
    }

    private void createBooking(final int modelId) {
        if (!(internetIsEnabled() && gpsIsEnabled()))
            return;

        showLoading();

        CreateBookingRequest createBooking = new CreateBookingRequest(modelId);
        ApiBuilder.api().createBooking(createBooking).enqueue(new Callback<CreateBookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateBookingResponse> call, @NonNull Response<CreateBookingResponse> response) {
                hideLoading();

                CreateBookingResponse body = response.body();
                if (!response.isSuccessful() || body == null) {
                    assert response.errorBody() != null;
                    showErrorDialog(BadResponse.getInstance(response.errorBody()).getMsg());
                    return;
                }

                CacheData.setBooking(body.getData());

                startAnimation();
            }

            @Override
            public void onFailure(@NonNull Call<CreateBookingResponse> call, @NonNull Throwable t) {
                hideLoading();
                showServerErrorDialog();
            }
        });
    }

    protected void cancelBooking() {
        if (!internetIsEnabled() || !gpsIsEnabled())
            return;

        showLoading();

        CancelBookingRequest request = new CancelBookingRequest();
        ApiBuilder.api().cancelBooking(request).enqueue(new Callback<CancelBookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<CancelBookingResponse> call, @NonNull Response<CancelBookingResponse> response) {
                hideLoading();

                if (!response.isSuccessful()) {
                    assert response.errorBody() != null;
                    showErrorDialog(BadResponse.getInstance(response.errorBody()).getMsg());
                    return;
                }

                stopAnimation();
            }

            @Override
            public void onFailure(@NonNull Call<CancelBookingResponse> call, @NonNull Throwable t) {
                hideLoading();
                showServerErrorDialog();
            }
        });
    }

    protected void shareClick() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, CacheData.getAppProperty().downloadUrl);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_one)));
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }

    protected void startService() {
        if (serviceIntent == null) {
            serviceIntent = new Intent(this, MainService.class);
        }

        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        EventBus.getDefault().register(this);
        startSocketService();
    }

    private void startSocketService() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post(new StartSocketServiceBus(MapActivityAction.this));
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 1000);
    }

    protected void stopService() {
        EventBus.getDefault().post(new StopSocketServiceBus());
        EventBus.getDefault().unregister(this);

        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
    }
}
