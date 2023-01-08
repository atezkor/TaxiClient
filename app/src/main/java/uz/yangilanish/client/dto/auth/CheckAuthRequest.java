package uz.yangilanish.client.dto.auth;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.utils.AppPref;


public class CheckAuthRequest extends AuthRequest {

    private String phone;

    @SerializedName("secret_key")
    private String secretKey;

    public CheckAuthRequest() {
        this.setPhone(AppPref.getPhone());
        this.setSecretKey(AppPref.getSecretKey());
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
