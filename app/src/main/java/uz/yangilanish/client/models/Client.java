package uz.yangilanish.client.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Client {

    private int id;

    @SerializedName("full_name")
    private String fullName;

    private String phone;

    @SerializedName("secret_key")
    private String secretKey;

    @SerializedName("booking_count")
    private int bookingCount;

    private double bonus;

    @SerializedName("bonus_sms_code")
    private int bonusSmsCode;

    @SerializedName("last_booking_addresses")
    private List<Address> lastBookingAddresses = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public int getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(int bookingCount) {
        this.bookingCount = bookingCount;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public int getBonusSmsCode() {
        return bonusSmsCode;
    }

    public void setBonusSmsCode(int bonusSmsCode) {
        this.bonusSmsCode = bonusSmsCode;
    }

    public List<Address> getLastBookingAddresses() {
        return lastBookingAddresses;
    }

    public void setLastBookingAddresses(List<Address> lastBookingAddresses) {
        this.lastBookingAddresses = lastBookingAddresses;
    }
}
