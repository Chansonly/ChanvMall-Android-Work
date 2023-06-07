package cn.baiyun.androidwork2.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.baiyun.androidwork2.face.OnPageReceviedListener;

public class PageHttpTask extends AsyncTask<String, Void, String> {
    private OnPageReceviedListener mListener;

    public PageHttpTask(OnPageReceviedListener listener) {
        mListener = listener;
    }
    @Override
    protected void onPostExecute(String s) {
        mListener.onPageReceived(s);
    }

    @Override
    protected String doInBackground(String... params) {
        String urlStr = params[0];
        String postData = params[1];
        String result = "";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为POST
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 启用输入和输出流
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // 设置请求内容的类型
            connection.setRequestProperty("Content-Type", "application/json");

            // 发送请求数据
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            Log.i("my","pageData:"+postData);
            outputStream.writeBytes(postData);
            outputStream.flush();
            outputStream.close();

            // 获取响应数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();

            // 断开连接
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
