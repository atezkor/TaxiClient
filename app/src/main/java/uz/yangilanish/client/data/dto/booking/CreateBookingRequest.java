package uz.yangilanish.client.data.dto.booking;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.data.dto.Request;
import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.models.Booking;
import uz.yangilanish.client.utils.CacheData;


public class CreateBookingRequest extends Request {

    private String address;

    private double latitude, longitude;

    private String type;

    @SerializedName("model_id")
    private int modelId;

    @SerializedName("address_payment")
    private int addressPayment;

    public CreateBookingRequest(int modelId) {
        setModelId(modelId);

        Location location = CacheData.getMapLocation();
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());

        Address address = CacheData.getCurrentAddress();
        setAddress(address.getName());
        setType(Booking.TYPE_APPLICATION);
        setAddressPayment(address.getAdditionalPayment());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getAddressPayment() {
        return addressPayment;
    }

    public void setAddressPayment(int addressPayment) {
        this.addressPayment = addressPayment;
    }
}
