package com.example.android.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
            // Else, inflate the item view from list_item.xml
            rootView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        TextView author = (TextView) rootView.findViewById(R.id.authors);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        TextView publishingInfo = (TextView) rootView.findViewById(R.id.publishing_info);
        TextView num_ratings = (TextView) rootView.findViewById(R.id.num_ratings);
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.stars);
        ImageView image = (ImageView) rootView.findViewById(R.id.book_cover_image);
        TextView no_image = (TextView) rootView.findViewById(R.id.no_img_available);

        Book currentItem = getItem(position);

        // Display author(s)
        author.setText(currentItem.getAuthor());

        // Display title
        title.setText(currentItem.getTitle());

        // Display publishing info
        if (currentItem.getPublishingDate().isEmpty()) {
            publishingInfo.setText(currentItem.getPublisher());
        } else {
            publishingInfo.setText(currentItem.getPublisher() + ", "
                    + currentItem.getPublishingDate());
        }

        // Display rating, if available
        if (currentItem.getNumRatings() > 0) {
            num_ratings.setText("(" + currentItem.getNumRatings() + ")");
            ratingBar.setRating((float) currentItem.getRating());
        } else {
            num_ratings.setText("");
            // If rating is not available, set the rating bar as empty
            ratingBar.setRating(0);
        }

        // Display thumbnail, if available
        if (currentItem.hasThumbnail()) {
            image.setImageBitmap(currentItem.getThumbnail());
            no_image.setVisibility(View.GONE);
        } else {
            // If there is no thumbnail, set a default image
            // with a string that says "NO IMAGE AVAILABLE"
            image.setImageResource(R.drawable.book);
            no_image.setVisibility(View.VISIBLE);
        }

        return rootView;
    }
}
