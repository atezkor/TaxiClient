package uz.yangilanish.client.events;

import uz.yangilanish.client.models.Driver;


public class TakeBookingBus {

    private Driver driver;

    public TakeBookingBus(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
