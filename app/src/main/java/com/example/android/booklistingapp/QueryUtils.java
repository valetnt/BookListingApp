package com.example.android.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
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
import java.util.List;


/**
 * Helper methods related to requesting and receiving book data from GoogleBooks API.
 *
 * WARNING!!!
 * This method will only return volumes that have:
 * 1) A publisher
 * 2) At least one Industry Identifier, such as an ISBN code
 * 3) Explicit language specification set to English
 *
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
    public static List<Book> fetchBooksData(String requestURL) {

        // Turn the String argument into a URL
        URL queryURL = createUrl(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String JSONResponse = null;
        try {
            JSONResponse = makeHttpRequest(queryURL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Parse the JSON response and extract a list of books
        List<Book> books = extractFeaturesFromJSON(JSONResponse);
        // Return the list
        return books;
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
    private static String makeHttpRequest(URL url) throws IOException {
        String JSONResponse = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        // If the URL is null, then return early.
        if (url == null) {
            return JSONResponse;
        }
        // Set HTTP connection to the URL
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the
            // input stream and store its whole content into a String.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else {
                // If the request was not successful, print the error code in the logcat
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
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
                line = bufferedReader.readLine();
            }
        }
        return outputString.toString();
    }

    /*
     * Return a list of {@link Book} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Book> extractFeaturesFromJSON(String JSONResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JSONResponse)) {
            return null;
        }

        // Create an empty ArrayList of Book objects to be filled later
        ArrayList<Book> booklist = new ArrayList<Book>();

        // Try to parse the JSONResponse. If there's a problem with the way the JSON string
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject containing the whole JSONResponse (the root)
            // Parse the JSONObject starting from the root
            JSONObject JSONString = new JSONObject(JSONResponse);
            JSONArray items = JSONString.getJSONArray("items");
            // Fill the ArrayList with items
            for (int i = 0; i < items.length(); i++) {
                JSONObject volume = items.getJSONObject(i);
                JSONObject volumeInfo = volume.getJSONObject("volumeInfo");
                // Get title
                String title = volumeInfo.getString("title");
                if (volumeInfo.has("subtitle")) {
                    String subtitle = volumeInfo.getString("subtitle");
                    // Concatenate title and subtitle
                    if (title.endsWith(".")) {
                        title = title + " " + subtitle;
                    } else if (title.endsWith(". ")) {
                        title = title + subtitle;
                    } else {
                        title = title + ". " + subtitle;
                    }
                }
                // Get author(s)
                String author = "Unknown Author";
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    if (authors.length() > 0) {
                        author = authors.get(0).toString();
                        String firstAuthor = author;
                        if (authors.length() > 1) {
                            String secondAuthor = authors.get(1).toString();
                            author = firstAuthor + ", " + secondAuthor;
                            if (authors.length() > 2) {
                                author = firstAuthor + " et al.";
                            }
                        }
                    }
                }
                // Get publisher - DISCARD UNPUBLISHED BOOKS
                if (volumeInfo.has("publisher")) {
                    String publisher = volumeInfo.getString("publisher");
                    // Get publishing date
                    String publishingDate = "";
                    if (volumeInfo.has("publishedDate")) {
                        publishingDate = volumeInfo.getString("publishedDate");
                        publishingDate = publishingDate.substring(0, 4); // Only the year
                    }
                    // Get link to book related page on books.google.com
                    // DISCARD BOOKS WITHOUT INDUSTRY IDENTIFIERS
                    String link = "https://books.google.com/books?vid=ISBN";
                    if (volumeInfo.has("industryIdentifiers")) {
                        JSONArray isbn = volumeInfo.getJSONArray("industryIdentifiers");
                        if (isbn.length() > 0) {
                            int j = 0;
                            while (j < isbn.length()) {
                                if (isbn.getJSONObject(j).getString("type").equals("ISBN_10")) {
                                    link = link + isbn.getJSONObject(j).getString("identifier");
                                    break;
                                }
                                j++;
                            }
                            // EXPLICITLY CHECK language restrictions (though
                            // already specified in the query parameters):
                            if (volumeInfo.has("language")) {
                                if (volumeInfo.getString("language").equals("en")) {

                                    // If all selection criteria are met, then create
                                    // a new instance of Book class
                                    Book book = new Book(title, author, publisher, publishingDate,
                                            link);
                                    // Add rating, if present
                                    if (volumeInfo.has("averageRating") &&
                                            volumeInfo.has("ratingsCount")) {
                                        double rating = volumeInfo.getDouble("averageRating");
                                        int num_ratings = volumeInfo.getInt("ratingsCount");
                                        if (num_ratings > 0) {
                                            book.setRating(rating, num_ratings);
                                        }
                                    }
                                    // Add thumbnail, if present. We need to make
                                    // another HTTP request to download the image.
                                    if (volumeInfo.has("imageLinks")) {
                                        JSONObject imageLinks =
                                                volumeInfo.getJSONObject("imageLinks");
                                        if (imageLinks.has("smallThumbnail")) {
                                            String thumbUrl = imageLinks.getString("smallThumbnail");
                                            Bitmap thumbnail = null;
                                            // Turn the String into a valid URL
                                            URL url = createUrl(thumbUrl);
                                            // Download image from the specified URL
                                            try {
                                                thumbnail = downloadBitmap(url);
                                            } catch (IOException e) {
                                                Log.e(LOG_TAG, "Could not download the image", e);
                                            }
                                            // If download was successful, assign image as
                                            // book cover thumbnail
                                            if (thumbnail != null) {
                                                book.setThumbnail(thumbnail);
                                            }
                                        }
                                    }
                                    // Add this volume to the list
                                    booklist.add(book);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        // Return the list of books
        return booklist;
    }

    private static Bitmap downloadBitmap(URL url) throws IOException {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        // If the URL is null, then return early.
        if (url == null) {
            return null;
        }
        // Set HTTP connection to the URL
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            // If the request was successful (response code 200), then decode the
            // input stream into a bitmap using the static method BitmapFactory.decodeStream
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
            } else {
                // If the request was not successful, print the error code in the logcat
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the image", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return bitmap;
    }
}
