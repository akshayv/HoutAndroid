package com.hout.HoutMobile;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.hout.api.ClientApi;
import com.hout.domain.entities.Notification;


import java.util.ArrayList;
import java.util.List;

public class NotificationsListViewActivity extends ListActivity {

    ClientApi api;

    List<Notification> notifications = new ArrayList<Notification>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        api = (ClientApi)getIntent().getExtras().getSerializable("api");

        try {
             notifications = api.getNotifications();
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Could not retrieve notifications!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog.show();
            return;
        }

        List<String> messages = new ArrayList<String>();
        for(Notification notification : notifications) {
            messages.add(notification.getMessage());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, messages);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Notification notification = notifications.get(position);
        Intent meetupDetailsActivity = new Intent(getApplicationContext(), MeetupDetailsActivity.class);
        meetupDetailsActivity.putExtra("api", api);
        meetupDetailsActivity.putExtra("meetupId", notification.getMeetupId());
        startActivity(meetupDetailsActivity);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
        }
    }

} 