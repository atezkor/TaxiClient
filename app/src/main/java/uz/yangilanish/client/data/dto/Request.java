package uz.yangilanish.client.data.dto;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.utils.CacheData;


public class Request {

    @SerializedName("client_id")
    protected int clientId = CacheData.getClient().getId(); // String phone = AppPref.getPhone();

    @SerializedName("secret_key")
    protected String secretKey = RequestHeader.getSecretKey();

    protected String language = RequestHeader.getLanguage();

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
