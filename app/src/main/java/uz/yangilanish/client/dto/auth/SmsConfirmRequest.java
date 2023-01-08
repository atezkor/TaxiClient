package uz.yangilanish.client.dto.auth;

import com.google.gson.annotations.SerializedName;

public class SmsConfirmRequest extends AuthRequest {

    private String phone;

    @SerializedName("sms_code")
    private short smsCode; // 32,767 (2 bytes)

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public short getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(short smsCode) {
        this.smsCode = smsCode;
    }
}
