package com.hout.HoutMobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.text.DateFormat;
import java.util.Date;

public class CreateMeetupDateSuggestionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetup_suggest_date);

        ((DatePicker) findViewById(R.id.datePicker)).setCalendarViewShown(false);
        findViewById(R.id.meetup_date_done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDoneButtonClick(true);
            }
        });

        loadPreviousSuggestedDate();
    }

    private void loadPreviousSuggestedDate() {
        Bundle currentExtras = getIntent().getExtras();
        if (currentExtras.getInt("timeHour") != 0) {
            TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
            DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
            timePicker.setCurrentHour(currentExtras.getInt("timeHour"));
            timePicker.setCurrentMinute(currentExtras.getInt("timeMinute"));
            datePicker.updateDate(currentExtras.getInt("year"), currentExtras.getInt("month"),
                    currentExtras.getInt("day"));
        }
    }

    private void putMeetupParametersIntoExtra(Intent redirectActivity) {
        Bundle currentBundle = getIntent().getExtras();
        redirectActivity.putExtra("description", currentBundle.getSerializable("description"));
        redirectActivity.putExtra("suggestedLocation", currentBundle.getSerializable("suggestedLocation"));
        redirectActivity.putExtra("isFacebookSharing", currentBundle.getSerializable("isFacebookSharing"));
        redirectActivity.putExtra("isTwitterSharing", currentBundle.getSerializable("isTwitterSharing"));
        redirectActivity.putExtra("isSuggestions", currentBundle.getSerializable("isSuggestions"));
        if (getIntent().getExtras().get("invitees") != null) {
            redirectActivity.putExtra("invitees", getIntent().getExtras().getSerializable("invitees"));
        }
    }

    private Date putDateParametersIntoExtra(Intent redirectActivity) {
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker.clearFocus();
        datePicker.clearFocus();
        redirectActivity.putExtra("timeHour", timePicker.getCurrentHour());
        redirectActivity.putExtra("timeMinute", timePicker.getCurrentMinute());
        redirectActivity.putExtra("year", datePicker.getYear());
        redirectActivity.putExtra("month", datePicker.getMonth());
        redirectActivity.putExtra("day", datePicker.getDayOfMonth());
        return new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    }

    public void onDoneButtonClick(boolean isDoneButtonPressed) {
        Intent redirectActivity = new Intent(getApplicationContext(), CreateMeetupActivity.class);
        redirectActivity.putExtra("api", getIntent().getExtras().getSerializable("api"));
        putMeetupParametersIntoExtra(redirectActivity);
        if(isDoneButtonPressed) {
            Date selectedDate = putDateParametersIntoExtra(redirectActivity);
            Context context = getApplicationContext();
            CharSequence text = "Selected " + DateFormat.getDateTimeInstance().format(selectedDate);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
        startActivity(redirectActivity);
    }

    @Override
    public void onBackPressed() {
        onDoneButtonClick(false);

    }
}
