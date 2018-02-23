package com.bhumika.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

public class AddBookScan extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFDb;
    private DatabaseReference mRef;

    Button getLoc;
    ImageView submit;
    public static EditText rText, cpText, cnText, locText, oInfText;
    public static Boolean isEdit;
    private Toolbar toolbar;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    String TAG= "Add Book activity";
    private EditText isbnText;
    private Button getIsbn;
    private int BARCODE_REQUEST_CODE= 2;
    public static Book book;
    public static StringBuffer isbnData, cpData, cnData, locData, rData;
    public static StringBuffer personId;
    private Button setCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_scan);

        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        book= new Book();

        setCurr= findViewById(R.id.setCurr);
        setCurr.setOnClickListener(this);
        isbnText= (EditText) findViewById(R.id.isbnText);
        //isbnText.setEnabled(false);
        getIsbn= findViewById(R.id.getIsbn);
        getIsbn.setOnClickListener(this);
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
            isbnText.setText(MainActivity.clickedBook.getIsbn());
            rText.setText(MainActivity.clickedBook.getRent());
            cpText.setText(MainActivity.clickedBook.getContactPerson());
            cnText.setText(MainActivity.clickedBook.getContact());
            locText.setText(MainActivity.clickedBook.getLocation());
            oInfText.setText(MainActivity.clickedBook.getOtherInfo());
        }

        mFDb= FirebaseDatabase.getInstance();
        mRef= mFDb.getReferenceFromUrl("https://booksanta-2b2cc.firebaseio.com/").child("Books");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        personId = new StringBuffer(currentUser.getUid());
        //
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag=0;
                isbnData= new StringBuffer(isbnText.getText().toString());
                rData= new StringBuffer(rText.getText().toString());
                cpData= new StringBuffer(cpText.getText().toString());
                cnData= new StringBuffer(cnText.getText().toString());
                locData= new StringBuffer(locText.getText().toString());
                if(TextUtils.isEmpty(isbnData))
                {
                    isbnText.setError("Please scan book ISBN");
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
                    GoogleApiRequest gar= new GoogleApiRequest(AddBookScan.this);
                    gar.execute(String.valueOf(isbnText.getText()));
                    if(isEdit) {
                        MainActivity.clickedBook = book;
                        startActivity(new Intent(AddBookScan.this, Details.class));
                    }
                    finish();
                }
            }
        });



    }
/*
    @Override
    public void onBackPressed() {
        if(isEdit)
        {
            Intent i= new Intent(AddBookScan.this, Details.class);
            //i.putExtra("fromEdit", true);
            startActivity(i);
            finish();
        }
        else
            super.onBackPressed();
    }
    */

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
                //locText.setText(""+place.getName());
                //Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                //Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        else if(requestCode== BARCODE_REQUEST_CODE && resultCode== BarcodeScannerActivity.SCAN_SUCCESSFUL)
        {
            isbnText.setText((data.getExtras().getString(BarcodeScannerActivity.SCAN_RESULT)).toString());
        }

    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.getLoc:
                findPlace(locText);
                break;
            case R.id.getIsbn:
                startActivityForResult(new Intent(AddBookScan.this, BarcodeScannerActivity.class),
                        BARCODE_REQUEST_CODE);
                break;
            case R.id.setCurr:
            {
                final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        // Implement your code here
                        rText.append(" "+name);
                        picker.dismiss();
                        Toast.makeText(AddBookScan.this, ""+name, Toast.LENGTH_SHORT).show();
                    }
                });
                picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
            }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
