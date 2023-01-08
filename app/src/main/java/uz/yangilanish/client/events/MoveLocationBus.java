package uz.yangilanish.client.events;

import android.location.Location;

public class MoveLocationBus {

    private Location location;

    public MoveLocationBus(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
