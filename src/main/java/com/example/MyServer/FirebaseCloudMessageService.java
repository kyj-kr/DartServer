package com.example.MyServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;

import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.util.List;

public class FirebaseCloudMessageService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/dart-1f534/messages:send";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessageTo(String targetToken, String title, String body, String time) throws IOException {
        String message = makeMessage(targetToken, title, body, time);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        String accessToken = getAccessToken();
        System.out.println("accessToken: " + accessToken);
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());

    }

    private String makeMessage(String targetToken, String title, String body, String time) throws JsonProcessingException {
        FcmMessage fcmMessage =
                FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .data(FcmMessage.Data.builder()
                                .title(title)
                                .body(body)
                                .date(time)
                                .build()
                        )
                        .build()
                ).validate_only(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }


    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "dart-1f534-firebase-adminsdk-3ytt9-4c730476d5.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
