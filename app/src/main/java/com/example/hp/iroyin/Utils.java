package com.example.hp.iroyin;

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

public final class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static final int CONNECT_TIMEOUT = 15000; /* milliseconds */
    private static final int READ_TIMEOUT = 10000; /* milliseconds */
    private static Object results;

    private Utils() {
        }



     public static List<News> fetchNewsData(String qUrl) {
        Log.i(TAG, "TEST: fetchNewsData() called ... ");
        URL url = writeUrl(qUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "issues making the HTTP request.", e);
        }
        List<News> news = fetchNewsData(jsonResponse);
        return news;
    }



   private static List<News> fetchResultJson(String jsonString){
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        List<News> freshNews = new ArrayList<>();
        try {
            JSONObject start = new JSONObject(jsonString);
            JSONArray newsArray = start.optJSONArray("results");
            if (results == null) return null;
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
              //  JSONObject properties = currentEarthquake.getJSONObject("properties");
                final String title = currentNews.optString("title");
                final String section = currentNews.optString("section");
                final String author = currentNews.optString("author");
                final String url = currentNews.optString("url");
                News news = new News(title, section, author, url);
                freshNews.add(news);
            }

        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the news JSON results", e);
        }
        return freshNews;
    }


    private static URL writeUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "challenge building the URL ", e);
        }
        return url;
    }



    private static String makeHttpRequest(URL url)throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = convertStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the news JSON results.", e);
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

    private static String convertStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bReader = new BufferedReader(inputStreamReader);
            String line = bReader.readLine();
            while (line != null) {
                output.append(line);
                line = bReader.readLine();
            }
        }
        return output.toString();
    }
}
