package com.bhumika.bookapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 20/12/2017.
 */

public class MyBooksAdapter extends RecyclerView.Adapter<MyBooksAdapter.MyViewHolder> implements Filterable
{

    private LayoutInflater inflater;
    private BooksClickListener mClickListener;
    List<Book> data;
    public static List<Book> mFilteredList;
    List<Book> searchData;
    public int position;
    Context c;

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public MyBooksAdapter(Context context, List<Book> data)
    {
        inflater= LayoutInflater.from(context);
        c= context;
        this.data= data;
        mFilteredList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.book_custom_row, parent, false);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       // view.findViewById(R.id.bookRow).setBackgroundColor(color);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    public Book get(int position)
    {
        Book b= mFilteredList.get(position);
        return b;
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book current= mFilteredList.get(position);
        holder.bookName.setText(current.bookName+"");
        holder.author.setText("by "+current.author+"");
        holder.bind(current);

    }



    @Override
    public int getItemCount(){
        return mFilteredList.size();
    }

    public void setBooksClickListener(BooksClickListener clickListener)
    {
        this.mClickListener= clickListener;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();

                //no constraints
                if (charString.isEmpty() && MainActivity.searchLoc.isEmpty())
                {
                    mFilteredList = data;
                }

                //only location is set
                else if(charString.isEmpty()) {

                    ArrayList<Book> filteredList = new ArrayList<>();
                    for (Book book : data) {

                        if (//(book.getBookName().toLowerCase().contains(charString) ||
                            //book.getAuthor().toLowerCase().contains(charString))
                                book.getLocation().toLowerCase().contains(MainActivity.searchLoc)) {

                            filteredList.add(book);
                        }
                    }
                    mFilteredList = filteredList;
                }

                //no location set but search tool is being used
                else if (MainActivity.searchLoc.isEmpty())
                {
                    ArrayList<Book> filteredList = new ArrayList<>();
                    for (Book book : data) {

                        if (book.getBookName().toLowerCase().contains(charString) ||
                            book.getAuthor().toLowerCase().contains(charString)){

                            filteredList.add(book);
                        }
                    }
                    mFilteredList = filteredList;
                }

                //location is set as well as search tool is being used
                else
                {
                    ArrayList<Book> filteredList = new ArrayList<>();
                    for (Book book : data) {

                        if ((book.getBookName().toLowerCase().contains(charString) ||
                            book.getAuthor().toLowerCase().contains(charString))
                             && book.getLocation().contains(MainActivity.searchLoc)) {

                            filteredList.add(book);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView bookName, author;
        ImageView bookImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            bookName= (TextView) itemView.findViewById(R.id.bookName);
            author= (TextView) itemView.findViewById(R.id.author);
            bookImg= itemView.findViewById(R.id.bookImg);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener!=null)
            {
                mClickListener.itemClicked(view, getPosition());
            }
        }

        public void bind(Book bookx) {
            bookName.setText(bookx.getBookName());
            author.setText(bookx.getAuthor());
            if(!bookx.getImageUrl().isEmpty())
            {
                Glide.with(c)
                        .load(bookx.getImageUrl())
                        .into(bookImg);
            }
            else
            {
                bookImg.setImageResource(R.drawable.books);
                bookImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(mClickListener!=null)
            {
                setPosition(position);
            }
            return false;
        }

    }

    public interface BooksClickListener
    {
        void itemClicked(View view, int position);
    }
}
