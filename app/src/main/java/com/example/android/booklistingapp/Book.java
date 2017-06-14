package com.example.android.booklistingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Book {

    private String mTitle;
    private String mMainAuthor;
    private String mPublisher;
    private String mDateOfPublishing;
    private int mEdition;
    private String mAuthor1 = null;
    private String mAuthor2 = null;
    private int mRating = -1;
    private int mNum_Ratings = -1;

    /*
     * Declare a public constructor that takes title, mainAuthor, publisher, dateOfPublishing
     * and edition_number as arguments
     */
    public Book(@NonNull String title, @NonNull String mainAuthor, @NonNull String publisher,
                @Nullable String dateOfPublishing, int edition_number) {
        mTitle = title;
        mMainAuthor = mainAuthor;
        mPublisher = publisher;
        mDateOfPublishing = dateOfPublishing;
        mEdition = edition_number;
    }

    public void setRating(int rating, int num_ratings) {
        mRating = rating;
        mNum_Ratings = num_ratings;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMainAuthor() {
        return mMainAuthor;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getDateOfPublishing() {
        return mDateOfPublishing;
    }

    public int getEdition() {
        return mEdition;
    }

    public int getRating() {
        return mRating;
    } // Returns -1 if rating is not assigned

    public int getNum_Ratings() {
        return mNum_Ratings;
    } // Returns -1 if rating is not assigned
}
