package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookListLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookListLoader.class.getSimpleName();
    private String mQueryURL;

    public BookListLoader(Context context, String queryURL) {
        super(context);
        mQueryURL = queryURL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        // If query string is null, return early
        if (mQueryURL == null) {
            return null;
        }
        // Perform the HTTP request for books data and process the response.
        return QueryUtils.fetchBooksList(mQueryURL);
    }
}
