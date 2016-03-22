package qianjun.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by john on 2016/3/15.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "qianjun.android.criminalintent.date";
    private static final String TAG = "DatePickerFragment";

    private Date mDate;

    public static DatePickerFragment newInstance(Date date){
        Log.i(TAG, "newInstance() start" + date);
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog() start");
        // 获取参数
        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);

        // Create a Calendar to get the year, month, and day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view =getActivity().getLayoutInflater()
                    .inflate(R.layout.dialog_date, null);
        // 在指定的视图位置显示时间
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Translate year, month, day into a Date Object using a calendar
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();

                // Update argument to preserve selected value on rotation
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
//                .setPositiveButton(android.R.string.ok, null)  点击确定按钮后出发的动作
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }

    /**
     * 同一个activity托管两个fragment间数据返回数据时，借助Fragment.onActivityResult()
     * 1，一个传入setTargetFragment()方法相匹配的请求代码，用以告知目标的
     * @param resultCode
     */
    private void sendResult(int resultCode){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
