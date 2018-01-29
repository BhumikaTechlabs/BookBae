package com.bhumika.bookapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends AppCompatActivity {

    private Toolbar toolbar;
    EditText feedback;
    private FirebaseDatabase mFDb;
    private DatabaseReference mRef;
    private ImageView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mFDb= FirebaseDatabase.getInstance();
        mRef= mFDb.getReferenceFromUrl("https://booksanta-2b2cc.firebaseio.com/").child("Feedbacks");
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Feedback.this);
        final String personId = acct.getId();
        final String dispName = acct.getDisplayName();
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                feedback= findViewById(R.id.feedback);
                final String feedbackTxt= feedback.getText().toString();
                if(TextUtils.isEmpty(feedbackTxt))
                {
                    feedback.setError("OOPS! Your feedback seems to be empty!");
                }
                else
                {
                    FeedbackModel feedBk = new FeedbackModel(feedbackTxt, dispName, personId);
                    String key= mRef.push().getKey();
                    mRef.child(key).setValue(feedBk);
                    Toast.makeText(Feedback.this, "Your feedback was recorded. Thank You!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}