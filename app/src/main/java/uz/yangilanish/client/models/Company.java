package uz.yangilanish.client.models;

import com.google.gson.annotations.SerializedName;

public class Company {

    private String name;

    private double latitude = 0.0;

    private double longitude = 0.0;

    @SerializedName("sms_centre_number1")
    private String smsCentreNumber1;

    @SerializedName("sms_centre_number2")
    private String smsCentreNumber2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSmsCentreNumber1() {
        return smsCentreNumber1;
    }

    public void setSmsCentreNumber1(String smsCentreNumber1) {
        this.smsCentreNumber1 = smsCentreNumber1;
    }

    public String getSmsCentreNumber2() {
        return smsCentreNumber2;
    }

    public void setSmsCentreNumber2(String smsCentreNumber2) {
        this.smsCentreNumber2 = smsCentreNumber2;
    }
}
