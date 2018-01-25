package com.bhumika.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class About extends AppCompatActivity implements View.OnClickListener {

    Button share, contact;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        share= (Button) findViewById(R.id.share);
        share.setOnClickListener(this);
        contact= findViewById(R.id.contact);
        contact.setOnClickListener(this);

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
                        "\n\nWhy just let your books sit on the shelf while they could get you some" +
                        " green, hard cash and somebody a good week's reading? " +
                        "Lend for the best prices to the community and get served!" +
                        "\n\nDownload BOOK BAE :\n\n <LINK>";
                String subject = "BOOK BAE";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.contact:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "bstech@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "BOOK BAE");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
                break;
        }
    }
}
