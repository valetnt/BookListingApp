package com.example.android.booklistingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mPublishingDate;
    private String mLink;
    private double mRating = -1;
    private int mNum_Ratings = -1;

    /*
     * Declare a public constructor that takes title, mainAuthor, publisher, dateOfPublishing
     * and edition_number as arguments
     */
    public Book(@NonNull String title, @NonNull String author, @NonNull String publisher,
                @Nullable String publishingDate, @Nullable String link) {
        mTitle = title;
        mAuthor = author;
        mPublisher = publisher;
        mPublishingDate = publishingDate;
        mLink = link;
    }

    public void setRating(double rating, int num_ratings) {
        mRating = rating;
        mNum_Ratings = num_ratings;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getPublishingDate() {
        return mPublishingDate;
    }

    public String getLink() {
        return mLink;
    }

    public double getRating() {
        return mRating;
    } // Returns -1 if rating is not assigned

    public int getNum_Ratings() {
        return mNum_Ratings;
    } // Returns -1 if rating is not assigned
}
