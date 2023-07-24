package uz.yangilanish.client.data.dto.auth;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.data.dto.RequestHeader;


public class CheckAuthRequest extends AuthRequest {

    private String phone;

    @SerializedName("secret_key")
    private String secretKey;

    public CheckAuthRequest() {
        this.setPhone(RequestHeader.getPhone());
        this.setSecretKey(RequestHeader.getSecretKey());
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
