package uz.yangilanish.client.ui.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import uz.yangilanish.client.R;


public class ViewPermission extends RelativeLayout {

    private ImageView locationStatusIcon;
    private ImageView smsStatusIcon;

    private LinearLayout btnLocationPermission;
    private LinearLayout btnSmsPermission;

    public ViewPermission(Context context) {
        super(context);
        initControl(context);
    }

    public ViewPermission(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public ViewPermission(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_permission, this);

        this.btnLocationPermission = findViewById(R.id.btn_location_permission);
        this.btnSmsPermission = findViewById(R.id.btn_sms_permission);

        this.locationStatusIcon = findViewById(R.id.iv_location_status);
        this.smsStatusIcon = findViewById(R.id.iv_sms_status);

        this.setViewContent();
        this.setPermissionsState(context);
    }

    public void setViewContent() {
        this.btnLocationPermission.setVisibility(View.GONE);
        this.btnSmsPermission.setVisibility(View.GONE);
    }

    public void setPermissionsState(Context context) {
        this.btnLocationPermission.setVisibility(View.VISIBLE);
        this.btnSmsPermission.setVisibility(View.VISIBLE);
        this.locationStatusIcon.setVisibility(View.VISIBLE);
        this.smsStatusIcon.setVisibility(View.VISIBLE);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.locationStatusIcon.setImageResource(R.drawable.ic_check);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            this.smsStatusIcon.setImageResource(R.drawable.ic_check);
        }
    }


    /* Click listener */
    public void setLocationClickListener(Activity activity) {
        this.btnLocationPermission.setOnClickListener(view -> requestLocationPermission(activity));
    }

    public void setSmsClickListener(Activity activity) {
        this.btnSmsPermission.setOnClickListener(view -> requestSMSPermission(activity));
    }


    /* Request permission */
    private void requestLocationPermission(Activity activity) {
        // Uses permission: ACCESS_FINE_LOCATION
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }

    private void requestSMSPermission(Activity activity) {
        // Uses permission: RECEIVE_SMS
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECEIVE_SMS}, 0);
    }

    public boolean checkFullPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23)
            return true;

        // SMS and LOCATION permission (both)
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}