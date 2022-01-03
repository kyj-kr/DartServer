package com.example.MyServer.mapper;

import com.example.MyServer.MyServerApplication;
import okhttp3.*;

public class FirebaseDbService {

    private static String FIREBASE_DB_URL = "https://dart-1f534-default-rtdb.firebaseio.com/users";

    public void updateAlarmList(String androidId, String deviceToken, String corpInfos) throws Exception {
        String message = "{\n" +
                "    \"deviceToken\": \"" + deviceToken + "\",\n" +
                "    \"corpNames\": \"" + corpInfos + "\"\n" +
                "}";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(FIREBASE_DB_URL + "/" + androidId + ".json")
                .put(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {
            // user 정보 업데이트
            MyServerApplication.getUserDatas();
        }
    }
}
