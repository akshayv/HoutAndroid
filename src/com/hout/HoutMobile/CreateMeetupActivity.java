package com.hout.HoutMobile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.hout.api.ClientApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateMeetupActivity extends Activity {

    ClientApi api;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meetup);

        api = (ClientApi)getIntent().getExtras().getSerializable("api");

        ((DatePicker)findViewById(R.id.datePicker)).setCalendarViewShown(false);

        final Button button = (Button) findViewById(R.id.createButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCreateButtonClick();
            }
        });
    }

    public void onCreateButtonClick() {
        String description =  ((EditText) findViewById(R.id.descText)).getText().toString();
        String suggestedLocation = ((EditText) findViewById(R.id.locationText)).getText().toString();
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
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

        Date suggestedDate = cal.getTime();

        Boolean isFacebookSharing = ((Switch) findViewById(R.id.fbSwitch)).isChecked();
        Boolean isTwitterSharing = ((Switch) findViewById(R.id.twitterSwitch)).isChecked();
        Boolean isSuggestions = ((Switch) findViewById(R.id.suggestionsSwitch)).isChecked();

        Log.v("log", "called createMeetup with Description=" + description +
                "suggestedLocation=" + suggestedLocation +
                "suggestedTime=" + suggestedDate +
                "isFacebook=" + isFacebookSharing +
                "isTwitter=" + isTwitterSharing +
                "isSuggestions=" + isSuggestions);

        try {
            List<Long> inviteeIds = new ArrayList<Long>();
            inviteeIds.add(1L);
            api.createMeetup(description, suggestedLocation, suggestedDate, isFacebookSharing, isTwitterSharing, isSuggestions, inviteeIds);
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Could not create Meetup!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog.show();
            return;
        }

        Context context = getApplicationContext();
        CharSequence text = "Meetup was created!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent redirectActivity = new Intent(getApplicationContext(), Hout.class);
        redirectActivity.putExtra("api", api);
        startActivity(redirectActivity);
    }
}
