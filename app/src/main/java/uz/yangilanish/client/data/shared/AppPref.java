package uz.yangilanish.client.data.shared;

import uz.yangilanish.client.utils.CacheData;


public class AppPref {

    public static void setLanguage(String language) {
        CacheData.getPreferences()
            .edit()
            .putString("language", language)
            .apply();
    }

    public static String getLanguage() {
        return CacheData.getPreferences().getString("language", "");
    }

    public static void setPhone(String phone) {
        CacheData.getPreferences().edit().putString("number", phone).apply();
    }

    public static String getPhone() {
        return CacheData.getPreferences().getString("number", null);
    }

    public static void setSecretKey(String secretKey) {
        CacheData.getDefaultPreferences().edit().putString("secret_key", secretKey).apply();
    }

    public static String getSecretKey() {
        return CacheData.getDefaultPreferences().getString("secret_key", null);
    }
}
