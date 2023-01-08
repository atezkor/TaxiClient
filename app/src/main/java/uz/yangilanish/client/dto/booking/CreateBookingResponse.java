package uz.yangilanish.client.dto.booking;

import uz.yangilanish.client.dto.Response;
import uz.yangilanish.client.models.Booking;

public class CreateBookingResponse extends Response {

    private Booking data;

    public Booking getData() {
        return data;
    }

    public void setData(Booking data) {
        this.data = data;
    }
}
