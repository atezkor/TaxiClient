package uz.yangilanish.client.data.dto.socket;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import uz.yangilanish.client.data.dto.Request;


public class SocketRequest extends Request {

    private String action = "";

    private double latitude;

    private double longitude;

    public SocketRequest() {
    }

    public SocketRequest(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
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

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    // "#<" + this.status + "|" + this.phoneNumber + "|" + this.secretKey + "|" + this.latitude + "|" + this.longitude + ">\r\n";
}
