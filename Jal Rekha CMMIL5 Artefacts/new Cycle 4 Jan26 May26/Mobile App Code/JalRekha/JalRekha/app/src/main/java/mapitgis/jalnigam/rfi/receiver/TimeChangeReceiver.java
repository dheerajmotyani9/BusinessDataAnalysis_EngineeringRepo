package mapitgis.jalnigam.rfi.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

public class TimeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if the system is using automatic date and time
        boolean isAutoTimeEnabled = isAutoTimeEnabled(context);

        if (!isAutoTimeEnabled) {
            // Show a dialog if automatic time is not set
            showAutoTimeDialog(context);
        }
    }

    // Method to check if automatic time is enabled
    private boolean isAutoTimeEnabled(Context context) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME) == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to show a dialog prompting the user to enable automatic time
    private void showAutoTimeDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Automatic Date/Time Disabled")
                .setMessage("Please enable automatic date and time in your device settings.")
                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open date and time settings
                        context.startActivity(new Intent(Settings.ACTION_DATE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false) // Prevent dialog from being dismissed by tapping outside
                .show();
    }
}
