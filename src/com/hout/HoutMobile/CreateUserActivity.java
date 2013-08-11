package com.hout.HoutMobile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.hout.api.ClientApi;
import com.hout.api.impl.ClientApiImpl;
import com.hout.api.mock.ClientApiMock;

public class CreateUserActivity extends Activity {

    ClientApi api;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        api = extras == null || extras.getSerializable("api") == null ?
                /*new ClientApiImpl(this)*/new ClientApiMock() : (ClientApi)extras.getSerializable("api");

        boolean result = api.isCurrentUserRegistered();
        if(result) {
            Intent houtMainActivity = new Intent(getApplicationContext(), Hout.class);
            houtMainActivity.putExtra("api", api);
            startActivity(houtMainActivity);
            finish();
        }

        setContentView(R.layout.new_user);

        final Button createUserButton = (Button) findViewById(R.id.createUserButton);

        final Context appContext = getApplicationContext();
        createUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    TelephonyManager tMgr =(TelephonyManager)getApplicationContext()
                           .getSystemService(Context.TELEPHONY_SERVICE);
                    String mPhoneNumber = tMgr.getLine1Number();
                    api.createNewUserOnServer(((EditText) findViewById(R.id.newUserText)).getText().toString(),
                            mPhoneNumber);
                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(appContext).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Could not create user! Please connect to the Internet");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    alertDialog.show();
                    return;
                }
                Intent houtMainActivity = new Intent(getApplicationContext(), Hout.class);
                houtMainActivity.putExtra("api", api);
                startActivity(houtMainActivity);
                finish();
            }
        });
    }
}
