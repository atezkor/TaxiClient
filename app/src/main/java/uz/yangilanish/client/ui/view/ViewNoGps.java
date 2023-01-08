package uz.yangilanish.client.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;

import uz.yangilanish.client.R;


public class ViewNoGps extends RelativeLayout {

    private AppCompatTextView errorTitle, errorText;

    private ImageView errorImage;

    public ViewNoGps(Context context) {
        super(context);
        initControl(context);
    }

    public ViewNoGps(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public ViewNoGps(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_no_network, this);

        errorTitle = findViewById(R.id.tv_error_title);
        errorText = findViewById(R.id.tv_error_text);
        errorImage = findViewById(R.id.iv_error_image);

        setScreenContent();
    }

    public void setScreenContent() {
        errorTitle.setText(getContext().getString(R.string.no_gps));
        errorText.setText(getContext().getString(R.string.no_gps_text));
        errorImage.setImageResource(R.drawable.ic_no_gps);
    }
}