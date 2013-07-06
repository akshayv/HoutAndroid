package com.hout.HoutMobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.hout.api.ClientApi;
import com.hout.api.mock.ClientApiMock;
import com.hout.domain.entities.Meetup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class MeetupListActivity extends Activity {

    ClientApi api;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetups);

        api = (ClientApi)getIntent().getExtras().getSerializable("api");

        final ArrayList<HashMap<String, String>> meetupList = new ArrayList<HashMap<String, String>>();

        final List<Meetup> meetups;

        Calendar fromDate = Calendar.getInstance();
        fromDate.add(Calendar.DATE, -7);
        try {
            meetups = api.getMeetupsForTimeRange(fromDate.getTime(), new Date());
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Could not retrieve meetups!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog.show();
            return;
        }

        ListView list=(ListView)findViewById(R.id.meetupListView);

        for(Meetup meetup : meetups) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("id", String.valueOf(meetup.getId()));
            data.put("description", meetup.getDescription());
            data.put("createdDate", df.format(meetup.getCreatedDate()));
            if(meetup.getFinalizedDate() != null) {
                data.put("finalizedDate", df.format(meetup.getFinalizedDate()));
                data.put("finalizedLocation", meetup.getFinalizedLocation().getLocation());
            }
            meetupList.add(data);
        }

        LazyAdapter adapter=new LazyAdapter(this, meetupList);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Long meetupId = Long.parseLong(meetupList.get(position).get("id"));

                Meetup selected = null;
                for(Meetup meetup : meetups) {
                    if(meetup.getId() == meetupId) {
                        selected = meetup;
                        break;
                    }
                }
                Intent meetupDetailsActivity = new Intent(getApplicationContext(), MeetupDetailsActivity.class);
                meetupDetailsActivity.putExtra("meetup", selected);
                meetupDetailsActivity.putExtra("api", api);
                startActivity(meetupDetailsActivity);
            }
        });
    }
}

class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.meetup_row, null);

        TextView desc = (TextView)vi.findViewById(R.id.descriptionTextDisplay);
        TextView finalizedDate = (TextView)vi.findViewById(R.id.finalizedDateText);
        TextView createdDate = (TextView)vi.findViewById(R.id.dateCreated);
        TextView finalizedLocation = (TextView)vi.findViewById(R.id.finalizedLocationText);

        HashMap<String, String> meetup = new HashMap<String, String>();
        meetup = data.get(position);

        // Setting all values in listview
        desc.setText(meetup.get("description"));
        createdDate.setText(meetup.get("createdDate"));
        if(meetup.get("finalizedDate") != null) {
            finalizedDate.setText(meetup.get("finalizedDate"));
            finalizedLocation.setText(meetup.get("finalizedLocation"));
        } else {

            finalizedDate.setText(meetup.get(""));
            finalizedLocation.setText(meetup.get(""));
        }
        return vi;
    }
}


