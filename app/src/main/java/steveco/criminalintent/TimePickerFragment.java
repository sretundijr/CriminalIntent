package steveco.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by sretu on 4/1/2016.
 */
public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME = "steveco.criminalintent.time";
    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date time){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedOnInstanceState) {


        Date time = (Date) getArguments().getSerializable(ARG_TIME);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                int hour = mTimePicker.getCurrentHour();
//                                int min = mTimePicker.getCurrentMinute();
//                                mTimePicker.setHours(hour);
//                                mTimePicker.setMinutes(min);
//                                sendResult(Activity.RESULT_OK, time);
                                Date time = (Date) getArguments().getSerializable(ARG_TIME);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(time);

                                int hour = mTimePicker.getCurrentHour();
                                int minute = mTimePicker.getCurrentMinute();
                                time.setHours(hour);
                                time.setMinutes(minute);

                                sendResult(Activity.RESULT_OK, time);
                            }
                        }).create();

    }

    //this method creates and intent extra to send the date back to crime frag
    private void sendResult(int resultCode, Date time){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
