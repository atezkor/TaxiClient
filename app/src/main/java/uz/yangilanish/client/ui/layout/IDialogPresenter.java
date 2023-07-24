package uz.yangilanish.client.ui.layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import uz.yangilanish.client.R;
import uz.yangilanish.client.act.actions.AuthAction;


public interface IDialogPresenter {

    LayoutInflater getLayoutInflater();

    Context getContext();

    default void showErrorDialog(AlertDialog errorDialog, String message) {
        AlertDialog alertDialog = errorDialog;

        if (alertDialog == null || !alertDialog.isShowing()) {
            AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(getContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);

            AppCompatButton btnOk = dialogView.findViewById(R.id.btn_ok);
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_error_text)).setText(message);

            btnOk.setOnClickListener(v -> alertDialog.dismiss());

            errorDialogBuilder.setView(dialogView);
            errorDialog = errorDialogBuilder.create();

            errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            errorDialog.setCancelable(false);
            errorDialog.show();
        }
    }

    default void showServerErrorDialog(AlertDialog errorDialog, OnDialogButtonClickListener onClickListener) {
        Context context = getContext();
        if (errorDialog == null || !errorDialog.isShowing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);

            AppCompatButton btnOk = dialogView.findViewById(R.id.btn_ok);
            ((AppCompatTextView) dialogView.findViewById(R.id.tv_error_text)).setText(context.getString(R.string.connection_error));
            ((ImageView) dialogView.findViewById(R.id.error_image)).setImageResource(R.drawable.ic_servers);

            btnOk.setText(context.getString(R.string.change_phone_number));
            AlertDialog finalErrorDialog = errorDialog;
            btnOk.setOnClickListener(v -> {
                finalErrorDialog.dismiss();
                AuthAction.logout();
                onClickListener.onPositiveClick();
            });

            dialogView.findViewById(R.id.iv_btn_close).setOnClickListener(v -> onClickListener.onNegativeClick());

            dialogBuilder.setView(dialogView);
            errorDialog = dialogBuilder.create();

            errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            errorDialog.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode != 4) {
                    return true;
                }

                onClickListener.onNegativeClick();
                return true;
            });

            errorDialog.show();
        }
    }

    default void showUpdateDialog(AlertDialog updateAlertDialog, final String updateUrl, OnDialogButtonClickListener listener) {
        if (updateAlertDialog == null || !updateAlertDialog.isShowing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_update, null);
            dialogView.setBackground(new ColorDrawable(Color.TRANSPARENT));

            (dialogView.findViewById(R.id.btn_update)).setOnClickListener(v -> listener.onPositiveClick());

            dialogBuilder.setView(dialogView);
            updateAlertDialog = dialogBuilder.create();
            updateAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            updateAlertDialog.setCancelable(false);
            updateAlertDialog.show();
        }
    }

    interface OnDialogButtonClickListener {

        void onPositiveClick();

        default void onNegativeClick() {

        }
    }
}
