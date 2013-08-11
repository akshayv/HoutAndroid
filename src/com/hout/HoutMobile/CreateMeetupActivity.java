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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meetup);

        api = (ClientApi) getIntent().getExtras().getSerializable("api");

        final Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkIfInviteesAdded();
            }
        });
        final ImageButton inviteButton = (ImageButton) findViewById(R.id.inviteUsersButton);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onInviteButtonClick();
            }
        });

        final ImageButton suggestDateButton = (ImageButton) findViewById(R.id.suggestDateImageButton);
        suggestDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSuggestDateButtonClick();
            }
        });

        findViewById(R.id.hideShowSharingRow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifySharingOptions();
            }
        });

        findViewById(R.id.hideShowSuggestionsRow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideShowUserSuggestions();
            }
        });

        setMeetupParametersFromExtra();
    }

    private void hideShowUserSuggestions() {
        if (findViewById(R.id.locationText).getVisibility() == View.GONE) {
            findViewById(R.id.locationText).setVisibility(View.VISIBLE);
            findViewById(R.id.suggestDateRelativeLayout).setVisibility(View.VISIBLE);
            ((ImageButton) findViewById(R.id.suggestionsCollapseExpandImage))
                    .setImageResource(R.drawable.navigation_collapse);
        } else {
            findViewById(R.id.locationText).setVisibility(View.GONE);
            findViewById(R.id.suggestDateRelativeLayout).setVisibility(View.GONE);
            ((ImageButton) findViewById(R.id.suggestionsCollapseExpandImage))
                    .setImageResource(R.drawable.navigation_expand);
        }
    }

    private void modifySharingOptions() {
        if (findViewById(R.id.facebookCheckBox).getVisibility() == View.GONE) {
            findViewById(R.id.facebookCheckBox).setVisibility(View.VISIBLE);
            findViewById(R.id.twiiterCheckbox).setVisibility(View.VISIBLE);
            ((ImageButton) findViewById(R.id.sharingCollapseExpandImage))
                    .setImageResource(R.drawable.navigation_collapse);
        } else {
            findViewById(R.id.facebookCheckBox).setVisibility(View.GONE);
            findViewById(R.id.twiiterCheckbox).setVisibility(View.GONE);
            ((ImageButton) findViewById(R.id.sharingCollapseExpandImage))
                    .setImageResource(R.drawable.navigation_expand);
        }
    }

    private void onSuggestDateButtonClick() {
        Intent redirectActivity = new Intent(getApplicationContext(), CreateMeetupDateSuggestionActivity.class);
        redirectActivity.putExtra("api", api);
        putMeetupParametersIntoExtra(redirectActivity);
        startActivity(redirectActivity);
    }

    private void setMeetupParametersFromExtra() {
        Bundle currentBundle = getIntent().getExtras();
        if (currentBundle.getString("description") != null) {
            ((EditText) findViewById(R.id.descText)).setText(currentBundle.getString("description"));
            ((EditText) findViewById(R.id.locationText)).setText(currentBundle.getString("suggestedLocation"));
            ((CheckBox) findViewById(R.id.facebookCheckBox)).setChecked(currentBundle.getBoolean("isFacebookSharing"));
            ((CheckBox) findViewById(R.id.twiiterCheckbox)).setChecked(
                    currentBundle.getBoolean("isTwitterSharing"));
            ((CheckBox) findViewById(R.id.suggestionsCheckbox)).setChecked(
                    currentBundle.getBoolean("isSuggestions"));
            if (currentBundle.getSerializable("invitees") != null) {
                @SuppressWarnings("unchecked")
                int size = ((ArrayList<Long>) currentBundle.getSerializable("invitees"))
                        .size();
                //noinspection StatementWithEmptyBody
                if (size != 0) {
                    //todo need to display number of invitees
//                    ((Button) findViewById(R.id.inviteUsersButton)).setText(
//                            (size + " invitees for meetup"));
                }
            }
        }
    }

    private void onInviteButtonClick() {
        Intent redirectActivity = new Intent(getApplicationContext(), RegisteredUsersListActivity.class);
        redirectActivity.putExtra("api", api);
        putMeetupParametersIntoExtra(redirectActivity);
        startActivity(redirectActivity);
    }

    private void putMeetupParametersIntoExtra(Intent redirectActivity) {
        redirectActivity.putExtra("description", ((EditText) findViewById(R.id.descText)).getText().toString());
        redirectActivity
                .putExtra("suggestedLocation", ((EditText) findViewById(R.id.locationText)).getText().toString());
        redirectActivity.putExtra("isFacebookSharing", ((CheckBox) findViewById(R.id.facebookCheckBox)).isChecked());
        redirectActivity.putExtra("isTwitterSharing", ((CheckBox) findViewById(R.id.twiiterCheckbox)).isChecked());
        redirectActivity.putExtra("isSuggestions", ((CheckBox) findViewById(R.id.suggestionsCheckbox)).isChecked());
        if (getIntent().getExtras().get("timeHour") != null) {
            redirectActivity.putExtra("timeHour", getIntent().getExtras().getInt("timeHour"));
            redirectActivity.putExtra("timeMinute", getIntent().getExtras().getInt("timeMinute"));
            redirectActivity.putExtra("year", getIntent().getExtras().getInt("year"));
            redirectActivity.putExtra("month", getIntent().getExtras().getInt("month"));
            redirectActivity.putExtra("day", getIntent().getExtras().getInt("day"));
        }
        if (getIntent().getExtras().get("invitees") != null) {
            redirectActivity.putExtra("invitees", getIntent().getExtras().getSerializable("invitees"));
        }
    }

    public void checkIfInviteesAdded() {
        Bundle currentExtras = getIntent().getExtras();
        List<Long> inviteeIds = new ArrayList<Long>();
        if (currentExtras.getSerializable("invitees") != null) {
            //noinspection unchecked
            inviteeIds = ((ArrayList<Long>) currentExtras.getSerializable("invitees"));
        }

        if (inviteeIds == null || inviteeIds.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Invitees?")
                    .setMessage("You have selected no invitees, are you sure you want to continue?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            onCreateButtonClick();
                        }
                    }).create().show();
        }
    }

    public void onCreateButtonClick() {
        Bundle currentExtras = getIntent().getExtras();
        String description = ((EditText) findViewById(R.id.descText)).getText().toString();
        String suggestedLocation = ((EditText) findViewById(R.id.locationText)).getText().toString();
        Date suggestedDate = null;
        if (currentExtras.get("timeHour") != null) {
            int timeHour = currentExtras.getInt("timeHour");
            int timeMinute = currentExtras.getInt("timeMinute");
            int year = currentExtras.getInt("year");
            int month = currentExtras.getInt("month");
            int day = currentExtras.getInt("day");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, timeHour);
            cal.set(Calendar.MINUTE, timeMinute);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            suggestedDate = cal.getTime();
        }

        Boolean isFacebookSharing = ((CheckBox) findViewById(R.id.facebookCheckBox)).isChecked();
        Boolean isTwitterSharing = ((CheckBox) findViewById(R.id.twiiterCheckbox)).isChecked();
        Boolean isSuggestions = ((CheckBox) findViewById(R.id.suggestionsCheckbox)).isChecked();

        List<Long> inviteeIds = new ArrayList<Long>();
        if (currentExtras.getSerializable("invitees") != null) {
            //noinspection unchecked
            inviteeIds = ((ArrayList<Long>) currentExtras.getSerializable("invitees"));
        }

        Log.v("log", "called createMeetup with Description=" + description +
                "&suggestedLocation=" + suggestedLocation +
                "&suggestedTime=" + suggestedDate +
                "&isFacebook=" + isFacebookSharing +
                "&isTwitter=" + isTwitterSharing +
                "&isSuggestions=" + isSuggestions +
                "&inviteeIds=" + inviteeIds);

        try {
            api.createMeetup(description, suggestedLocation, suggestedDate, isFacebookSharing, isTwitterSharing,
                    isSuggestions, inviteeIds);
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Cancel?")
                .setMessage("Are you sure you want to cancel?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent redirectActivity = new Intent(getApplicationContext(), Hout.class);
                        redirectActivity.putExtra("api", api);
                        startActivity(redirectActivity);
                    }
                }).create().show();
    }
}
