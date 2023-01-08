package uz.yangilanish.client.network;


import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import kotlin.jvm.Synchronized;
import uz.yangilanish.client.utils.AppPref;


public class SocketApp {

    private final Socket mSocket;

    {
        try {
            IO.Options options = new IO.Options();

            Map<String, List<String>> header = new HashMap<>();
            header.put("secret-key", Collections.singletonList(AppPref.getSecretKey()));
            options.extraHeaders = header;

            mSocket = IO.socket(ApiConstants.API_URL, options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Synchronized
    public Socket getSocketClient() {
        return mSocket;
    }
}
