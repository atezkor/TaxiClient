package uz.yangilanish.client.data.dto.socket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import uz.yangilanish.client.models.Booking;
import uz.yangilanish.client.models.Driver;


public class SocketResponse {

    private String action = "";

    private List<Driver> driverList = new CopyOnWriteArrayList<>();

    private Booking booking;

    private Driver driver;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public static SocketResponse getInstance(Object... args) { // getParsedObject
        Gson gson = new Gson();
        try {
            return gson.fromJson(args[0].toString(), SocketResponse.class);
        } catch (JsonSyntaxException | NullPointerException e) {
            e.printStackTrace();
            return new SocketResponse();
        }
    }
}
