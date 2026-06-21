package mapitgis.jalnigam.rfi.helper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SingleDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private SingleDateListener listener;
    private boolean isDateExtended;
    private boolean isCurrentMin;

    public SingleDatePicker(SingleDateListener listener,boolean isDateExtended,boolean isCurrentMin) {
        this.listener = listener;
        this.isDateExtended = isDateExtended;
        this.isCurrentMin = isCurrentMin;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar mCalender = Calendar.getInstance();
        int year = mCalender.get(Calendar.YEAR);
        int month = mCalender.get(Calendar.MONTH);
        int dayOfMonth = mCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);

        if (isDateExtended){
            mCalender.add(Calendar.DAY_OF_MONTH, extendedDate());
        }else{
            mCalender.add(Calendar.DAY_OF_MONTH, 1);
        }

        if(isCurrentMin){
            pickerDialog.getDatePicker().setMinDate(mCalender.getTimeInMillis());
        }

        return pickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listener.onDateSelected(view, year, month, dayOfMonth);
    }
    public int extendedDate() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format (0 - 23)
        int minute = calendar.get(Calendar.MINUTE); // Minutes

        // Define the start and end hours (8:00 AM to 8:00 PM)
        int startHour = 8; // 8:00 AM
        int endHour = 21;  // 9:00 PM (21:00 in 24-hour format)

        // Check if current time is between 8:00 AM and 8:00 PM
        if (hour >= startHour && hour < endHour) {
            return 2; // Enable the button
        } else {
            return 3; // Disable the button
        }
    }

    public interface SingleDateListener {
        void onDateSelected(DatePicker view, int year, int month, int dayOfMonth);
    }

    public void showTimePickerDialog(Context context, String type, TimePickerListener listener) {
        int hourOfDay = 0; // Initial hour
        int minute = 0;   // Initial minute

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedTime = hourOfDay + ":" + minute;
                        listener.onTimePicked(selectedTime, type);
                    }
                },
                hourOfDay,
                minute,
                false // Set to true if you want 24-hour format
        );

        timePickerDialog.show();
    }

    public static String calculateTotalTime(List<Date> dates) {
        long totalTimeMillis = 0;

        // Ensure there are at least two dates to calculate a difference
        if (dates != null && dates.size() > 1) {
            for (int i = 0; i < dates.size() - 1; i++) {
                Date startDate = dates.get(i);
                Date endDate = dates.get(i + 1);

                // Calculate the difference in milliseconds
                long diffMillis = endDate.getTime() - startDate.getTime();
                totalTimeMillis += diffMillis;
            }
        }

        long hours = TimeUnit.MILLISECONDS.toHours(totalTimeMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeMillis) % 60;

        return String.format("%02d hrs %02d min %02d sec", hours, minutes, seconds);
    }

    public interface TimePickerListener {
        void onTimePicked(String time, String type);
    }

    public static String formatDateDDMMMYYYY(String originalDate) {
        // Define the original date format
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        // Define the desired format
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date date;
        String formattedDate = "";

        try {
            // Parse the original date string to a Date object
            date = originalFormat.parse(originalDate);

            // Format the Date object into the target format
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String getCurrentDate(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        return dateFormat.format(currentDate);
    }

    public static String getCurrentTime(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        return dateFormat.format(currentDate);
    }

}
