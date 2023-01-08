package uz.yangilanish.client.dto;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.utils.AppPref;
import uz.yangilanish.client.utils.CacheData;


public class Request {

    @SerializedName("client_id")
    protected int clientId = CacheData.getClient().getId(); // String phone = AppPref.getPhone();

    @SerializedName("secret_key")
    protected String secretKey = AppPref.getSecretKey();

    protected String language = AppPref.getLanguage();

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
