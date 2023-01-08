package uz.yangilanish.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.socket.client.Socket;
import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.models.AppProperty;
import uz.yangilanish.client.models.Booking;
import uz.yangilanish.client.models.CarModel;
import uz.yangilanish.client.models.Client;
import uz.yangilanish.client.models.Company;
import uz.yangilanish.client.models.Driver;
import uz.yangilanish.client.network.SocketClient;


public class CacheData {

    private static Context applicationContext;

    private static Client client;

    private static Location gpsLocation, mapLocation;

    private static List<CarModel> carModelList;

    private static List<Driver.Tariff> tariffList;

    private static List<Address> addressList;

    private static Address currentAddress;

    private static Booking booking;

    private static Driver driver;

    private static List<Driver> onlineDrivers;

    private static Company company;
    private static AppProperty appProperty;

    private static SocketClient socketClient;
    private static Thread socketThread;
    private static Socket socket; // Channel channel

    public static void setApplicationContext(Context context) {
        applicationContext = context;
    }

    public static String getPackageName() {
        return applicationContext.getPackageName();
    }

    public static SharedPreferences getPreferences() {
        return applicationContext.getSharedPreferences(getPackageName(), 0);
    }

    public static SharedPreferences getDefaultPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public static Driver getDriver() {
        return driver;
    }

    public static void setDriver(Driver driver) {
        CacheData.driver = driver;
    }

    /* Location */
    public static Location getGpsLocation() {
        return gpsLocation;
    }

    public static void setGpsLocation(Location location) {
        gpsLocation = location;
    }

    public static Location getMapLocation() {
        return mapLocation;
    }

    public static void setMapLocation(Location location) {
        mapLocation = location;
    }

    /* Client */
    public static Client getClient() {
        return client;
    }

    public static void setClient(Client model) {
        client = model;
    }

    public static Booking getBooking() {
        return booking;
    }

    public static void setBooking(Booking model) {
        booking = model;
    }

    public static Company getCompany() {
        return company;
    }

    public static void setCompany(Company company) {
        CacheData.company = company;
    }

    public static AppProperty getAppProperty() {
        return appProperty;
    }

    public static void setAppProperty(AppProperty property) {
        appProperty = property;
    }

    /* Socket */
    public static SocketClient getSocketClient() {
        return socketClient;
    }

    public static void setSocketClient(SocketClient client) {
        socketClient = client;
    }

    public static Thread getSocketThread() {
        return socketThread;
    }

    public static void setSocketThread(Thread thread) {
        socketThread = thread;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket client) {
        socket = client;
    }

    // List
    public static List<Driver> getOnlineDrivers() {
        if (onlineDrivers == null)
            return new CopyOnWriteArrayList<>();

        return onlineDrivers;
    }

    public static void setOnlineDrivers(List<Driver> drivers) {
        onlineDrivers = drivers;
    }

    public static List<CarModel> getCarModelList() {
        return carModelList;
    }

    public static void setCarModelList(List<CarModel> list) {
        carModelList = list;
    }

    public static List<Driver.Tariff> getTariffList() {
        return tariffList;
    }

    public static void setTariffList(List<Driver.Tariff> list) {
        tariffList = list;
    }

    public static List<Address> getAddressList() {
        return addressList;
    }

    public static void setAddressList(List<Address> list) {
        addressList = list;
    }

    public static Address getCurrentAddress() {
        if (currentAddress == null)
            return new Address();

        return currentAddress;
    }

    public static void setCurrentAddress(Address address) {
        currentAddress = address;
    }
}
