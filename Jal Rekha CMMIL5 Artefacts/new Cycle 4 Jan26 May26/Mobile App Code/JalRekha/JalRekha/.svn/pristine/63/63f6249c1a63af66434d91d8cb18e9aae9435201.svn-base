package mapitgis.jalnigam.core;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.Objects;

import mapitgis.jalnigam.R;

public class ProgressDialog {
    private static Dialog dialog;

    public static void startDialog(@NonNull Context context) {
        stopDialog();
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_api_call);
        AppCompatTextView textView = dialog.findViewById(R.id.textView);
        textView.setText(R.string.saving_data_locally);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public static void startDialog(@NonNull Context context,@NonNull String message) {
        stopDialog();
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_api_call);
        AppCompatTextView textView = dialog.findViewById(R.id.textView);
        textView.setText(message);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public static void startDialog(@NonNull Context context,@StringRes int message) {
        stopDialog();
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_api_call);
        AppCompatTextView textView = dialog.findViewById(R.id.textView);
        textView.setText(message);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public static void stopDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}
