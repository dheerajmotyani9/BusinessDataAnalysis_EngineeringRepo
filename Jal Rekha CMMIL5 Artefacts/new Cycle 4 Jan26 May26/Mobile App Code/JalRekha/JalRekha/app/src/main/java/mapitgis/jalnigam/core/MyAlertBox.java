package mapitgis.jalnigam.core;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.R;

public abstract class MyAlertBox {
    private final Context context;
    private final List<Dialog> dialog;

    public MyAlertBox(@NonNull Context context) {
        this.context = context;
        dialog = new ArrayList<>();
    }

    private void dismiss(@NonNull Dialog dialog) {
        dialog.dismiss();
    }

    private void show(@StringRes int title,@StringRes int message, @StringRes int button1, @StringRes int button2){

    }

    public Dialog show(@NonNull String title,@NonNull String message,@NonNull String button1,@NonNull String button2){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.upload_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
        LinearLayout linearLayout = dialog.findViewById(R.id.linearLayoutButton);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
//        this.buttonCancel = dialog.findViewById(R.id.buttonCancel);
//        this.buttonOK = dialog.findViewById(R.id.buttonOK);
//        this.imageViewIcon = dialog.findViewById(R.id.imageViewIcon);
//        this.buttonCancel.setOnClickListener(v->onCancel());
//        this.buttonOK.setOnClickListener(v->onOK());

        this.dialog.add(dialog);
        return dialog;
    }

    protected abstract void onOK();

    protected abstract void onCancel();

}
