package com.hout.HoutMobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.hout.api.ClientApi;
import com.hout.domain.entities.Meetup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MeetupDetailsActivity extends Activity {

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    ClientApi api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetup_details);

        Bundle extras = getIntent().getExtras();
        api = (ClientApi)extras.getSerializable("api");

        Meetup meetup = (Meetup)extras.getSerializable("meetup");
        if(meetup ==  null) {
            try {
                meetup = api.getMeetupDetails(extras.getLong("meetupId"));
            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Could not retrieve meetup!");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.show();
                return;
            }
        }

        TextView desc = (TextView) findViewById(R.id.detailsViewDescription);
        TextView created = (TextView)findViewById(R.id.detailsViewCreated);
        TextView location = (TextView) findViewById(R.id.detailsViewLocation);
        TextView time = (TextView) findViewById(R.id.detailsViewTime);
        TextView timeText = (TextView) findViewById(R.id.detailsTextTime);
        TextView locationText = (TextView) findViewById(R.id.detailsTextLocation);

        desc.setText(meetup.getDescription());
        created.setText(df.format(meetup.getCreatedDate()));
        if(meetup.getFinalizedDate() != null)  {
            location.setText(meetup.getFinalizedLocation().getLocation());
            time.setText(df.format(meetup.getFinalizedDate()));
        } else {
            locationText.setVisibility(TextView.INVISIBLE);
            timeText.setVisibility(TextView.INVISIBLE);
        }

        CheckBox suggestions = (CheckBox) findViewById(R.id.detailsSuggestionsCheckBox);
        CheckBox facebook = (CheckBox) findViewById(R.id.detailsFacebookCheckBox);
        CheckBox twitter = (CheckBox) findViewById(R.id.detailsTwitterCheckBox);

        suggestions.setChecked(meetup.isSuggestionAllowed());
        facebook.setChecked(meetup.isFacebookSharing());
        twitter.setChecked(meetup.isTwitterSharing());

        final Button checkSuggestionsButton = (Button) findViewById(R.id.checkSuggestionsButton);
        final Meetup finalMeetup = meetup;
        checkSuggestionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent checkSuggestionsActivity = new Intent(getApplicationContext(), SuggestionListActivity.class);
                checkSuggestionsActivity.putExtra("meetup", finalMeetup);
                checkSuggestionsActivity.putExtra("api", api);
                startActivity(checkSuggestionsActivity);
            }
        });
    }
}
