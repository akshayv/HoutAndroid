package com.hout.HoutMobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.hout.api.ClientApi;
import com.hout.api.mock.ClientApiMock;
import com.hout.domain.entities.Meetup;

import java.util.Calendar;


public class CreateSuggestionActivity extends Activity {

    ClientApi api;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_suggestion);

        Bundle extras = getIntent().getExtras();
        final Meetup meetup = (Meetup)extras.getSerializable("meetup");
        api = (ClientApi)extras.getSerializable("api");

        ((DatePicker)findViewById(R.id.datePickerSuggestionPage)).setCalendarViewShown(false);

        final Button button = (Button) findViewById(R.id.createSuggestionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCreateButtonClick(meetup.getId(), getApplicationContext());
            }
        });
    }

    public void onCreateButtonClick(long meetupId, Context applicationContext) {

        String location =  ((EditText) findViewById(R.id.newSuggestionLocation)).getText().toString();
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePickerSuggestionPage);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePickerSuggestionPage);
        timePicker.clearFocus();
        datePicker.clearFocus();
        int timeHour = timePicker.getCurrentHour();
        int timeMinute = timePicker.getCurrentMinute();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR, timeHour);
        cal.set(Calendar.MINUTE, timeMinute);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        try {
            api.addSuggestionForMeetup(meetupId, location, cal.getTime());
        }  catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Could not add Suggestion!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog.show();
            return;
        }

        Context context = getApplicationContext();
        CharSequence text = "Suggestion was added!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent redirectActivity = new Intent(getApplicationContext(), Hout.class);
        redirectActivity.putExtra("api", api);
        startActivity(redirectActivity);
    }
}
