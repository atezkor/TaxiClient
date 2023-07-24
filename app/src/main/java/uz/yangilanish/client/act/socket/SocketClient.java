package uz.yangilanish.client.act.socket;

import android.app.Activity;

import org.greenrobot.eventbus.EventBus;

import io.socket.client.Socket;
import uz.yangilanish.client.data.dto.socket.SocketRequest;
import uz.yangilanish.client.data.dto.socket.SocketResponse;
import uz.yangilanish.client.events.LogoutBus;
import uz.yangilanish.client.events.TakeBookingBus;
import uz.yangilanish.client.models.Booking;
import uz.yangilanish.client.models.Driver;
import uz.yangilanish.client.utils.CacheData;


public class SocketClient implements Runnable {

    private final Socket socket;
    private final SocketListener socketListener;

    private final String EVENT_CLIENT = "client-server";
    private final String ACTION_START = "start";

    public SocketClient(Activity activity) {
        this.socket = new SocketApp().getSocketClient();
        this.socketListener = new SocketListener(activity);

        CacheData.setSocket(socket);
    }

    @Override
    public void run() {
        socket.connect();

        socket.on(Socket.EVENT_DISCONNECT, socketListener.onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, socketListener.onConnectError);
        socket.on(Socket.EVENT_CONNECT, args -> {
            socketListener.onConnect.call(args);
            socket.emit(EVENT_CLIENT, new SocketRequest(ACTION_START));
        });

        socket.on("server-client", args -> {
            SocketResponse response = SocketResponse.getInstance(args);

            if (response.getAction().equals("logout")) {
                EventBus.getDefault().post(new LogoutBus());
            } else {
                CacheData.setOnlineDrivers(response.getDriverList());
            }
        });

        socket.on("driver-client", args -> {
            SocketResponse response = SocketResponse.getInstance(args);
            Booking booking = response.getBooking();

            Booking book = CacheData.getBooking();
            if (booking != null) {
                CacheData.setBooking(booking);

                if (booking.getStatus() == Booking.STATUS_TAKEN && (book == null || book.getStatus() < Booking.STATUS_TAKEN)) {
                    EventBus.getDefault().post(new TakeBookingBus(response.getDriver()));
                }

                if (book == null) {
                    CacheData.setBooking(booking);
                }
            }

            Driver driver = response.getDriver();
            if (driver != null) {
                CacheData.setDriver(driver);
            }
        });
    }

    public void sendLocation() {
        socket.emit(EVENT_CLIENT, new SocketRequest());
    }
}
