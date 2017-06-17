package com.example.android.booklistingapp;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/*
 * Class that stores essential book features that can be retrieved from Google Books API
 */
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
     * Declare a public constructor that takes the following arguments:
     * title, author, publisher, date of publishing and link to related page on books.google.com
     */
    public Book(String title, String author, String publisher, String publishingDate, @NonNull String link) {
        mTitle = title;
        mAuthor = author;
        mPublisher = publisher;
        mPublishingDate = publishingDate;
        mLink = link;
    }

    /*
     * Set rating info for this book, i.e. average rating expressed by readers and
     * number of readers who have contributed to rating this book.
     * If rating is not available for this book, mNum_Ratings = -1 and mRating remains unassigned
     */
    public void setRating(double rating, int num_ratings) {
        mRating = rating;
        mNum_Ratings = num_ratings;
    }

    /*
     * Check whether a thumbnail has been assigned to this book
     */
    public boolean hasThumbnail() {
        // Returns false if thumbnail has not been assigned
        return mHasThumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    /*
     * "Getter" methods for displaying book features
     */

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

    public int getNumRatings() {
        // Returns -1 if rating has not been assigned
        return mNum_Ratings;
    }

    public double getRating() {
        /*
         * Method getRating() should be invoked ONLY IF method getNumRatings() has returned
         * a POSITIVE integer.
         * Returns null if rating has not been assigned.
         */
        return mRating;
    }

    public Bitmap getThumbnail() {
        /*
         * Method getThumbnail() should be invoked ONLY IF method hasThumbnail() has returned TRUE.
         * Returns null if thumbnail has not been assigned.
         */
        return mThumbnail;
    }

    /*
     * Set book cover thumbnail. If no thumbnail is available, mHasThumbnail remains false and
     * mThumbnail remains unassigned
     */
    public void setThumbnail(Bitmap bitmap) {
        mThumbnail = bitmap;
        mHasThumbnail = true;
    }
}
