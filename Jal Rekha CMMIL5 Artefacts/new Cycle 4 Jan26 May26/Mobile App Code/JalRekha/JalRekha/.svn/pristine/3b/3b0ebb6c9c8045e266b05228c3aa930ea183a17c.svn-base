package mapitgis.jalnigam.rfi.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


import mapitgis.jalnigam.R;

public class ProgressHelper {

    public static final String ERROR_MESSAGE = "Something went wrong!";
    private Activity context;
    private ProgressDialog progressDialog;

    public ProgressHelper(Activity context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    //todo: show and dismiss progress...
    public void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgress() {
        progressDialog.dismiss();
    }

    public void message(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // TODO: 06-12-2020 : Show error and success dialog...
    public void showSuccessDialog(String message, String title, String type, String btnText, ShowDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_success_dialog_box, null);

        TextView btnCancel = view.findViewById(R.id.layout_success_dialog_btn_done);
        TextView titleTextView = view.findViewById(R.id.layout_success_dialog_title_text_view);
        TextView messageTextView = view.findViewById(R.id.layout_success_dialog_message_text_view);

        titleTextView.setText(title);
        messageTextView.setText(message);
        btnCancel.setText(btnText);

        AlertDialog dialog;
        builder.setView(view);
        dialog = builder.create();

        builder.setCancelable(false);
        dialog.setCancelable(false);


        btnCancel.setOnClickListener(view1 -> {

//            if (type.equalsIgnoreCase("back")) {
//                context.finish();
//            }
            listener.onShowDialogButtonClicked(type);
            dialog.cancel();
        });


        dialog.show();


    }

    public static void copy(String text, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(text, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied: " + text, Toast.LENGTH_SHORT).show();
    }

    // TODO: 28-07-2022 :
    public void showQuestionDialog(String title, String message, String btnYesText, String btnNoText, QuestionDialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.dialog_question_layout, null);

        TextView titleTextView = view.findViewById(R.id.layout_dialog_question_title_text_view);
        TextView messageTextView = view.findViewById(R.id.layout_dialog_question_message_text_view);
        Button btnOk = view.findViewById(R.id.dialog_question_btn_ok);
        Button btnCancel = view.findViewById(R.id.dialog_question_btn_cancel);

        titleTextView.setText(title);
        messageTextView.setText(message);
        btnOk.setText(btnYesText);
        btnCancel.setText(btnNoText);

        titleTextView.setText(title);
        messageTextView.setText(message);

        AlertDialog dialog;
        builder.setView(view);
        dialog = builder.create();

        builder.setCancelable(false);
        dialog.setCancelable(false);


        btnCancel.setOnClickListener(view1 -> {
            listener.onCancelQuestionDialog();
            dialog.cancel();
        });

        btnOk.setOnClickListener(v -> {
            listener.onQuestionDialog();
            dialog.cancel();
        });

        dialog.show();

    }

//    public void showMonitoringTypeDialog(DialogMonitoringTypeListener listener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view = inflater.inflate(R.layout.dialog_monitoring_type_login_layout, null);
//
//        CardView cardViewDI = view.findViewById(R.id.dialog_monitoring_daily_ins_login_card);
//        CardView cardViewRFI = view.findViewById(R.id.dialog_monitoring_rfi_login_card);
//        AlertDialog dialog;
//        builder.setView(view);
//        dialog = builder.create();
//
////        builder.setCancelable(false);
////        dialog.setCancelable(false);
//
//        //  viewPasswordImageView.setOnClickListener(v -> togglePassword());
//
//        cardViewDI.setOnClickListener(view1 -> {
//            dialog.cancel();
//            listener.onDailyInspectionClick();
//        });
//
//        cardViewRFI.setOnClickListener(v -> {
//            dialog.cancel();
//            listener.onRFIClick();
//        });
//
//
//        dialog.show();
//    }


    public void showAutoTimeDialog(Context context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Automatic Date/Time Disabled")
                .setMessage("Please enable automatic date and time in your device settings.")
                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open date and time settings
                        context.startActivity(new Intent(Settings.ACTION_DATE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       System.exit(0);
                    }
                })
                .setCancelable(false) // Prevent dialog from being dismissed by tapping outside
                .show();
    }

    public interface QuestionDialogListener {
        void onQuestionDialog();

        void onCancelQuestionDialog();
    }

    public interface ShowDialogListener {
        void onShowDialogButtonClicked(String type);
    }

    public boolean isAutoTimeEnabled(Context context) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME) == 1;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

}
