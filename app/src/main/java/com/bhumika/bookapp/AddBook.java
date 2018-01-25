/*package com.bhumika.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBook extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFDb;
    private DatabaseReference mRef;

    Button getLoc;
    ImageView submit;
    EditText bnText, anText, rText, cpText, cnText, locText, oInfText;
    Boolean isEdit;
    private Toolbar toolbar;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    String TAG= "Add Book activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        bnText = (EditText) findViewById(R.id.bnText);
        anText = (EditText) findViewById(R.id.anText);
        rText = (EditText) findViewById(R.id.rText);
        cpText = (EditText) findViewById(R.id.cpText);
        cnText = (EditText) findViewById(R.id.cnText);
        locText = (EditText) findViewById(R.id.locText);
        locText.setEnabled(false);
        oInfText = (EditText) findViewById(R.id.oInfText);
        getLoc = findViewById(R.id.getLoc);
        getLoc.setOnClickListener(this);

        isEdit = getIntent().getBooleanExtra("edit", false);
        if(isEdit) {
            toolbar.setTitle("Edit Book");
            bnText.setText(MainActivity.clickedBook.getBookName());
            anText.setText(MainActivity.clickedBook.getAuthor());
            rText.setText(MainActivity.clickedBook.getRent());
            cpText.setText(MainActivity.clickedBook.getContactPerson());
            cnText.setText(MainActivity.clickedBook.getContact());
            locText.setText(MainActivity.clickedBook.getLocation());
            oInfText.setText(MainActivity.clickedBook.getOtherInfo());
        }

        mFDb= FirebaseDatabase.getInstance();
        mRef= mFDb.getReferenceFromUrl("https://booksanta-2b2cc.firebaseio.com/").child("Books");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String personId = currentUser.getUid();
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag=0;
                final String bnData= bnText.getText().toString();
                final String anData= anText.getText().toString();
                final String rData= rText.getText().toString();
                final String cpData= cpText.getText().toString();
                final String cnData= cnText.getText().toString();
                final String locData= locText.getText().toString();
                if(TextUtils.isEmpty(bnData))
                {
                    bnText.setError("Please enter Book Name");
                    flag=1;
                }
                if(TextUtils.isEmpty(anData))
                {
                    anText.setError("Please enter Author Name");
                    flag=1;
                }
                if(TextUtils.isEmpty(cpData))
                {
                    cpText.setError("Please enter Contact Person");
                    flag=1;
                }
                if(TextUtils.isEmpty(cnData))
                {
                    cnText.setError("Please enter Contact Number");
                    flag=1;
                }
                if(TextUtils.isEmpty(locData))
                {
                    locText.setError("Please set Location");
                    flag=1;
                }
                if(TextUtils.isEmpty(rData))
                {
                    rText.setError("Please enter Rent");
                    flag=1;
                }
                if(flag==0)
                {
                    String key;
                    if(!isEdit) {
                        // call to func with firebase code to add book
                        key = mRef.push().getKey();

                    }
                    else {
                        key = MainActivity.clickedBook.getPushKey();
                    }
                    Book book = new Book(bnData, anData, cnData, cpData,
                            rData, locData, oInfText.getText().toString(),
                            personId, key, String.valueOf(Details.isOn));
                    mRef.child(key).setValue(book);
                    if(isEdit) {
                        MainActivity.clickedBook = book;
                        startActivity(new Intent(AddBook.this, Details.class));
                    }
                    finish();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        if(isEdit)
        {
            Intent i= new Intent(AddBook.this, Details.class);
            //i.putExtra("fromEdit", true);
            startActivity(i);
            finish();
        }
        else
            super.onBackPressed();
    }

    //
    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                locText.setText(""+place.getName()+",\n"+place.getAddress());
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onClick(View view) {
        findPlace(locText);
    }
}
*/