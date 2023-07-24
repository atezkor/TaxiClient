package uz.yangilanish.client.utils;

import java.util.Map;
import java.util.TreeMap;

import uz.yangilanish.client.models.Address;


public class LocationCalculator {

    private final static double R = 6371000.0d;

    public static Address getAddressByLocation(double latitude, double longitude) {
        Map<Double, Address> addressTreeMap = new TreeMap<>();

        double distance;
        for (Address address : CacheData.getAddressList()) {
            distance = calculateDistance(latitude, longitude, address.getLatitude(), address.getLongitude());
            addressTreeMap.put(distance, address);
        }

        if (addressTreeMap.size() > 0) {
            return addressTreeMap.entrySet().iterator().next().getValue(); // minimal distance
        }

        return null;
    }

    private static int calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        double dLat = Math.toRadians(endLatitude - startLatitude);
        double dLng = Math.toRadians(endLongitude - startLongitude);

        double a = (Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude)) * Math.sin(dLng / 2.0d) * Math.sin(dLng / 2.0d));

        return (int) (R * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a)) * 2.0d);
    }
}
