package uz.yangilanish.client.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.TimerTask;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.yangilanish.client.R;
import uz.yangilanish.client.act.actions.AuthAction;
import uz.yangilanish.client.data.api.ApiBuilder;
import uz.yangilanish.client.data.dto.auth.SmsConfirmRequest;
import uz.yangilanish.client.data.dto.auth.SmsConfirmResponse;
import uz.yangilanish.client.data.dto.response.BadResponse;
import uz.yangilanish.client.events.CheckNetworkBus;
import uz.yangilanish.client.ui.layout.LayoutActivity;
import uz.yangilanish.client.ui.view.ViewNumPad;
import uz.yangilanish.client.utils.NumberFormat;


public class SmsConfirmActivity extends LayoutActivity implements ViewNumPad.OnNumPadClickListener {

    private BroadcastReceiver broadcastReceiver;

    private AppCompatTextView phoneNumber;
    private AppCompatTextView smsCode;
    private AppCompatButton btnSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLocale();
        setContentView(R.layout.activity_sms_confirm);

        EventBus.getDefault().register(this);

        setContentActivity();
        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        unregisterReceiver(this.broadcastReceiver);

        stopTimer();
    }

    /* Initializing views */
    private void setContentActivity() {
        smsCode = findViewById(R.id.tv_sms_code);
        phoneNumber = findViewById(R.id.tv_phone_number);
        btnSendCode = findViewById(R.id.btn_send);

        viewNoGps = findViewById(R.id.view_no_gps);
        viewNoInternet = findViewById(R.id.view_no_internet);
        progressBar = findViewById(R.id.rl_progress_bar);
        ViewNumPad viewNumPad = findViewById(R.id.num_pad);

        viewNumPad.onClickNumPad(this);
        btnSendCode.setOnClickListener(v -> sendSmsCode());

        phoneNumber.setText(getIntent().getStringExtra("phone"));
        String phone1 = getIntent().getStringExtra("phone1");
        String phone2 = getIntent().getStringExtra("phone2");

        initSmsAutoReceiver(phone1, phone2);
    }

    /* Autofill SMS code */
    private void initSmsAutoReceiver(String phone1, String phone2) {
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }

                try {
                    SmsMessage smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent)[0];
                    String messageAddress = smsMessage.getOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();

                    if (messageAddress.equals(phone1) || messageAddress.equals(phone2)) {
                        Pattern pattern = Pattern.compile("[^0-9]");
                        String otp = pattern.matcher(messageBody).replaceAll("");

                        onKeyDown(otp);
                        sendSmsCode();
                    }
                } catch (Exception e) {
                    SmsConfirmActivity.this.closeCurrentActivity();
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }

    /* Sms code */
    @Override
    public void onKeyDown(String number) {
        String text = this.smsCode.getText().toString();
        String unmasked = NumberFormat.unmaskedCode(text);

        if (unmasked.length() <= 3) {
            unmasked += number;
            this.smsCode.setText(NumberFormat.maskedCode(unmasked));

            if (unmasked.length() == 4) {
                this.btnSendCode.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDelete() {
        String text = this.smsCode.getText().toString();
        String unmasked = NumberFormat.unmaskedCode(text);

        if (unmasked.length() > 0 && unmasked.length() <= 4) {
            String code = unmasked.substring(0, unmasked.length() - 1);
            this.smsCode.setText(NumberFormat.maskedCode(code));

            if (unmasked.length() == 4) {
                this.btnSendCode.setVisibility(View.GONE);
            }
        }
    }

    /* Send code */
    private void sendSmsCode() {
        if (!gpsIsEnabled() || !internetIsEnabled()) {
            return;
        }

        showLoading();

        SmsConfirmRequest request = new SmsConfirmRequest();
        request.setPhone(NumberFormat.unmasked(phoneNumber.getText().toString()));
        request.setSmsCode(NumberFormat.getSmsCode(NumberFormat.unmaskedCode(smsCode.getText().toString())));

        ApiBuilder.api().smsConfirm(request).enqueue(new Callback<SmsConfirmResponse>() {
            @Override
            public void onResponse(@NonNull Call<SmsConfirmResponse> call, @NonNull Response<SmsConfirmResponse> response) {
                hideLoading();

                if (!response.isSuccessful()) {
                    assert response.errorBody() != null; // Throw
                    String errorMessage = BadResponse.getInstance(response.errorBody()).getMsg();
                    showErrorDialog(errorMessage);

                    return;
                }

                AuthAction action = new AuthAction();
                assert response.body() != null;
                action.smsConfirm(response.body());

                openMapActivity();
                closeCurrentActivity();
            }

            @Override
            public void onFailure(@NonNull Call<SmsConfirmResponse> call, @NonNull Throwable t) {
                hideLoading();
                showServerErrorDialog();
            }
        });
    }

    @Override
    protected void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post(new CheckNetworkBus());
            }
        };

        super.startTimer();
    }

    /* Events */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(CheckNetworkBus bus) {
        if (this.internetIsEnabled()) {
            viewNoInternet.setVisibility(View.GONE);

            if (this.gpsIsEnabled()) {
                viewNoGps.setVisibility(View.GONE);
            } else {
                viewNoGps.setVisibility(View.VISIBLE);
            }
        } else {
            viewNoInternet.setVisibility(View.VISIBLE);
        }
    }
}