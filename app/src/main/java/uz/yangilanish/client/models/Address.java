package uz.yangilanish.client.models;

import com.google.gson.annotations.SerializedName;

public class Address {

    private int id;

    private double latitude, longitude;

    private String name;

    @SerializedName("additional_payment")
    private int additionalPayment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdditionalPayment() {
        return additionalPayment;
    }

    public void setAdditionalPayment(int additionalPayment) {
        this.additionalPayment = additionalPayment;
    }
}
