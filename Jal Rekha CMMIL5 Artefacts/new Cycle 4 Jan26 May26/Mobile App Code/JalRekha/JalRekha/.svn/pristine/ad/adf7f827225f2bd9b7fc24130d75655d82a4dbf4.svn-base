/**
 * Created on : 21-08-2024
 * Author     : Jameel Khan
 * Email      : khanjameel353@gmail.com
 */

package mapitgis.jalnigam.rfi.helper;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        saveLogToFile("JalRekhaErrorLogs.txt");
    }

    public void saveLogToFile(String fileName) {

        File docFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File logFile = new File(docFolder, fileName);

        try {
            // Create the file if it doesn't exist
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            // Get the logcat output and write to the file
//            Process process = Runtime.getRuntime().exec("logcat -d");
           // String[] cmd = {"logcat", "-f", logFile.getAbsolutePath(), "AndroidRuntime:E", "*:S"};
        //    Process process = Runtime.getRuntime().exec(cmd);
            Process process = Runtime.getRuntime().exec("logcat -d -D -v long " + "YOUR_APPS_TAG" + ":V *:E");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));

            bufferedWriter.write("-----------------------------------------\n");
            bufferedWriter.write("Log captured on: " + new Date() + "\n\n");

            // Read the logcat output and write to the file
            String line;
            while ((line = reader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            reader.close();
            process.destroy();

            Log.d("TAG", "Log saved to " + logFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("TAG", "Failed to save log to file", e);
        }
    }

}
