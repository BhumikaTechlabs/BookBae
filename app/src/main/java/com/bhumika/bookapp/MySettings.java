package com.bhumika.bookapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;

public class MySettings extends AppCompatActivity implements View.OnClickListener {

    Switch notifSwitch;
    private Toolbar toolbar;
    Boolean isChecked;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if(pref.contains("recieveNotifications"))
        {
            isChecked= pref.getBoolean("recieveNotifications", true);
        }
        else
        {
            editor.putBoolean("recieveNotifications", true);
            isChecked= true;
            editor.commit();
        }

        notifSwitch= findViewById(R.id.notifSwitch);
        notifSwitch.setChecked(isChecked);
        notifSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.notifSwitch:
                isChecked= !isChecked;
                //isChecked= notifSwitch.isChecked();
                editor.putBoolean("recieveNotifications", Boolean.valueOf(isChecked));
                editor.commit();
                break;

        }
    }
}
