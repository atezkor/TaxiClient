package uz.yangilanish.client.data.dto.booking;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.data.dto.Request;
import uz.yangilanish.client.utils.CacheData;


public class CancelBookingRequest extends Request {

    @SerializedName("booking_id")
    private int bookingId;

    public CancelBookingRequest() {
        setBookingId(CacheData.getBooking().getId());
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}
