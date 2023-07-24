package uz.yangilanish.client.ui.layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.Timer;
import java.util.TimerTask;

import uz.yangilanish.client.ui.MainActivity;
import uz.yangilanish.client.ui.view.ViewNoGps;
import uz.yangilanish.client.ui.view.ViewNoInternet;


public abstract class LayoutActivity extends MainActivity implements IDialogPresenter {

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
        showErrorDialog(errorDialog, message);
    }

    public void showServerErrorDialog() {
        showServerErrorDialog(errorDialog, new OnDialogButtonClickListener() {
            @Override
            public void onPositiveClick() {
                openLoginActivity();
                closeCurrentActivity();
            }

            @Override
            public void onNegativeClick() {
                closeCurrentActivity();
            }
        });
    }

    protected void showUpdateDialog(final String updateUrl) {
        showUpdateDialog(updateAlertDialog, updateUrl, () -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)));
            closeCurrentActivity();
        });
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

    @Override
    public Context getContext() {
        return this;
    }
}
