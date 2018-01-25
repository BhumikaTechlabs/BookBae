package com.bhumika.bookapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment
implements MyAdapter.ClickListener
{

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private static final String PREF_FILE_NAME="testpref";
    private static final String KEY_USER_LEARNED_DRAWER="user ;earned drawer";
    private boolean mUserLearnedDrawer, mFromSavedInstanceState;
    private ImageView pp;
    private TextView dispName, email;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout=inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView= (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter= new MyAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pp =(ImageView) layout.findViewById(R.id.pp);
        dispName= (TextView) layout.findViewById(R.id.dispName);
        email= (TextView) layout.findViewById(R.id.email);
        dispName.setText(LoginActivity.dispName);
        email.setText(LoginActivity.email);
        Glide.with(getActivity().getApplicationContext())
                .load(LoginActivity.pp)
                .thumbnail(0.5f)
                .into(pp);


        return layout;
    }

    public static List<DrawerRecyclerViewContent> getData()
    {
        List<DrawerRecyclerViewContent> data =
                new ArrayList<>();
        String titles[]= {"My Books","Sign Out"};
        for(int i=0; i<(titles.length); i++)
        {
            DrawerRecyclerViewContent current=
                    new DrawerRecyclerViewContent();
            current.title= titles[i];
            data.add(current);
        }
        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView=getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                containerView.setClickable(true);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

         /*   @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                if(slideOffset<0.6)
                {
                    toolbar.setAlpha(1-slideOffset);
                }
            }
         */

        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply(); //or commit()
    }

    public static String readFromPreferences(Context context,String preferenceName,String defaultValue)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }



    @Override
    public void itemClicked(View view, int position) {
        switch(position)
        {
            case 0:
                Intent intent= new Intent(getActivity(), User.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case 1:
                //FirebaseAuth.getInstance().signOut();
                Intent intent2= new Intent(getActivity(), LoginActivity.class);
                intent2.putExtra("sign out", 1);
                startActivity(intent2);
                getActivity().finish();
                break;
        }
    }
}
