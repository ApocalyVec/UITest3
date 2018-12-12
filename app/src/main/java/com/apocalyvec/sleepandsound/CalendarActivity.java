package com.apocalyvec.sleepandsound;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalendarView";

    private CalendarView mCandendarView;
    private Button btnConfirmDate;

    private int mYear;
    private int mMonth;
    private int mDayOfMonth;

    private Date selectedDate;
    private Date currentDate;

    private int currYear;
    private int currMonth;
    private int currDayOfMonth;

    private Calendar mCalendar;

    private String KID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendar = Calendar.getInstance();

        //set initial dates
        mYear = currYear =mCalendar.get(Calendar.YEAR);
        mMonth = currMonth =mCalendar.get(Calendar.MONTH);
        mDayOfMonth = currDayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = currentDate = new Date(mCalendar.getTimeInMillis());


        mCandendarView = findViewById(R.id.calendarView);
        btnConfirmDate = findViewById(R.id.btn_confirmDate);

        KID = getIntent().getExtras().get("KID").toString();

        //captures date change event
        mCandendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDayOfMonth = dayOfMonth;
//                SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
//                try{
//                    selectedDate = myFormat.parse(Integer.toString(dayOfMonth) + Integer.toString(month+1) + Integer.toString(year));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                String date = year + "/" + month + "/" +dayOfMonth;

                LocalDate localDate = new LocalDate(year, (month+1), dayOfMonth);
                selectedDate = localDate.toDate();

                Log.d(TAG, "onSelectedDayChange: date: " + date);

//
//                Log.d(TAG, "selectedDate: date: " + selectedDate.toString());
            }
        });

        btnConfirmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mYear > currYear || mMonth > currMonth || mDayOfMonth > currDayOfMonth) {
                    Toast.makeText(CalendarActivity.this, "Please Select today or a day before today", Toast.LENGTH_SHORT).show();
                }
                else {


                    Log.d(TAG, "Selected Date: " + selectedDate.toString());
                    Log.d(TAG, "Current Date: " + currentDate.toString());

                    long dayDifference = getDifferenceDays(selectedDate, currentDate);



                    Log.d(TAG, "Date Difference is: " +  dayDifference);

                    Intent childViewIntent = new Intent(CalendarActivity.this, ChildViewActivity.class);
                    childViewIntent.putExtra("KID", KID);
                    childViewIntent.putExtra("SelectedDate", selectedDate);
                    childViewIntent.putExtra("dateDiff", dayDifference);
                    startActivity(childViewIntent);

                }
            }
        });
    }

    private static long getDifferenceDays(Date d1, Date d2) {

        Days diffDays = Days.daysBetween(LocalDate.fromDateFields(d1), LocalDate.fromDateFields(d2));
//        long diff = d1.getTime() - d2.getTime();
//
//        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        return diffDays.getDays();
//        System.out.println("difference between days: " + diffDays);
//
//        long diff = d2.getTime() - d1.getTime();
//        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
