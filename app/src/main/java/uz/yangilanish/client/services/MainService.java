package uz.yangilanish.client.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import uz.yangilanish.client.R;
import uz.yangilanish.client.events.MoveLocationBus;
import uz.yangilanish.client.events.StartSocketServiceBus;
import uz.yangilanish.client.events.StopSocketServiceBus;
import uz.yangilanish.client.events.TimerBus;
import uz.yangilanish.client.act.socket.SocketClient;
import uz.yangilanish.client.utils.CacheData;


public class MainService extends Service {

    private int gcTimer = 0;
    private int locationTimer = 0;

    private Timer timer;
    private TimerTask timerTask;

    private LocationManager locationManager;

    final int NOTIFICATION_ID = 1101;
    final String SYSTEM_NOTIFICATION_CHANNEL_ID = "SYSTEM_NOTIFICATION_CHANNEL_ID";

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        showNotification();
        startGPSLocationService();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopTimer();
        stopSocketService();
        EventBus.getDefault().unregister(this);
    }

    private void showNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, SYSTEM_NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentTitle(getApplicationContext().getString(R.string.app_ready))
                .setPriority(Notification.DEFAULT_LIGHTS)
                .setOngoing(true)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < 26) {
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            return;
        }

        NotificationChannel notificationChannel = new NotificationChannel(SYSTEM_NOTIFICATION_CHANNEL_ID, "-", NotificationManager.IMPORTANCE_DEFAULT); // IMPORTANCE_HIGH -> before
        notificationManager.createNotificationChannel(notificationChannel);
        // Foreground permission
        startForeground(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void startGPSLocationService() {
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            return;
        }

        // CacheData.setGpsLocation(AppPref.getLastLocation());
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }

        //noinspection Convert2Lambda
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (CacheData.getMapLocation() == null) {
                    EventBus.getDefault().post(new MoveLocationBus(location));
                }

                CacheData.setGpsLocation(location);
                CacheData.setMapLocation(location);
            }
        });

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            CacheData.setGpsLocation(lastKnownLocation);
            CacheData.setMapLocation(lastKnownLocation);

            EventBus.getDefault().post(new MoveLocationBus(lastKnownLocation));
        }
    }

    protected void startTimer() {
        stopTimer();

        timerTask = new TimerTask() {
            public void run() {
                if (locationTimer <= 0 && CacheData.getSocketClient() != null) {
                    locationTimer = CacheData.getAppProperty().locationTimer;
                    CacheData.getSocketClient().sendLocation();
                }

                if (gcTimer == 60000) {
                    gcTimer = 0;
                    System.gc(); // garbage clear
                }

                locationTimer -= 1000;
                gcTimer += 1000;
                EventBus.getDefault().post(new TimerBus());
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 500, 1000);
    }

    protected void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
        }

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }


    private void startSocketService() {
        Thread thread = CacheData.getSocketThread();
        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(CacheData.getSocketClient());
        CacheData.setSocketThread(thread);

        thread.start();
    }

    private void stopSocketService() {
        if (CacheData.getSocketThread() != null) {
            CacheData.getSocketThread().interrupt();
        }

        if (CacheData.getSocket() != null) {
            CacheData.getSocket().disconnect();
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StartSocketServiceBus bus) {
        SocketClient client = new SocketClient(bus.getActivity());
        CacheData.setSocketClient(client);

        startSocketService();
    }


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StopSocketServiceBus bus) {
        stopSocketService();
    }

//    public void startGoogleLocationService() {
//        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
//        builder.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//            @Override
//            public void onConnected(Bundle bundle) {
//                if (ActivityCompat.checkSelfPermission(MainService.this.getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(MainService.this.getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
//                    Location location = LocationServices.FusedLocationApi.getLastLocation(MainService.this.googleApiClient);
//                    if (MainService.this.firstTimeLocation && location != null) {
//                        CacheData.setMapLocation(location);
//                        CacheData.setGpsLocation(location);
//                        EventBus.getDefault().post(new BusMoveMarker(location));
//                    }
//
//                    LocationServices.FusedLocationApi.requestLocationUpdates(MainService.this.googleApiClient, LocationRequest.create().setPriority(100).setInterval(3000), new com.google.android.gms.location.LocationListener() {
//                        @Override
//                        public void onLocationChanged(Location location) {
//                            if (MainService.this.firstTimeLocation) {
//                                MainService.this.firstTimeLocation = false;
//                                CacheData.setMapLocation(location);
//                                CacheData.setGpsLocation(location);
//                                EventBus.getDefault().post(new BusMoveMarker(location));
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onConnectionSuspended(int i) {
//            }
//        });
//        builder.addApi(LocationServices.API);
//        this.googleApiClient = builder.build();
//        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        GoogleApiClient googleApiClient2 = this.googleApiClient;
//        if (googleApiClient2 != null) {
//            googleApiClient2.connect();
//        }
//    }
//
//    public void stopGoogleLocationService() {
//        GoogleApiClient googleApiClient2 = googleApiClient;
//        if (googleApiClient2 != null && googleApiClient2.isConnected()) {
//            this.googleApiClient.disconnect();
//        }
//    }
}
