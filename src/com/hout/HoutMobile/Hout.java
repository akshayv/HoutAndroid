package com.hout.HoutMobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.hout.api.ClientApi;
import com.hout.api.impl.ClientApiImpl;
import com.hout.api.mock.ClientApiMock;
import com.hout.domain.entities.User;

public class Hout extends Activity {

    ClientApi api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Bundle extras = getIntent().getExtras();
        api = (ClientApi)extras.getSerializable("api");
        User user;
        try {
           user = api.getUserDetails();
        }catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Could not retrieve User Details!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog.show();
            return;
        }

        TextView userName = (TextView) findViewById(R.id.userNameTextView);
        userName.setText(user.getName());

        final Button createMeetupsButton = (Button) findViewById(R.id.redirectCreateButton);
        final Button checkMeetupsButton = (Button) findViewById(R.id.redirectCheckMeetupsButton);
        final Button checkNotificationsButton = (Button) findViewById(R.id.redirectNotificationsButton);
        checkMeetupsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent checkMeetupsActivity = new Intent(getApplicationContext(), MeetupListActivity.class);
                checkMeetupsActivity.putExtra("api", api);
                startActivity(checkMeetupsActivity);
            }
        });
        createMeetupsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent createMeetupActivity = new Intent(getApplicationContext(), CreateMeetupActivity.class);
                createMeetupActivity.putExtra("api", api);
                startActivity(createMeetupActivity);
            }
        });
        checkNotificationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent checkNotificationActivity = new Intent(getApplicationContext(), NotificationsListViewActivity.class);
                checkNotificationActivity.putExtra("api", api);
                startActivity(checkNotificationActivity);
            }
        });
    }

}
