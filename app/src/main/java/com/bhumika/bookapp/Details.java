package com.bhumika.bookapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Details extends AppCompatActivity implements View.OnClickListener {

    private TextView bookName, author, rent, contactPerson, contact, location, otherInfo, info, descrp;
    private Toolbar toolbar;
    private Button deleteBtn, editBtn;
    private ToggleButton toggleBtn;
    public static boolean isOn= true;
    private boolean isUser, fromEdit;
    private FirebaseDatabase mFDb;
    private DatabaseReference mRef;
    ImageView img; char ch='"';


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mFDb= FirebaseDatabase.getInstance();
        mRef= mFDb.getReferenceFromUrl("https://booksanta-2b2cc.firebaseio.com/").child("Books");
        
        isUser = getIntent().getBooleanExtra("fromUser", false);

        img= findViewById(R.id.coverpage);
        info= findViewById(R.id.info);
        bookName= (TextView) findViewById(R.id.bookName);
        author = (TextView) findViewById(R.id.author);
        rent= (TextView) findViewById(R.id.rent);
        contact = (TextView) findViewById(R.id.contact);
        contact.setOnClickListener(this);
        contactPerson = (TextView) findViewById(R.id.contactPerson);
        location= (TextView) findViewById(R.id.location);
        otherInfo= (TextView) findViewById(R.id.otherInfo);
        descrp= findViewById(R.id.description);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        editBtn = (Button) findViewById(R.id.editBtn);
        toggleBtn= findViewById(R.id.togglebutton);
        isOn= Boolean.parseBoolean(MainActivity.clickedBook.getAvlbl());
        toggleBtn.setChecked(isOn);
        if(isOn)
        {
            toggleBtn.setBackgroundColor(getResources().getColor(R.color.mygreen));
        }
        else
        {
            toggleBtn.setBackgroundColor(getResources().getColor(R.color.myblue));
        }
        toggleBtn.setOnClickListener(this);
        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(!MainActivity.clickedBook.getImageUrl().isEmpty())
        {
            Glide.with(this.getApplicationContext())
                    .load(MainActivity.clickedBook.getImageUrl())
                    .into(img);
        }

        bookName.setText(""+MainActivity.clickedBook.getBookName());
        author.setText("by "+MainActivity.clickedBook.getAuthor());
        contact.setText(""+MainActivity.clickedBook.getContact());
        contactPerson.setText("Contact:\n"+MainActivity.clickedBook.getContactPerson());
        rent.setText("Borrow for "+MainActivity.clickedBook.getRent()+"/week");
        location.setText("Location:\n"+MainActivity.clickedBook.getLocation());
        if(!(MainActivity.clickedBook.getDescription().equals("NONE")))
            descrp.setText("Description:\n"+MainActivity.clickedBook.getDescription());
        if(!TextUtils.isEmpty(MainActivity.clickedBook.getOtherInfo()))
        {
            otherInfo.setText("Other Information:\n"+MainActivity.clickedBook.getOtherInfo());
        }
        else
        {
            otherInfo.setText("");
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String personId = currentUser.getUid();
        if(MainActivity.clickedBook.getUser().equals(currentUser.getUid()) || MainActivity.clickedBook.getUser().equals(personId)){
            deleteBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
            toggleBtn.setEnabled(true);
            info.setVisibility(View.VISIBLE);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display a confirmation dialog box
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        Details.this);
                alert.setTitle("DELETE BOOK");
                alert.setMessage("The book will be deleted");
                alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //now delete
                        deleteBook();
                        dialog.dismiss();
                        startActivity(new Intent(Details.this, User.class));
                        finish();
                    }
                });
                alert.setNegativeButton("CANCEl", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.clickedBook.getWasFound().equalsIgnoreCase("true"))
                {
                    Intent i = new Intent(Details.this, AddBookScan.class);
                    i.putExtra("edit", true);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(Details.this, AddBook.class);
                    i.putExtra("edit", true);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    void deleteBook() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        String pushKey = MainActivity.clickedBook.getPushKey();
        mDatabase.child("Books").child(pushKey).removeValue();
        //MainActivity.clickedBook= null;
    }

/*    @Override
    public void onBackPressed() {
        if(isUser)
        {
            startActivity(new Intent(this, User.class));
            finish();
        }
        else
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
*/

    @Override
    public void onClick(View view) {
        int id= view.getId();
        boolean on;
        switch(id)
        {
            case R.id.togglebutton:
                on = ((ToggleButton)view).isChecked();
                isOn= !isOn;
                MainActivity.clickedBook.setAvlbl(String.valueOf(isOn));
                String key = MainActivity.clickedBook.getPushKey();
                mRef.child(key).setValue(MainActivity.clickedBook);

                if (on) {
                    view.setBackgroundColor(getResources().getColor(R.color.mygreen));
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.myblue));
                }
                break;
            case R.id.contact:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { ""+MainActivity.clickedBook.getContact() });
                intent.putExtra(Intent.EXTRA_SUBJECT, "BOOK BAE: Borrow "+
                        ch+MainActivity.clickedBook.getBookName()+ch);
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
