package com.example.ayush.theguardiannews;

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

public class QueryUtils {


    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Query The Guardian News API and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Log.e(LOG_TAG, "fetchNewsDta: INTERRUPTED", ie);
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeaturesFromJson(jsonResponse);
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
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeaturesFromJson(String newsJSON) {

        String title;
        String section;
        String date;
        String url;
        String author = "";

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON Response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONObject associated with key called "response"
            JSONObject baseJsonResponseResult = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of items (or news articles).
            JSONArray currentNewsArticles = baseJsonResponseResult.getJSONArray("results");

            // For each article in the currentNewsArticles array, create an {@link News} object.
            for (int i = 0; i < currentNewsArticles.length(); i++) {

                // Get a single news article at position i within the list of articles
                JSONObject currentArticle = currentNewsArticles.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                title = currentArticle.getString("webTitle");

                // Extract the value for the key called "sectionName"
                section = currentArticle.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                date = currentArticle.getString("webPublicationDate");

                // Extract the value for the key called "webUrl"
                url = currentArticle.getString("webUrl");

                // AUTHORS
                // Extract the JSONArray associated with the key called "tags"
                // Some articles don't have tags node, use try/catch to prevent null pointers.
                JSONArray tagsArray = currentArticle.getJSONArray("tags");
                for (int j = 0; j < tagsArray.length(); j++) {
                    JSONObject currentAuthor = tagsArray.getJSONObject(j);
                    try {
                        // Extract the value for the key called "webTitle" (author)
                        author = "by " + currentAuthor.getString("webTitle");
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Missing one or more author's name JSONObject");
                    }
                }

                // Create a new {@link News} object with the title, section, date,
                // and url from the JSON response.
                News news = new News(title, section, date, url, author);
                // Add the new {@link Book} to the list of books.
                newsList.add(i, news);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news articles
        return newsList;
    }

}
