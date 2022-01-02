package com.example.MyServer;

import okhttp3.*;
import org.apache.http.HttpHeaders;

public class SendMsgService {

    private static final String URL_DART_HEROKU = "https://dart-application.herokuapp.com";

    public void sendMySelf() {
        Response response = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL_DART_HEROKU)
                    .build();

            response = client.newCall(request).execute();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.body().close();
                }
                catch (final Throwable th) {
                    System.out.println(th.getMessage());
                }
            }
        }
    }
}
