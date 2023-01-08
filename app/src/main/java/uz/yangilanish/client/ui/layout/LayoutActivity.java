package uz.yangilanish.client.ui.layout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.Timer;
import java.util.TimerTask;

import uz.yangilanish.client.R;
import uz.yangilanish.client.actions.AuthAction;
import uz.yangilanish.client.ui.MainActivity;
import uz.yangilanish.client.ui.view.ViewNoGps;
import uz.yangilanish.client.ui.view.ViewNoInternet;


public class LayoutActivity extends MainActivity {

    protected Timer timer;
    protected TimerTask timerTask;

    protected AlertDialog errorDialog;
    protected AlertDialog updateAlertDialog;

    protected ViewNoGps viewNoGps;
    protected ViewNoInternet viewNoInternet;

    protected RelativeLayout progressBar;

    protected SpinKitView spinKitLoader;

    /* Dialogs */
    protected void showErrorDialog(String message) {
        AlertDialog alertDialog = errorDialog;

        if (alertDialog == null || !alertDialog.isShowing()) {
            AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);

            AppCompatButton btnOk = dialogView.findViewById(R.id.btn_ok);
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_error_text)).setText(message);

            btnOk.setOnClickListener(v -> errorDialog.dismiss());

            errorDialogBuilder.setView(dialogView);
            errorDialog = errorDialogBuilder.create();

            errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            errorDialog.setCancelable(false);
            errorDialog.show();
        }
    }

    public void showServerErrorDialog() {
        if (errorDialog == null || !errorDialog.isShowing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);

            AppCompatButton btnOk = dialogView.findViewById(R.id.btn_ok);
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_error_text)).setText(getString(R.string.connection_error));
            ((ImageView) dialogView.findViewById(R.id.error_image)).setImageResource(R.drawable.ic_servers);

            btnOk.setText(getString(R.string.change_phone_number));
            btnOk.setOnClickListener(v -> {
                errorDialog.dismiss();
                AuthAction.logout();

                openLoginActivity();
                closeCurrentActivity();
            });

            dialogView.findViewById(R.id.iv_btn_close).setOnClickListener(v -> closeCurrentActivity());

            dialogBuilder.setView(dialogView);
            errorDialog = dialogBuilder.create();

            errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            errorDialog.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode != 4) {
                    return true;
                }

                closeCurrentActivity();
                return true;
            });

            errorDialog.show();
        }
    }

    protected void showUpdateDialog(final String updateUrl) {
        if (updateAlertDialog == null || !updateAlertDialog.isShowing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_update, null);
            dialogView.setBackground(new ColorDrawable(Color.TRANSPARENT));

            (dialogView.findViewById(R.id.btn_update)).setOnClickListener(v -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)));
                closeCurrentActivity();
            });

            dialogBuilder.setView(dialogView);
            updateAlertDialog = dialogBuilder.create();
            updateAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            updateAlertDialog.setCancelable(false);
            updateAlertDialog.show();
        }
    }


    protected void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        if (spinKitLoader != null) {
            spinKitLoader.setIndeterminateDrawable(new Circle());
            spinKitLoader.setVisibility(View.VISIBLE);
        }
    }

    protected void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        if (spinKitLoader != null) {
            spinKitLoader.setVisibility(View.INVISIBLE);
        }
    }


    /* Timer side */
    protected void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 1000, 3000);
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
}
