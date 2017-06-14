package com.example.android.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Book> {

    private Context mContext;

    public CustomAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        // Invoke superclass constructor passing 0 as resourceID,
        // because we are going to inflate a custom view
        super(context, 0, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // If the view already exists, recycle it
        View rootView = convertView;
        if (rootView == null) {
            // Inflate the item view from list_item.xml
            rootView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        TextView author = (TextView) rootView.findViewById(R.id.authors);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        TextView publishingInfo = (TextView) rootView.findViewById(R.id.publishing_info);
        TextView num_ratings = (TextView) rootView.findViewById(R.id.num_ratings);
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.stars);

        Book currentItem = getItem(position);

        author.setText(currentItem.getAuthor());
        title.setText(currentItem.getTitle());
        publishingInfo.setText(currentItem.getPublisher() + ", " + currentItem.getPublishingDate());
        if (currentItem.getNum_Ratings() > 0) {
            num_ratings.setText("(" + currentItem.getNum_Ratings() + ")");
            ratingBar.setRating((float) currentItem.getRating());
        } else {
            num_ratings.setText("");
        }

        return rootView;
    }
}
