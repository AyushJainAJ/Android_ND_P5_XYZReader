package com.example.xyzreader.network;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteEndpointUtil {
    private static final String TAG = "RemoteEndpointUtil";

    private RemoteEndpointUtil() {
    }

    public static JSONArray fetchJsonArray() {
        String itemsJson = null;

        try {
            itemsJson = fetchPlainText(Config.BASE_URL);
        } catch (IOException e) {

            return null;
        }

        try {
            JSONTokener tokener = new JSONTokener(itemsJson);
            Object val = tokener.nextValue();

            if (!(val instanceof JSONArray))
                throw new JSONException("Expected JSONArray");

            return (JSONArray) val;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    static String fetchPlainText(URL url) throws IOException {

        return new String(fetch(url), "UTF-8");
    }

    static byte[] fetch(URL url) throws IOException {
        InputStream in = null;

        try {
            OkHttpClient client = new OkHttpClient();
            HttpURLConnection conn = client.open(url);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) > 0)
                out.write(buffer, 0, bytesRead);

            return out.toByteArray();

        } catch(IOException e) {
            e.printStackTrace();
        }
        finally {

            if (in != null)
                in.close();
        }
        return null;
    }
}