package uz.yangilanish.client.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import uz.yangilanish.client.data.dto.auth.CheckAuthRequest;
import uz.yangilanish.client.data.dto.auth.CheckAuthResponse;
import uz.yangilanish.client.data.dto.auth.LoginRequest;
import uz.yangilanish.client.data.dto.auth.LoginResponse;
import uz.yangilanish.client.data.dto.auth.SmsConfirmRequest;
import uz.yangilanish.client.data.dto.auth.SmsConfirmResponse;
import uz.yangilanish.client.data.dto.booking.CancelBookingRequest;
import uz.yangilanish.client.data.dto.booking.CancelBookingResponse;
import uz.yangilanish.client.data.dto.booking.CreateBookingRequest;
import uz.yangilanish.client.data.dto.booking.CreateBookingResponse;


public interface ClientApi {

    @POST("/client/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/client/confirm-sms")
    Call<SmsConfirmResponse> smsConfirm(@Body SmsConfirmRequest request);

    @POST("/client/check")
    Call<CheckAuthResponse> checkClientAuth(@Body CheckAuthRequest request);

    @POST("/client/booking/create")
    Call<CreateBookingResponse> createBooking(@Body CreateBookingRequest createBookingRequest);

    @HTTP(method = "DELETE", path = "/client/booking/cancel", hasBody = true)
    Call<CancelBookingResponse> cancelBooking(@Body CancelBookingRequest request);
}
