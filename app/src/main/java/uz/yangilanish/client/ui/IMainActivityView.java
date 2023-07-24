package uz.yangilanish.client.ui;

import uz.yangilanish.client.models.Company;

public interface IMainActivityView {

    boolean gpsIsEnabled();

    boolean internetIsEnabled();

    void openSplashActivity();

    void openLoginActivity();

    void openSmsConfirmActivity(String phone, Company company);

    void openMapActivity();

    void openTaximeterActivity();

    void closeCurrentActivity();

    void openCallView(String phoneNumber);
}
