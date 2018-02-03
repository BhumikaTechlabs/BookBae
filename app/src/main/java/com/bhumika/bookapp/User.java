package com.bhumika.bookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class User extends AppCompatActivity
        implements MyBooksAdapter.BooksClickListener, View.OnClickListener, SearchView.OnQueryTextListener {

    ImageView addBookButton;
    private static final String TAG = "SignInActivity";
    private SharedPreferences prefs;
    Query mQuery;
    List<Book> books;
    ChildEventListener mChildEventListener;
    RecyclerView bookList;
    MyBooksAdapter adapter;
    private Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button noIsbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        noIsbn= findViewById(R.id.noIsbn);
        noIsbn.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReferenceFromUrl("https://booksanta-2b2cc.firebaseio.com/").child("Books");
        myRef.keepSynced(true);

        addBookButton= findViewById(R.id.addButton);
        addBookButton.setOnClickListener(this);

        FirebaseUser acct= FirebaseAuth.getInstance().getCurrentUser();
        if (acct != null) {
            /*
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            */
            String personId = acct.getUid();
            mQuery = myRef.orderByChild("user").equalTo(personId);

            bookList = (RecyclerView) findViewById(R.id.myBooksList);
            adapter= new MyBooksAdapter(User.this, getData());
            bookList.setAdapter(adapter);
            adapter.setBooksClickListener(this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            bookList.setLayoutManager(mLayoutManager);
        }


    }

    public List<Book> getData()
    {
        books = new ArrayList<>();

        //fetching data from database
        mChildEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book b1= dataSnapshot.getValue(Book.class);
                b1.setPushKey(dataSnapshot.getKey());
                books.add(b1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Book b1= dataSnapshot.getValue(Book.class);
                for (int i = 0; i < books.size(); i++) {
                    if(b1!=null && books.get(i).getPushKey().equals(b1.getPushKey())){
                        books.set(i, b1);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key= dataSnapshot.getKey();
                for(Book b:books)
                {
                    if(b.getPushKey().equals(key))
                    {
                        books.remove(b);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Book b1= dataSnapshot.getValue(Book.class);
                books.add(b1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(User.this, "Failed to load Books! Check your Internet Connection",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mQuery.addChildEventListener(mChildEventListener);
        return books;
    }

    private void addBook()
    {
        Intent intent= new Intent(this, AddBookScan.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.addButton:
                addBook();
                break;
            case R.id.noIsbn:
                startActivity(new Intent(User.this, AddBook.class));
                break;
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        MainActivity.clickedBook= MyBooksAdapter.mFilteredList.get(position);
        Intent i = new Intent(User.this, Details.class);
        i.putExtra("fromUser", true);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.about:
                startActivity(new Intent(this, About.class));
                break;

            case R.id.giveFeedback:
                startActivity(new Intent(this, Feedback.class));
                break;

           /* case R.id.settings:
                startActivity(new Intent(this, MySettings.class));
                break; */
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return true;
    }

    @Override
    public void onBackPressed() {
        NavigationDrawerFragment.isDrawerOpen= false;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
