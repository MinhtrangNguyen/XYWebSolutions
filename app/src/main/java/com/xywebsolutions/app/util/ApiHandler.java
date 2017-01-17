package com.xywebsolutions.app.util;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiHandler {
    OkHttpClient client;

    public String getFromURL(String url) throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String submit(String name, String company_name, String email, String phone, String content) throws IOException {
        client = new OkHttpClient();

        String _url = "http://api-xywebsolutions.kennjdemo.com/v1/ticket/submit";
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("company_name", company_name)
                .add("email", email)
                .add("phone", phone)
                .add("content", content)
                .build();
        Request request = new Request.Builder()
                .url(_url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
