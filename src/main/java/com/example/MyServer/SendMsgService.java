package com.example.MyServer;

import okhttp3.*;
import org.apache.http.HttpHeaders;

public class SendMsgService {

    private static final String URL_DART_HEROKU = "https://dart-application.herokuapp.com/token";

    public void sendMySelf() {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create("", MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(URL_DART_HEROKU)
                    .get()
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();

            Response response = client.newCall(request).execute();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
