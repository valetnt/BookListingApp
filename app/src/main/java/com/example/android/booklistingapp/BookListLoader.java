package com.example.android.booklistingapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/*
 * BookListLoader has the purpose of managing an Asynchronous Task to perform an HTTP query
 * to a specified URL and then gracefully "handing on" the results of the query
 * to the SearchResultsActivity.
 * The asynchronous task is executed in the background thread and returns a list of Book objects,
 * which is the result of the HTTP query.
 * BookListLoader "hands on" these results to the SearchResultsActivity in the main thread
 * (via callback method onLoadFinished()).
 */
public class BookListLoader extends AsyncTaskLoader<List<Book>> {

    private String mQueryURL;

    /*
     * Constructor
     */
    public BookListLoader(Context context, String queryURL) {
        super(context);
        mQueryURL = queryURL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /*
     * Method loadInBackground() is executed in the background thread.
     * It returns a List of Book objects, which will be then passed on to the SearchResultsActivity
     * in the main thread (via callback method onLoadFinished()).
     */
    @Override
    public List<Book> loadInBackground() {
        // If query string is null, return early
        if (mQueryURL == null) {
            return null;
        }
        // Perform the HTTP request for books data and process the response.
        return QueryUtils.fetchBooksData(mQueryURL);
    }
}
