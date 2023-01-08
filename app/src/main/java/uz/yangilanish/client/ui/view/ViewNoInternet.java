package uz.yangilanish.client.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;

import uz.yangilanish.client.R;


public class ViewNoInternet extends RelativeLayout {

    private AppCompatTextView errorTitle, errorText;

    public ViewNoInternet(Context context) {
        super(context);
        initControl(context);
    }

    public ViewNoInternet(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public ViewNoInternet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_no_network, this);

        errorTitle = findViewById(R.id.tv_error_title);
        errorText = findViewById(R.id.tv_error_text);

        setScreenContent();
    }

    public void setScreenContent() {
        errorTitle.setText(getContext().getString(R.string.no_internet));
        errorText.setText(getContext().getString(R.string.no_internet_text));
    }
}