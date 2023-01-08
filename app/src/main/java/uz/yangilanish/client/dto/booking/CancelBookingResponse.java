package uz.yangilanish.client.dto.booking;

import uz.yangilanish.client.dto.Response;

public class CancelBookingResponse extends Response {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
