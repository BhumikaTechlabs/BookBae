package com.bhumika.bookapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 20/12/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ClickListener mClickListener;
    List<DrawerRecyclerViewContent> data= Collections.emptyList();

    public MyAdapter(Context context, List<DrawerRecyclerViewContent> data)
    {
        inflater= LayoutInflater.from(context);
        this.data= data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DrawerRecyclerViewContent current= data.get(position);
        holder.title.setText(current.title+"");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(ClickListener clickListener)
    {
        this.mClickListener= clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title= (TextView) itemView.findViewById(R.id.tvContent);
        }

        @Override
        public void onClick(View view) {

            if(mClickListener!=null)
            {
                mClickListener.itemClicked(view, getPosition());
            }
        }
    }

    public interface ClickListener
    {
        void itemClicked(View view, int position);
    }

}
