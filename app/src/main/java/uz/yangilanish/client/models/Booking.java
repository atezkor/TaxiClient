package uz.yangilanish.client.models;

public class Booking {

    public static final int STATUS_CREATED = 1;
    public static final int STATUS_TAKEN = 2;
    public static final int STATUS_IN_PLACE = 3;
    public static final int STATUS_FINISHED = 5;

    public static final String TYPE_APPLICATION = "application";

    private int id;

    private int status = STATUS_CREATED;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
