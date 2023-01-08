package uz.yangilanish.client.ui.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import okhttp3.internal.cache.DiskLruCache;
import uz.yangilanish.client.R;


public class ViewNumPad extends RelativeLayout {

    private LinearLayout number1, number2, number3, number4, number5, number6, number7, number8, number9, number0;

    private LinearLayout backspace, myNumber;

    public ViewNumPad(Context context) {
        super(context);
        initControl(context);
    }

    public ViewNumPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public ViewNumPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_numpad, this);

        number1 = findViewById(R.id.num1);
        number2 = findViewById(R.id.num2);
        number3 = findViewById(R.id.num3);
        number4 = findViewById(R.id.num4);
        number5 = findViewById(R.id.num5);
        number6 = findViewById(R.id.num6);
        number7 = findViewById(R.id.num7);
        number8 = findViewById(R.id.num8);
        number9 = findViewById(R.id.num9);
        number0 = findViewById(R.id.num0);
        backspace = findViewById(R.id.backspace);
        myNumber = findViewById(R.id.my_number);
    }

    public void onClickNumPad(OnNumPadClickListener callback) {
        number1.setOnClickListener(v -> callback.onKeyDown(DiskLruCache.VERSION_1));
        number2.setOnClickListener(v -> numberClick(v, callback));
        number3.setOnClickListener(v -> numberClick(v, callback));
        number4.setOnClickListener(v -> numberClick(v, callback));
        number5.setOnClickListener(v -> numberClick(v, callback));
        number6.setOnClickListener(v -> numberClick(v, callback));
        number7.setOnClickListener(v -> numberClick(v, callback));
        number8.setOnClickListener(v -> numberClick(v, callback));
        number9.setOnClickListener(v -> numberClick(v, callback));
        number0.setOnClickListener(v -> numberClick(v, callback));

        // For backspace
        backspace.setOnClickListener(v -> callback.onDelete());
        myNumber.setOnClickListener(v -> getPhoneNumber(getContext(), callback));
    }

    public void numberClick(View view, OnNumPadClickListener clickListener) {
        String text = ((AppCompatTextView) ((LinearLayout) view).getChildAt(0)).getText().toString();

        /// callback.onKeyDown(DiskLruCache.GPS_MEASUREMENT_2D); // 2
        // callback.onKeyDown(DiskLruCache.GPS_MEASUREMENT_3D); // 3
        clickListener.onKeyDown(text);
    }

    @SuppressLint("HardwareIds")
    private void getPhoneNumber(Context context, OnNumPadClickListener clickListener) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            clickListener.onKeyDown(manager.getLine1Number());
        } catch (SecurityException exception) {
            Log.d("SIM", manager.getSimOperatorName());
        }
    }

    public interface OnNumPadClickListener {

        void onKeyDown(String number);

        void onDelete();
    }
}