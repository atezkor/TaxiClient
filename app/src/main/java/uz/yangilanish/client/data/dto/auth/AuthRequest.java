package uz.yangilanish.client.data.dto.auth;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.BuildConfig;
import uz.yangilanish.client.data.dto.RequestHeader;


public class AuthRequest {

    private String language = RequestHeader.getLanguage();

    @SerializedName("app_version")
    private String appVersion = BuildConfig.VERSION_NAME;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
