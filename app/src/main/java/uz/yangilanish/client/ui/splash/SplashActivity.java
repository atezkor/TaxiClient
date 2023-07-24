package uz.yangilanish.client.ui.splash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.yangilanish.client.BuildConfig;
import uz.yangilanish.client.R;
import uz.yangilanish.client.act.actions.AuthAction;
import uz.yangilanish.client.data.api.ApiBuilder;
import uz.yangilanish.client.data.dto.auth.CheckAuthRequest;
import uz.yangilanish.client.data.dto.auth.CheckAuthResponse;
import uz.yangilanish.client.data.shared.AppPref;
import uz.yangilanish.client.ui.layout.LayoutActivity;
import uz.yangilanish.client.ui.view.ViewLanguage;
import uz.yangilanish.client.ui.view.ViewPermission;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends LayoutActivity {

    private AppCompatTextView appVersion;

    private ViewLanguage viewLanguage;
    private ViewPermission viewPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLocale();
        setContentView(R.layout.activity_splash);

        setContentActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }

        stopTimer();
    }


    /* Initialize views */
    private void setContentActivity() {
        appVersion = findViewById(R.id.tv_app_version);

        viewNoGps = findViewById(R.id.view_no_gps);
        viewLanguage = findViewById(R.id.view_language);
        viewPermission = findViewById(R.id.view_permission);
        viewNoInternet = findViewById(R.id.view_no_internet);

        spinKitLoader = findViewById(R.id.spin_kit_view);

        setScreenContent();
        checkDeviceState();
    }

    private void setScreenContent() {
        String version = String.format(getString(R.string.full_version), BuildConfig.VERSION_NAME);
        appVersion.setText(version);

        viewPermission.setLocationClickListener(this);
        viewPermission.setSmsClickListener(this);

        viewLanguage.setClickListenerUz(() -> setLanguage(ViewLanguage.LANG_UZ));
        viewLanguage.setClickListenerRu(() -> setLanguage(ViewLanguage.LANG_RU));
    }

    private void setLanguage(String lang) {
        AppPref.setLanguage(lang);
        recreate(); // ilova oynasini qayta yaratadi
    }

    /* Permissions */
    private void checkDeviceState() {
        viewLanguage.setVisibility(View.GONE);
        viewPermission.setVisibility(View.GONE);
        viewNoInternet.setVisibility(View.GONE);
        viewNoGps.setVisibility(View.GONE);

        if (AppPref.getLanguage().equals("")) {
            viewLanguage.setVisibility(View.VISIBLE);
            return;
        }

        if (viewPermission.checkFullPermission(this)) {
            startTimer();
        } else {
            viewPermission.setVisibility(View.VISIBLE);
        }
    }


    /* After choose permission button. (Allow, deny, don't ask again) */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        viewPermission.setPermissionsState(this);
        checkDeviceState();
    }

    // Shu orqali sahifa almashadi
    private void openNextActivity() {
        if (!this.internetIsEnabled()) {
            viewNoInternet.setVisibility(View.VISIBLE);
            return;
        }

        if (!gpsIsEnabled()) {
            viewNoGps.setVisibility(View.VISIBLE);
            return;
        }

        stopTimer();
        viewNoInternet.setVisibility(View.GONE);
        viewNoGps.setVisibility(View.GONE);

        if (AppPref.getPhone() == null) {
            openLoginActivity();
            closeCurrentActivity();

            return;
        }

        checkClientAuth();
    }

    private void checkClientAuth() {
        if (!internetIsEnabled() || !gpsIsEnabled())
            return;

        showLoading();

        CheckAuthRequest request = new CheckAuthRequest();
        ApiBuilder.api().checkClientAuth(request).enqueue(new Callback<CheckAuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckAuthResponse> call, @NonNull Response<CheckAuthResponse> response) {
                hideLoading();

                CheckAuthResponse authResponse;
                if (!response.isSuccessful() || (authResponse = response.body()) == null) {
                    AuthAction.logout();
                    openLoginActivity();
                    closeCurrentActivity();

                    return;
                }

                // Check update this app
                if (authResponse.getData().isRequiredUpdate()) {
                    SplashActivity.this.showUpdateDialog(authResponse.getData().getUpdateUrl());
                    return;
                }

                AuthAction action = new AuthAction();
                action.clientAuth(authResponse);

                openMapActivity();
                closeCurrentActivity();
            }

            @Override
            public void onFailure(@NonNull Call<CheckAuthResponse> call, @NonNull Throwable t) {
                hideLoading();
                showServerErrorDialog();
            }
        });
    }


    @Override
    protected void startTimer() {
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(() -> openNextActivity());
            }
        };

        super.startTimer();
    }
}