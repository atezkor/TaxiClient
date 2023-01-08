package uz.yangilanish.client.events;

import android.app.Activity;

public class StartSocketServiceBus {

    private final Activity activity;

    public StartSocketServiceBus(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
