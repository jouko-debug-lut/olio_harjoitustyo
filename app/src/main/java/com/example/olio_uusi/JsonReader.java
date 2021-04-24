package com.example.olio_uusi.ui;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

    // help got from https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java

    // this is used to convert the Buffered reader result to String. It is used in the JSON function.
    // this function returns the reader to string.
    private static String convertToString(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = reader.read()) != -1) {
            sb.append((char) i);
        }
        return sb.toString();
    }

    // With this we convert the inserted url string to json object
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {

        // We need to use StrictMode to catch accidental disk or network access on the application's main thread.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        InputStream inputStream = new URL(url).openStream();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = convertToString(bufferedReader);
            JSONObject json = new JSONObject(jsonText);

            return json;

        } finally {
            inputStream.close();
        }
    }

}
