package uz.yangilanish.client.ui.taximeter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.yangilanish.client.R;
import uz.yangilanish.client.data.api.ApiBuilder;
import uz.yangilanish.client.data.dto.booking.CancelBookingRequest;
import uz.yangilanish.client.data.dto.booking.CancelBookingResponse;
import uz.yangilanish.client.data.dto.response.BadResponse;
import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.models.Driver;
import uz.yangilanish.client.ui.layout.LayoutActivity;
import uz.yangilanish.client.utils.CacheData;
import uz.yangilanish.client.utils.NumberFormat;


public class TaximeterActivityAction extends LayoutActivity {

    protected AlertDialog cancelBookingAlertDialog;
    protected AlertDialog finishBookingAlertDialog;

    public void callDriver(Driver driver) {
        if (driver != null) {
            openCallView(driver.getPhone());
        }
    }

    public void notifyAboutTaximeter() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.taximeter_notification);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /* Dialogs */
    protected void showCancelBookingDialog() {
        if (cancelBookingAlertDialog == null || !cancelBookingAlertDialog.isShowing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cancel_booking, null);

            Driver driver = CacheData.getDriver();
            Address address = CacheData.getCurrentAddress();
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_address_name)).setText(address.getName());
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_car_model)).setText(driver.getCar().getModel());

            int price = driver.getTariff().getMinimalPayment() + address.getAdditionalPayment();
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_payment)).setText(NumberFormat.price(price));

            dialogView.findViewById(R.id.btn_close).setOnClickListener(v -> cancelBookingAlertDialog.dismiss());
            AppCompatButton btnYes = dialogView.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(v -> {
                cancelBookingAlertDialog.dismiss();
                cancelBooking();
            });

            dialogBuilder.setView(dialogView);
            cancelBookingAlertDialog = dialogBuilder.create();
            cancelBookingAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancelBookingAlertDialog.setCancelable(true);
            cancelBookingAlertDialog.show();
        }
    }

    protected void showFinishBookingDialog(int payment) {
        notifyAboutTaximeter();
        if (finishBookingAlertDialog == null || !finishBookingAlertDialog.isShowing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_finish_booking, null);

            ((AppCompatTextView) dialogView.findViewById(R.id.tv_booking_pay)).setText(NumberFormat.price(payment));
            AppCompatButton btnFinish = dialogView.findViewById(R.id.btn_finish);
            btnFinish.setOnClickListener(v -> {
                finishBookingAlertDialog.dismiss();
                closeCurrentActivity();
            });

            dialogBuilder.setView(dialogView);
            finishBookingAlertDialog = dialogBuilder.create();
            finishBookingAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            finishBookingAlertDialog.setCancelable(false);
            finishBookingAlertDialog.show();
        }
    }

    private void cancelBooking() {
        if (!internetIsEnabled() || !gpsIsEnabled())
            return;

        showLoading();

        CancelBookingRequest cancelBooking = new CancelBookingRequest();
        ApiBuilder.api().cancelBooking(cancelBooking).enqueue(new Callback<CancelBookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<CancelBookingResponse> call, @NonNull Response<CancelBookingResponse> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    closeCurrentActivity();
                    return;
                }
                assert response.errorBody() != null;
                showErrorDialog(BadResponse.getInstance(response.errorBody()).getMsg());
            }

            @Override
            public void onFailure(@NonNull Call<CancelBookingResponse> call, @NonNull Throwable t) {
                hideLoading();
                showServerErrorDialog();
            }
        });
    }
}
