package mapitgis.jalnigam.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import mapitgis.jalnigam.BuildConfig;

public class DatabaseMailer {
    public static void emailDatabase(Context context, String databaseName) {
        try {
            // Get database path
            File dbFile = context.getDatabasePath(databaseName);
            if (!dbFile.exists()) {
                Utility.show(context, "Database file does not exist.");
                return;
            }

            // Copy database to cache directory (for sharing)
            File cacheDir = new File(context.getCacheDir(), "db_backup");
            if (!cacheDir.exists()) cacheDir.mkdirs();

            File backupFile = new File(cacheDir, databaseName);
            copyFile(dbFile, backupFile);

            // Get content URI using FileProvider
            Uri uri = FileProvider.getUriForFile(context, String.format("%s.fileProvider", BuildConfig.APPLICATION_ID), backupFile);

            // Create email intent
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("application/octet-stream");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"m4nish.cse@gmail.com"}); // Add recipient
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Database Backup");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Attached is the database backup.");
            emailIntent.putExtra("jid", "918959177684@s.whatsapp.net");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Start email chooser
            context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (Exception e) {
            Log.e("Database Mail", "Error sending database", e);
        }
    }

    private static void copyFile(File source, File destination) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
        }

        fis.close();
        fos.close();
    }
}
