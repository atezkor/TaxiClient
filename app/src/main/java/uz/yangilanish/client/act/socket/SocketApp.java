package uz.yangilanish.client.act.socket;


import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import kotlin.jvm.Synchronized;
import uz.yangilanish.client.data.api.NetworkConstants;
import uz.yangilanish.client.data.dto.RequestHeader;


public class SocketApp {

    private final Socket mSocket;

    {
        try {
            IO.Options options = new IO.Options();

            Map<String, List<String>> header = new HashMap<>();
            header.put("secret-key", Collections.singletonList(RequestHeader.getSecretKey()));
            options.extraHeaders = header;

            mSocket = IO.socket(NetworkConstants.API_URL, options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Synchronized
    public Socket getSocketClient() {
        return mSocket;
    }
}
