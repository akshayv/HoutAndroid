package com.hout.HoutMobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.hout.api.ClientApi;
import com.hout.domain.entities.Meetup;
import com.hout.domain.entities.SuggestionStatus;
import com.hout.domain.entities.Suggestion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuggestionListActivity extends Activity {

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    ClientApi api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetups_suggestions);

        Bundle extras = getIntent().getExtras();
        final Meetup meetup = (Meetup)extras.getSerializable("meetup");

        api = (ClientApi)extras.getSerializable("api");

        final Button addSuggestions = (Button) findViewById(R.id.addSuggestionButton);
        addSuggestions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newSuggestionActivity = new Intent(getApplicationContext(), CreateSuggestionActivity.class);
                newSuggestionActivity.putExtra("meetup", meetup);
                newSuggestionActivity.putExtra("api", api);
                startActivity(newSuggestionActivity);
            }
        });

        if(!meetup.isSuggestionAllowed()) {
            addSuggestions.setVisibility(View.INVISIBLE);
        }

        final ArrayList<HashMap<String, String>> suggestionList = new ArrayList<HashMap<String, String>>();

        final List<Suggestion> suggestions;
        try {
            suggestions = api.getSuggestionsForMeetup(meetup.getId());
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Could not retrieve suggestions!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            alertDialog.show();
            return;
        }

        ListView list=(ListView)findViewById(R.id.suggestionsListView);

        for(Suggestion suggestion : suggestions) {
            SuggestionStatus status = null;
            try {
                status = api.getUserSuggestionStatus(suggestion);
            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Could not retrieve suggestions");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialog.show();
                return;
            }
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("id", String.valueOf(suggestion.getId()));
            data.put("address", suggestion.getVenue().getAddress());
            data.put("location", suggestion.getVenue().getLocation());
            data.put("date", df.format(suggestion.getDate()));
            data.put("meetupId", String.valueOf(meetup.getId()));
            data.put("status", status.toString());
            suggestionList.add(data);
        }

        SuggestionAdaptor adapter=new SuggestionAdaptor(this, suggestionList,
                getApplicationContext(), api);
        list.setAdapter(adapter);
    }

}
    class SuggestionAdaptor extends BaseAdapter {

        @SuppressWarnings("FieldCanBeLocal")
        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private static LayoutInflater inflater=null;
        private Context context;
        ClientApi api;

        public SuggestionAdaptor(Activity a, ArrayList<HashMap<String, String>> d, Context context
                , ClientApi api) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.api = api;
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
                vi = inflater.inflate(R.layout.suggestions_row, null);

            TextView address = (TextView)vi.findViewById(R.id.suggestionAddress);
            TextView location = (TextView)vi.findViewById(R.id.suggestionLocation);
            TextView date = (TextView)vi.findViewById(R.id.suggestionTime);


            HashMap<String, String> suggestion;
            suggestion = data.get(position);


            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

            final long suggestionId = Long.parseLong(suggestion.get("id"));
            final long meetupId = Long.parseLong(suggestion.get("meetupId"));
            SuggestionStatus status = SuggestionStatus.valueOf(suggestion.get("status"));
            final Button yesSuggestion = (Button) vi.findViewById(R.id.suggestionYes);
            final Button noSuggestion = (Button) vi.findViewById(R.id.suggestionNo);
            switch (status){
                case YES:
                    setRSVPYesButton(vi);
                    break;
                case NO:
                    setRSVPNoButton(vi);
                    break;
                default:
                    break;
            }
            final View finalVi = vi;
            yesSuggestion.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(!yesSuggestion.getText().equals("RSVPd Yes")) {
                        try {
                            api.RSVPToSuggestion(meetupId, suggestionId, SuggestionStatus.YES);
                            setRSVPYesButton(finalVi);
                            CharSequence text = "RSVP Yes noted!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        } catch (Exception e) {
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("Could not RSVP!");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                            alertDialog.show();
                        }
                    }
                }
            });

            noSuggestion.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                        if(!noSuggestion.getText().equals("RSVPd No")) {
                            try {
                                api.RSVPToSuggestion(meetupId, suggestionId, SuggestionStatus.NO);
                                setRSVPNoButton(finalVi);

                            CharSequence text = "RSVP No noted!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            } catch (Exception e) {
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Could not RSVP!");
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                            alertDialog.show();
                            }
                        }
                }


            });

            // Setting all values in listview
            address.setText(suggestion.get("address"));
            location.setText(suggestion.get("location"));
            date.setText(suggestion.get("date"));
            return vi;
        }

        private void setRSVPYesButton(View vi) {
            final Button yesSuggestion = (Button) vi.findViewById(R.id.suggestionYes);
            final Button noSuggestion = (Button) vi.findViewById(R.id.suggestionNo);
            yesSuggestion.setText("RSVPd Yes");
            yesSuggestion.setTypeface(null, Typeface.BOLD);
            yesSuggestion.setTextSize(20);
            noSuggestion.setText("No");
            noSuggestion.setTypeface(null, Typeface.NORMAL);
            noSuggestion.setTextSize(15);
        }

        private void setRSVPNoButton(View vi) {
            final Button yesSuggestion = (Button) vi.findViewById(R.id.suggestionYes);
            final Button noSuggestion = (Button) vi.findViewById(R.id.suggestionNo);
            noSuggestion.setText("RSVPd No");
            noSuggestion.setTypeface(null, Typeface.BOLD);
            noSuggestion.setTextSize(20);
            yesSuggestion.setText("Yes");
            yesSuggestion.setTypeface(null, Typeface.NORMAL);
            yesSuggestion.setTextSize(15);
        }
    }


