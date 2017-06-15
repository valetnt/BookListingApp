package com.example.android.booklistingapp;

import android.graphics.Bitmap;

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mPublishingDate;
    private String mLink;
    private double mRating;
    private int mNum_Ratings = -1;
    private Bitmap mThumbnail;
    private boolean mHasThumbnail = false;

    /*
     * Declare a public constructor that takes title, mainAuthor, publisher, dateOfPublishing
     * and edition_number as arguments
     */
    public Book(String title, String author, String publisher, String publishingDate, String link) {
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
    }

    public int getNum_Ratings() {
        return mNum_Ratings;
    } // Returns -1 if rating is not assigned

    public boolean hasThumbnail() {
        return mHasThumbnail;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    } // Returns null if no image assigned

    public void setThumbnail(Bitmap bitmap) {
        mThumbnail = bitmap;
        mHasThumbnail = true;
    }
}
