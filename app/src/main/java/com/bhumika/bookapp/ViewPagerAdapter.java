package com.bhumika.bookapp;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Admin on 10/01/2018.
 */

public class ViewPagerAdapter extends PagerAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] slides = { R.drawable.map, R.drawable.icon_book};
    private String [] texts = {
            "Grab your latest interests!\nLocate books nearby",
            "BOOK BAE\n\nHappy Reading!"
    };

    public ViewPagerAdapter(Context context)
    {
        this.context= context;
    }

    @Override
    public int getCount() {
        return slides.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.login_view_pager, null);

        LinearLayout ll = view.findViewById(R.id.ll);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        ImageView iv = view.findViewById(R.id.imageView);
        iv.setImageResource(slides[position]);
        TextView tv = view.findViewById(R.id.text);
        tv.setText(texts[position]);
        switch(position)
        {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ViewPager vp = (ViewPager) container;
        View v = (View) object;
        vp.removeView(v);
    }
}
