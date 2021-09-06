package com.example.MyServer.mapper;

import com.example.MyServer.domain.TokenVo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import okhttp3.*;
import org.springframework.stereotype.Service;

@Service
public class FireDbServiceTest {

    private final String URL = "https://dart-1f534-default-rtdb.firebaseio.com/users";
    public static final String COL_NAME = "users";

    public String createUserDetail(TokenVo tokenVo) throws Exception {
        String message = "{\n" +
                "    \"deviceToken\": \"1231\",\n" +
                "    \"corpNames\": \"3S\"\n" +
                "}";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(URL + ".json")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        String responseBodyString = response.body().string();
        return responseBodyString;
    }

    public TokenVo getUserDetail(String deviceToken) throws Exception {
        return null;
    }

    public String updateUserDetail(TokenVo tokenVo) throws Exception {
        String message = "{\n" +
                "    \"deviceToken\": \"1231\",\n" +
                "    \"corpNames\": \"3S\"\n" +
                "}";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(URL + ".json")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        String responseBodyString = response.body().string();
        System.out.println("responseBodyString: " + responseBodyString);
        return responseBodyString;
    }

    public String deleteUserDetail(String deviceToken) throws Exception {
        return null;
    }
}
