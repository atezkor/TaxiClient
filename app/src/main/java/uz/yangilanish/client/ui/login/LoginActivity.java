package uz.yangilanish.client.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.yangilanish.client.R;
import uz.yangilanish.client.data.api.ApiBuilder;
import uz.yangilanish.client.data.dto.auth.LoginRequest;
import uz.yangilanish.client.data.dto.auth.LoginResponse;
import uz.yangilanish.client.data.dto.response.BadResponse;
import uz.yangilanish.client.events.CheckNetworkBus;
import uz.yangilanish.client.ui.layout.LayoutActivity;
import uz.yangilanish.client.ui.view.ViewNumPad;
import uz.yangilanish.client.utils.NumberFormat;


public class LoginActivity extends LayoutActivity implements ViewNumPad.OnNumPadClickListener {

    private AppCompatButton btnLogin;
    private AppCompatTextView phoneNumber;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLocale();
        setContentView(R.layout.activity_login);

        EventBus.getDefault().register(this);

        setContentActivity();
        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        stopTimer();
    }

    /* Initializing views */
    private void setContentActivity() {
        viewNoGps = findViewById(R.id.view_no_gps);
        viewNoInternet = findViewById(R.id.view_no_internet);

        phoneNumber = findViewById(R.id.tv_phone_number);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.rl_progress_bar);

        ViewNumPad viewNumPad = findViewById(R.id.num_pad);
        // Pastdan turib yuqoriga ta'sir qilish
        viewNumPad.onClickNumPad(this);

        btnLogin.setOnClickListener(this::clickLoginButton);
    }

    @Override
    public void onKeyDown(String number) {
        String masked = this.phoneNumber.getText().toString();
        if (number.length() >= 4)
            masked = "";
        if (number.length() > 9)
            number = number.substring(0, 9);

        String unmasked = NumberFormat.unmasked(masked);
        if (unmasked.length() < 9) {
            masked = NumberFormat.maskedPhone(masked.concat(number));
            phoneNumber.setText(masked);

            if (masked.length() == 14) {
                btnLogin.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDelete() {
        String maskedNumber = phoneNumber.getText().toString();
        String unmasked = NumberFormat.unmasked(maskedNumber);
        int l = unmasked.length();

        if (l > 0) {
            unmasked = unmasked.substring(0, l - 1);
            phoneNumber.setText(NumberFormat.maskedPhone(unmasked));

            if (maskedNumber.length() == 14) {
                btnLogin.setVisibility(View.GONE);
            }
        }
    }

    private void clickLoginButton(View v) {
        if (!(internetIsEnabled() && gpsIsEnabled())) {
            return;
        }

        showLoading();

        LoginRequest request = new LoginRequest(NumberFormat.unmasked(phoneNumber.getText().toString()));
        ApiBuilder.api().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                hideLoading();

                if (!response.isSuccessful()) {
                    assert response.errorBody() != null;
                    BadResponse errors = BadResponse.getInstance(response.errorBody());
                    showErrorDialog(errors.getMsg());

                    return;
                }

                assert response.body() != null;
                LoginResponse.Data data = response.body().getData();
                if (data.isRequiredUpdate()) {
                    showUpdateDialog(response.body().getData().getUpdateUrl());
                    return;
                }

                String phone = LoginActivity.this.phoneNumber.getText().toString();
                LoginActivity.this.openSmsConfirmActivity(phone, data.getCompany());
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                LoginActivity.this.hideLoading();
                LoginActivity.this.showServerErrorDialog();
            }
        });
    }


    @Override
    protected void startTimer() {
        timerTask = new TimerTask() {
            public void run() {
                EventBus.getDefault().post(new CheckNetworkBus());
            }
        };

        super.startTimer();
    }

    /* Events */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(CheckNetworkBus bus) {
        if (this.internetIsEnabled()) {
            viewNoInternet.setVisibility(View.GONE);

            if (this.gpsIsEnabled()) {
                viewNoGps.setVisibility(View.GONE);
            } else {
                viewNoGps.setVisibility(View.VISIBLE);
            }
        } else {
            viewNoInternet.setVisibility(View.VISIBLE);
        }
    }
}