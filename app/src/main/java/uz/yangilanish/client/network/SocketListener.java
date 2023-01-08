package uz.yangilanish.client.network;

import android.app.Activity;
import android.util.Log;

import io.socket.emitter.Emitter;


public class SocketListener {

    public static final String TAG = "SOCKET_EVENT";

    public static Boolean isConnected = false;

    private final Activity activity;

    public SocketListener(Activity activity) {
        this.activity = activity;
    }

    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            activity.runOnUiThread(() -> {
                isConnected = true;
                Log.i(TAG, "Connected to Socket");
            });
        }
    };

    public Emitter.Listener onDisconnect = args -> {
        isConnected = false;
        Log.i(TAG, "Disconnected");
    };

    public Emitter.Listener onConnectError = args -> Log.e(TAG, "Error connecting");
}
