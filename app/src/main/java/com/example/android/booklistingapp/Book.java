package com.example.android.booklistingapp;

public class Book {

    private String mTitle;
    private String mMainAuthor;
    private String mAuthor1 = null;
    private String mAuthor2 = null;
    private int mRating = -1;
    private String mPublisher = null;
    private String mDateOfPublishing = null;

    public Book(String title, String mainAuthor) {
        mTitle = title;
        mMainAuthor = mainAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMainAuthor() {
        return mMainAuthor;
    }
}
