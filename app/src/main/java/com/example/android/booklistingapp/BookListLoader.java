package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookListLoader extends AsyncTaskLoader<List<Book>> {

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
        return QueryUtils.fetchBooksList(mQuery);
    }
}
