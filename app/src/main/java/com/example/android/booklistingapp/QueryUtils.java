package com.example.android.booklistingapp;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * Helper methods related to requesting and receiving book data from GoogleBooks API.
 */
public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Book} objects that has been built up from parsing a JSON response.
     */
    public static ArrayList<Book> fetchBooksList(String query) {

        // Create an empty ArrayList of Book objects to be filled later
        ArrayList<Book> booklist = new ArrayList<Book>();

        // Turn the String argument into a URL
        URL queryURL = createUrl(query);

        if (queryURL == null) {
            // If URL is malformed, do not even perform the http request, but simply return null
            return null;
        } else {
            // Perform HTTP request to the URL and receive a JSON response back
            String JSONResponse = makeHttpRequest(queryURL);

            // If IOException occurred while performing the HTTP request, return null
            if (JSONResponse == null) {
                return null;
            }

            // Try to parse the JSONResponse. If there's a problem with the way the JSON string
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {
                // Parse the JSON response and fill the ArrayList
                JSONObject JSONString = new JSONObject(JSONResponse);
                JSONArray items = JSONString.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject volume = items.getJSONObject(i);
                    String link = volume.getString("selfLink");
                    JSONObject volumeInfo = volume.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    String subtitle = volumeInfo.getString("subtitle");
                    // Concatenate title and subtitle
                    if (title.endsWith(".")) {
                        title = title + subtitle;
                    } else {
                        title = title + "." + subtitle;
                    }
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    String author = "Unknown Author";
                    if (authors.length() > 0) {
                        author = authors.get(0).toString();
                        String firstAuthor = author;
                        if (authors.length() > 1) {
                            String secondAuthor = authors.get(1).toString();
                            author = firstAuthor + ", " + secondAuthor;
                            if (authors.length() > 2) {
                                SpannableStringBuilder spannable =
                                        new SpannableStringBuilder(firstAuthor + "et al.");
                                spannable.setSpan(new StyleSpan(Typeface.ITALIC),
                                        firstAuthor.length() + 1, spannable.length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                author = spannable.toString();
                            }
                        }
                    }
                    String publisher = volumeInfo.getString("publisher");
                    String publishingDate = volumeInfo.getString("publishedDate");
                    publishingDate = publishingDate.substring(0, 3); // Only the year

                    Book book = new Book(title, author, publisher, publishingDate, link);

                    double rating = volumeInfo.getDouble("averageRating");
                    int num_ratings = volumeInfo.getInt("ratingsCount");
                    if (num_ratings > 0) {
                        book.setRating(rating, num_ratings);
                    }

                    booklist.add(book);
                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
        }
        return booklist;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) {

        String JSONResponse = null;
        InputStream inputStream;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the
            // InputStream and store its whole content into a String.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else {
                // If the request was not successful, print the error code in the logcat
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        return JSONResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder outputString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                outputString.append(line);
                bufferedReader.readLine();
            }
        }
        return outputString.toString();
    }
}
