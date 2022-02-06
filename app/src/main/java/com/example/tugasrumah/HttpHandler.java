package com.example.tugasrumah;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpHandler {
    public String sendPostReq(String reqUrl, HashMap<String, String> postDataParams) {
        URL url;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10);
            connection.setConnectTimeout(10);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);


            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8")
            );
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            outputStream.close();


            int respCode = connection.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );
                stringBuilder = new StringBuilder();
                String resp;
                while ((resp = reader.readLine()) != null) {
                    stringBuilder.append(resp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                res.append("&");
            }
            res.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            res.append("=");
            res.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
        }
        return res.toString(); }

    public String sendGetResp(String respUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(respUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            String response;
            while ((response = reader.readLine()) != null) {
                stringBuilder.append(response + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public String sendGetResp(String respUrl, String id) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(respUrl + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            String response;
            while ((response = reader.readLine()) != null) {
                stringBuilder.append(response + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

