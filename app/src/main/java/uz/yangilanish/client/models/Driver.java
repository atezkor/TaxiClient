package uz.yangilanish.client.models;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.utils.CacheData;

public class Driver {

    private String phone;

    @SerializedName("tariff_id")
    private int tariffId;

    private Car car;

    private Tariff tariff;

    private double latitude;

    private double longitude;

    private float bearing;

    private int speed;

    private float taximeterDistance;

    private int payment;

    private int taximeterTime;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTariffId() {
        return tariffId;
    }

    public void setTariffId(int tariffId) {
        this.tariffId = tariffId;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
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

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getTaximeterDistance() {
        return taximeterDistance;
    }

    public void setTaximeterDistance(float taximeterDistance) {
        this.taximeterDistance = taximeterDistance;
    }

    public int getTaximeterPayment() {
        return payment;
    }

    public void setTaximeterPayment(int payment) {
        this.payment = payment;
    }

    public int getTaximeterTime() {
        return taximeterTime;
    }

    public void setTaximeterTime(int taximeterTime) {
        this.taximeterTime = taximeterTime;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }


    public static class Car {
        private String number;

        private String model;

        @SerializedName("model_id")
        private int modelId;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getModel() {
            setModel(CacheData.getCarModelList().get(modelId).getName());
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }

    public static class Tariff {

        private int id;

        private String name;

        @SerializedName("minimal_payment")
        private int minimalPayment;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMinimalPayment() {
            return minimalPayment;
        }

        public void setMinimalPayment(int minimalPayment) {
            this.minimalPayment = minimalPayment;
        }
    }
}
