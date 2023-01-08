package uz.yangilanish.client.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Date;

import uz.yangilanish.client.R;


public class MapPin extends RelativeLayout {

    private long markerShownTime;
    private static final int interval = 200;

    private ImageView pin, shadow, eye, searchPin;

    private boolean start = true;

    private Animation upward, scaleIn, backward, scaleOut, fadeIn, fadeOut;

    public MapPin(Context context) {
        super(context);
        setContentView(context);
    }

    public MapPin(Context context, AttributeSet attrs) {
        super(context, attrs);
        setContentView(context);
    }

    public MapPin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(context);
    }

    private void setContentView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_map_pin, this);

        pin = findViewById(R.id.main_pin);
        eye = findViewById(R.id.pin_eye);
        shadow = findViewById(R.id.pin_shadow);
        searchPin = findViewById(R.id.search_pin);

        /* Load animations */
        upward = AnimationUtils.loadAnimation(getContext(), R.anim.upward);
        scaleIn = AnimationUtils.loadAnimation(getContext(), R.anim.shadow_scale_in);
        backward = AnimationUtils.loadAnimation(getContext(), R.anim.backward);
        scaleOut = AnimationUtils.loadAnimation(getContext(), R.anim.shadow_scale_out);
        // fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        upward.setDuration(interval);
        scaleIn.setDuration(interval);
        backward.setDuration(interval);
        scaleOut.setDuration(interval);
        fadeIn.setDuration(interval);
        fadeOut.setDuration(interval);
    }

    public void showMarker() {
        markerShownTime = new Date().getTime();
        if (start) {
            return;
        }

        pin.startAnimation(upward);
        eye.startAnimation(upward);
        shadow.startAnimation(scaleIn);
        searchPin.startAnimation(fadeIn);

        start = true;
    }

    public void hideMarker() {
        long currentTime = new Date().getTime();
        if (currentTime < markerShownTime + interval / 2) {
            return;
        }

        if (start) {
            pin.startAnimation(backward);
            eye.startAnimation(backward);
            shadow.startAnimation(scaleOut);
            searchPin.startAnimation(fadeOut);
        }

        start = false;
    }
}