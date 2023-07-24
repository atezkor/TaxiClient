package uz.yangilanish.client.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import uz.yangilanish.client.data.shared.AppPref;
import uz.yangilanish.client.models.Company;
import uz.yangilanish.client.ui.login.LoginActivity;
import uz.yangilanish.client.ui.login.SmsConfirmActivity;
import uz.yangilanish.client.ui.main.MapActivity;
import uz.yangilanish.client.ui.splash.SplashActivity;
import uz.yangilanish.client.ui.taximeter.TaximeterActivity;
import uz.yangilanish.client.utils.CacheData;


public class MainActivity extends AppCompatActivity implements IMainActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CacheData.setApplicationContext(getApplicationContext());
    }

    @Override
    public boolean gpsIsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public boolean internetIsEnabled() {
        NetworkInfo activeNetwork = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public void openSplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
    }

    @Override
    public void openSmsConfirmActivity(String phone, Company company) {
        Intent intent = new Intent(getApplicationContext(), SmsConfirmActivity.class);
        intent.putExtra("phone", phone);

        if (!company.getSmsCentreNumber1().isEmpty() && !company.getSmsCentreNumber1().isEmpty()) {
            intent.putExtra("phone1", company.getSmsCentreNumber1());
            intent.putExtra("phone2", company.getSmsCentreNumber2());
            startActivity(intent);
        }

        this.closeCurrentActivity();
    }

    @Override
    public void openMapActivity() {
        startActivity(new Intent(this, MapActivity.class));
    }

    @Override
    public void openTaximeterActivity() {
        setDefaultLocale();
        startActivity(new Intent(this, TaximeterActivity.class));
    }

    @Override
    public void closeCurrentActivity() {
        finish();
    }

    @Override
    public void openCallView(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    protected void setDefaultLocale() {
        Locale myLocale = new Locale(AppPref.getLanguage());
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}