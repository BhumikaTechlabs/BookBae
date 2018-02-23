package com.bhumika.bookapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class About extends AppCompatActivity implements View.OnClickListener {

    Button share, contact;
    private Toolbar toolbar;
    ImageButton backIcon, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        share= (Button) findViewById(R.id.share);
        share.setOnClickListener(this);
        contact= findViewById(R.id.contact);
        contact.setOnClickListener(this);
        rate= findViewById(R.id.rate);
        rate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "We understand your insatiable thirst for reading. " +
                        "And how you have always wanted to read all those books out there!"
                        +"\n\nWe have got your back! With BOOK BAE you can locate books nearby and borrow them for the best price!" +
                        "\n\nYour books deserve more readers than a place on the shelf! " +
                        "They can give someone a good week's reading! " +
                        "Lend for the best prices to the community and get served!" +
                        "\n\nDownload BOOK BAE :\n\n https://play.google.com/store/apps/details?id=com.bhumika.bookapp"
                        +"\n\nBecause happiness should be shared!";
                String subject = "BOOK BAE";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.contact:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "tech.bs.98@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "BOOK BAE");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
                break;
            case R.id.rate:
                rateApp();
                break;

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /*
* Start with rating the app
* Determine if the Play Store is installed on the device
*
* */
    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

}
