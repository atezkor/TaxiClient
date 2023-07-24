package uz.yangilanish.client.data.dto.booking;

import uz.yangilanish.client.data.dto.Response;

public class CancelBookingResponse extends Response {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
