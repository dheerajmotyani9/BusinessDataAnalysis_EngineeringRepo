package mapitgis.jalnigam.core;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapitgis.jalnigam.BuildConfig;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.helper.PrefManager;

public class ApiCaller {
    private final Activity activity;
    private final ApiListener apiListener;
    private final int key;
    private final String data;
    private final String dialogMessage;
    private final Method method;
    private Dialog dialog;
    private TextView textView;

    public static class Method {
        private final String method;

        public Method(String method) {
            this.method = method;
        }
    }

    public static final Method GET = new Method("GET");
    public static final Method POST = new Method("POST");

    /**
     * @param activity      for show dialog
     * @param apiListener   for handle result
     * @param key           for result detection
     * @param data          for data send to server
     * @param dialogMessage for show dialog
     */
    public ApiCaller(@NonNull Activity activity, @NonNull ApiListener apiListener, int key, @Nullable String data, @Nullable String dialogMessage) {
        this.activity = activity;
        this.apiListener = apiListener;
        this.key = key;
        this.method = data == null ? GET : POST;
        this.data = data;
        this.dialogMessage = dialogMessage;
        init();
    }

    /**
     * @param activity      for show dialog for handle result
     * @param key           for result detection
     * @param data          for data send to server
     * @param dialogMessage for show dialog
     */
    public <T extends ApiListener> ApiCaller(@NonNull T activity, int key, @Nullable String data, @Nullable String dialogMessage) {
        this.activity = (Activity) activity;
        this.key = key;
        this.method = data == null ? GET : POST;
        this.data = data;
        this.dialogMessage = dialogMessage;
        this.apiListener = activity;
        init();
    }

    /**
     * @param activity      for show dialog
     * @param apiListener   for handle result
     * @param key           for result detection
     * @param method        for methods of custom
     * @param data          for data send to server
     * @param dialogMessage for show dialog
     */
    public ApiCaller(@NonNull Activity activity, @NonNull ApiListener apiListener, int key, @NonNull Method method, @Nullable String data, @Nullable String dialogMessage) {
        this.activity = activity;
        this.apiListener = apiListener;
        this.key = key;
        this.data = data;
        this.dialogMessage = dialogMessage;
        this.method = method;
        init();
    }

    /**
     * @param activity      for show dialog for handle result
     * @param key           for result detection
     * @param method        for methods of custom
     * @param data          for data send to server
     * @param dialogMessage for show dialog
     */
    public <T extends ApiListener> ApiCaller(@NonNull T activity, int key, @NonNull Method method, @Nullable String data, @Nullable String dialogMessage) {
        this.activity = (Activity) activity;
        this.key = key;
        this.data = data;
        this.dialogMessage = dialogMessage;
        this.method = method;
        this.apiListener = activity;
        init();
    }

    private void init() {
        if (dialogMessage == null) {
            this.dialog = null;
            this.textView = null;
        } else {
            dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_api_call);
            AppCompatTextView textView = dialog.findViewById(R.id.textView);
            textView.setText(dialogMessage);
            dialog.setCancelable(false);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        }
    }

    private Handler handler;

    public void start(@NonNull String api) {
        final String url = (!(api.startsWith("http"))) ? String.format("%s%s", BuildConfig.JAL_NIGAM_BASE_URL, api) : api;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            //Background work here
            final String result = postData(url);
            if (BuildConfig.DEBUG) {
                Log.e("ABCD_URLL", url);
                Log.e("ABCD_DATA", data == null ? "N/A" : data);
                Log.e("ABCD_RESP", result);
            }
            handler.post(() -> {
                //UI Thread work here
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (result.toLowerCase().contains("session expire")) {
                    Utility.show(activity,"Session expired");
                    SqLite.instance(activity).logout();
                    Utility.goFirst(activity, true);
                    new PrefManager(activity).logout();
                    return;
                }
                apiListener.onResponse(result, key);
            });
        });
    }

    private void showPercentage(int percentage) {
        if (dialog != null && dialog.isShowing() && textView != null) {
            textView.setText("");
        }
        apiListener.webProgress(percentage, key);
    }

    @NonNull
    private String postData(@NonNull String url) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setConnectTimeout(5000);
//            urlConnection.setReadTimeout(20000);
            urlConnection.setRequestProperty("Accept-Encoding", "identity");
            urlConnection.setRequestProperty("user-agent", Utility.getUA());
            if (data != null) {
                if (data.contains("{")) {
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                } else {
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                }
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("X-HTTP-Method-Override", this.method.method);
                urlConnection.connect();
                DataOutputStream ws = new DataOutputStream(urlConnection.getOutputStream());
                ws.write(data.getBytes());
                ws.flush();
            } else {
                urlConnection.setRequestProperty("X-HTTP-Method-Override", this.method.method);
                urlConnection.connect();
            }
            InputStream is = urlConnection.getInputStream();
            if(apiListener.getInputStream()) {
                apiListener.onInputStream(is);
                return "{\"Success\":false,\"Message\":\"Input Stream Loaded\"}";
            }
            int contentLength = urlConnection.getContentLength();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            final int[] readBytes = {0};
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
                readBytes[0] += length;
                handler.post(() -> showPercentage((readBytes[0] * 100) / contentLength));
            }
            is.close();
            urlConnection.disconnect();
            return result.toString("UTF-8");
        } catch (UnknownHostException | FileNotFoundException | ConnectException e) {
            return "{\"Success\":false,\"Message\":\"Turn On Internet Connection\",\"internet\":true}";
        } catch (Exception e) {
            return "{\"Success\":false,\"Message\":\"" + e.getMessage() + "\"}";
        }
    }

//    @NonNull
//    private String postData(@NonNull String url) {
//        try {
//            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
//            urlConnection.setConnectTimeout(5000);
//            urlConnection.setReadTimeout(20000);
//            urlConnection.setRequestProperty("Accept-Encoding", "identity");
//            urlConnection.setRequestProperty("user-agent", Utility.getUA());
//            if (data != null) {
//                if (data.contains("{")) {
//                    urlConnection.setRequestProperty("Content-Type", "application/json");
//                } else {
//                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                }
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//                urlConnection.setRequestProperty("X-HTTP-Method-Override", this.method.method);
//                urlConnection.connect();
//                DataOutputStream ws = new DataOutputStream(urlConnection.getOutputStream());
//                ws.write(data.getBytes());
//                ws.flush();
//            } else {
//                urlConnection.setRequestProperty("X-HTTP-Method-Override", this.method.method);
//                urlConnection.connect();
//            }
//            InputStream is = urlConnection.getInputStream();
//            int contentLength = urlConnection.getContentLength();
//            ByteArrayOutputStream result = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            final int[] readBytes = {0};
//            int length;
//            while ((length = is.read(buffer)) != -1) {
//                result.write(buffer, 0, length);
//                readBytes[0] += length;
//                handler.post(() -> showPercentage((readBytes[0] * 100) / contentLength));
//            }
//            is.close();
//            return result.toString("UTF-8");
//        } catch (UnknownHostException | FileNotFoundException e) {
//            return "{\"Success\":false,\"Message\":\"Turn On Internet Connection\",\"internet\":true}";
//        } catch (Exception e) {
//            if (e.getMessage() == null) {
//                return "{\"Success\":false,\"Message\":\"Error\"}";
//            } else if (e.getMessage().toLowerCase(Locale.UK).contains("failed to connect to")) {
//                return "{\"Success\":false,\"Message\":\"Turn On Internet Connection\",\"internet\":true}";
//            } else {
//                return "{\"Success\":false,\"Message\":\"" + e.getMessage() + "\"}";
//            }
//        }
//    }
}