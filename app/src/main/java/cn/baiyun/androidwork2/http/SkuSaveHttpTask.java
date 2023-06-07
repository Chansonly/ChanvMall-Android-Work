package cn.baiyun.androidwork2.http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.baiyun.androidwork2.face.OnDataReceivedListener;
import cn.baiyun.androidwork2.face.OnSkuSaveReceivedListener;

public class SkuSaveHttpTask extends AsyncTask<String, Void, String> {
    private OnSkuSaveReceivedListener mListener;

    public SkuSaveHttpTask(OnSkuSaveReceivedListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.onSkuSaveReceived(result);
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response.toString();
    }

}
