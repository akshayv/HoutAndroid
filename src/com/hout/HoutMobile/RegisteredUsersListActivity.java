package com.hout.HoutMobile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.hout.api.ClientApi;
import com.hout.domain.entities.UserMin;

import java.util.*;

public class RegisteredUsersListActivity extends Activity {

    ClientApi api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_users);

        ListView list=(ListView)findViewById(R.id.registeredUsersListView);

        api = (ClientApi) getIntent().getExtras().getSerializable("api");
        List<Long> invitees = (ArrayList<Long>) getIntent().getExtras().getSerializable("invitees");
        Set<String> contactNumbers = getContactNumbers();

        List<UserMin> registeredUsers = new ArrayList<UserMin>();
        try {
            registeredUsers = api.getRegisteredUsers(contactNumbers);
        } catch (Exception ignored) {
        }

        final ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();

        for(UserMin user : registeredUsers) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("id", String.valueOf(user.getUserId()));
            data.put("name", user.getName());
            data.put("contact", user.getContactNumber());
            if(invitees != null && invitees.contains(user.getUserId())) {
                data.put("selected", "true");
            }
            usersList.add(data);
        }

        final RegisteredUserAdaptor adapter=new RegisteredUserAdaptor(this, usersList,
                getApplicationContext(), api);

        final Button createButton = (Button) findViewById(R.id.addInviteesButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDoneButtonClick(adapter, v);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                if (adapter.getData().get(position).get("selected") == null || !Boolean
                        .parseBoolean(adapter.getData().get(position).get("selected"))) {
                    view.setBackgroundResource(R.color.pressed_color);
                    adapter.getData().get(position).put("selected", String.valueOf(true));
                } else {
                    view.setBackgroundResource(R.color.default_color);
                    adapter.getData().get(position).put("selected", String.valueOf(false));
                }

            }
        });

        list.setItemsCanFocus(false);
        list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(adapter);
    }

    public void onDoneButtonClick(RegisteredUserAdaptor adaptor, View v) {

        List<HashMap<String, String>> data = adaptor.getData();

        List<Long> selectedUserIds = new ArrayList<Long>();
        for (HashMap properties : data) {
            if (properties.get("selected") != null && Boolean.parseBoolean((String) properties.get("selected"))) {
                selectedUserIds.add(Long.valueOf(properties.get("id").toString()));
            }
        }
        Context context = getApplicationContext();
        CharSequence text = "Selected " + selectedUserIds.size() + " users.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent redirectActivity = new Intent(getApplicationContext(), CreateMeetupActivity.class);
        redirectActivity.putExtra("api", api);
        redirectActivity.putExtra("invitees", (ArrayList<Long>)selectedUserIds);
        putMeetupParametersIntoExtra(redirectActivity);
        startActivity(redirectActivity);
    }

    private void putMeetupParametersIntoExtra(Intent redirectActivity) {
        Bundle currentBundle = getIntent().getExtras();
        redirectActivity.putExtra("description", currentBundle.getSerializable("description"));
        redirectActivity.putExtra("suggestedLocation", currentBundle.getSerializable("suggestedLocation"));
        redirectActivity.putExtra("isFacebookSharing", currentBundle.getSerializable("isFacebookSharing"));
        redirectActivity.putExtra("isTwitterSharing", currentBundle.getSerializable("isTwitterSharing"));
        redirectActivity.putExtra("isSuggestions", currentBundle.getSerializable("isSuggestions"));
        redirectActivity.putExtra("timeHour", currentBundle.getSerializable("timeHour"));
        redirectActivity.putExtra("timeMinute", currentBundle.getSerializable("timeMinute"));
        redirectActivity.putExtra("year", currentBundle.getSerializable("year"));
        redirectActivity.putExtra("month", currentBundle.getSerializable("month"));
        redirectActivity.putExtra("day", currentBundle.getSerializable("day"));
    }

    public Set<String> getContactNumbers() {
        Set<String> contacts = new HashSet<String>();
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()) {
            String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(phoneNo);
        }
        return contacts;
    }
}

class RegisteredUserAdaptor extends BaseAdapter {

    @SuppressWarnings("FieldCanBeLocal")
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private Context context;
    ClientApi api;

    public RegisteredUserAdaptor(Activity a, ArrayList<HashMap<String, String>> d, Context context
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
        return data.get(position);
    }

    public long getItemId(int position) {
        return Long.parseLong(data.get(position).get("id"));
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.user_row, null);

        TextView userNameTextView = (TextView)vi.findViewById(R.id.userName);
        TextView userNumberTextView = (TextView)vi.findViewById(R.id.userNumber);
        ImageView userImage = (ImageView)vi.findViewById(R.id.userImage);

        HashMap<String, String> users;
        users = data.get(position);

        final String userName = users.get("name");
        final String contactNumber = users.get("contact");

        // Setting all values in listview
        userNameTextView.setText(userName);
        userNumberTextView.setText(contactNumber);
        userImage.setImageResource(R.drawable.hout);
        if(Boolean.parseBoolean(users.get("selected"))) {
            vi.setBackgroundResource(R.color.pressed_color);
        } else {
            vi.setBackgroundResource(R.color.default_color);
        }
        return vi;
    }
}
