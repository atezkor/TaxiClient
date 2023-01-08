package uz.yangilanish.client.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import uz.yangilanish.client.R;


public class ViewLanguage extends RelativeLayout {

    public static String LANG_UZ = "uz";
    public static String LANG_RU = "ru";

    private LinearLayout langRu;

    private LinearLayout langUz;

    public ViewLanguage(Context context) {
        super(context);
        initControl(context);
    }

    public ViewLanguage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public ViewLanguage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_language, this);

        langUz = findViewById(R.id.ll_uz);
        langRu = findViewById(R.id.ll_ru);
    }

    public void setClickListenerUz(SelectLangListener listener) {
        langUz.setOnClickListener(v -> listener.onClick());
    }

    public void setClickListenerRu(SelectLangListener listener) {
        langRu.setOnClickListener(v -> listener.onClick());
    }

    public interface SelectLangListener {
        void onClick();
    }
}