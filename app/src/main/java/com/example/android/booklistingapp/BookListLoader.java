package com.example.android.booklistingapp;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BookListLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookListLoader.class.getSimpleName();

    private String mQuery;

    public BookListLoader(Context context, String query) {
        super(context);
        mQuery = query;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        // If query string is null, return early
        if(mQuery == null) {
            return null;
        }

        // Create an ArrayList of books and fill it with the results of the query
        ArrayList<Book> books = QueryUtils.fetchBooksList(mQuery);
        return books;
    }

}
