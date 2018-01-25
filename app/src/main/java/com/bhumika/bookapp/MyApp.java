package com.bhumika.bookapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Akshay on 31-12-2017.
 */

public class MyApp extends Application {

    public static int flag=0;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //saves data locally
    }
}
