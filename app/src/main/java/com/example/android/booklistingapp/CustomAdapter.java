package com.example.android.booklistingapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
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
            // Inflate the item view from list_item.xml
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
        if (currentItem.getAuthor().endsWith("et al.")) {
            SpannableString spannable =
                    new SpannableString(currentItem.getAuthor());
            StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
            spannable.setSpan(styleSpan, spannable.length() - 6, spannable.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            author.setText(spannable);
        } else {
            author.setText(currentItem.getAuthor());
        }

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
        if (currentItem.getNum_Ratings() > 0) {
            num_ratings.setText("(" + currentItem.getNum_Ratings() + ")");
            ratingBar.setRating((float) currentItem.getRating());
        } else {
            num_ratings.setText("");
            ratingBar.setRating(0);
        }

        // Display thumbnail, if available
        if (currentItem.hasThumbnail()) {
            image.setImageBitmap(currentItem.getThumbnail());
            no_image.setVisibility(View.GONE);
        } else {
            image.setImageResource(R.drawable.book);
            no_image.setVisibility(View.VISIBLE);
        }

        return rootView;
    }
}
