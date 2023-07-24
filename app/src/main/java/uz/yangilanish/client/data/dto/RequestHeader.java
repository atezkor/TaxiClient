package uz.yangilanish.client.data.dto;

import uz.yangilanish.client.data.shared.AppPref;


public class RequestHeader {

    public static String getPhone() {
        return AppPref.getPhone();
    }

    public static String getSecretKey() {
        return AppPref.getSecretKey();
    }

    public static String getLanguage() {
        return AppPref.getLanguage();
    }
}
