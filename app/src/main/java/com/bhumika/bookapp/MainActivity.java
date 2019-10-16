package com.bhumika.bookapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyBooksAdapter.BooksClickListener, SearchView.OnQueryTextListener//, View.OnClickListener
{
    private static ArrayList<Book> books;
    private Toolbar toolbar;
    private Button addBookButton;
    private ImageButton locFilter;
    private RecyclerView bookList;
    private MyBooksAdapter adapter;
    private ChildEventListener mChildEventListener;
    public static Book clickedBook;
    private Query mQuery;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE=1;
    private String TAG= "Main activity";
    public static String searchLoc= "";
    private ProgressBar spinner;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    NavigationDrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_appbar);

            spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.setVisibility(View.GONE);

            //askForPermission(Manifest.permission.CAMERA, 1);
            //askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 2);
            //askForPermission(Manifest.permission.INTERNET, 4);
            //askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 3);

            toolbar= (Toolbar)findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);

            //locFilter= (ImageButton) findViewById(R.id.locBtn);
            //locFilter.setOnClickListener(this);

            database = FirebaseDatabase.getInstance();
            myRef = database.getReferenceFromUrl("your_firebase_url_here").child("Books");

            geoFire = new GeoFire(database.getReferenceFromUrl("your_firebase_url_here").child("Locations_Books"));
            geoQuery = geoFire.queryAtLocation(new GeoLocation(18.5411726, 73.72813050000002), 2.0);

            //if (MyApp.flag==0)
            mQuery = myRef.orderByChild("pushKey");
            bookList = (RecyclerView) findViewById(R.id.bookList);
            adapter= new MyBooksAdapter(MainActivity.this, getData());
            bookList.setAdapter(adapter);
            adapter.setBooksClickListener(this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            bookList.setLayoutManager(mLayoutManager);

            //getSupportActionBar().setHomeButtonEnabled(true);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            drawerFragment= (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer);
            drawerFragment.setUp(R.id.fragment_nav_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    private void askForPermission(String permission, Integer requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        }
   /*     else
        {
            if(requestCode==1)
                Toast.makeText(this, "" + "RECORD AUDIO permission" + " is already granted.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "" + "WRITE EXTERNAL STORAGE permission" + " is already granted.", Toast.LENGTH_SHORT).show();
        }
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            return;
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
                databaseError.toException().printStackTrace();
            }
        };
        mQuery.addChildEventListener(mChildEventListener);
        return books;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        //
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

            /*case R.id.settings:
                startActivity(new Intent(this, MySettings.class));
                break;
               */
            //case android.R.id.home:
            //    NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void itemClicked(View view, int position) {
        clickedBook= MyBooksAdapter.mFilteredList.get(position);
        startActivity(new Intent(MainActivity.this, Details.class));
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

    //
   /* public void findPlace(View view) {
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
                searchLoc= ""+place.getName()+",\n"+place.getAddress();
                //searchLoc= ""+place.getName();
                MyApp.flag=1;
                adapter.getFilter().filter("");
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
        //findPlace(locFilter);
        geoQueryFilter();
    }

    private void geoQueryFilter() {

        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 5);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
                books.remove(this);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(searchLoc.isEmpty())
            super.onBackPressed();
        else
        {
            searchLoc="";
            adapter.getFilter().filter("");
        }
    } */

    @Override
    public void onBackPressed() {
        if(NavigationDrawerFragment.isDrawerOpen)
            drawerFragment.mDrawerLayout.closeDrawer(drawerFragment.containerView);
        else
            super.onBackPressed();
    }
}

