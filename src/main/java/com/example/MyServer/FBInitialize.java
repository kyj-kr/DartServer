package com.example.MyServer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Service
public class FBInitialize {

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccountStream = new FileInputStream("src/main/resources/dart-1f534-firebase-adminsdk-3ytt9-4c730476d5.json");
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .setDatabaseUrl("https://dart-1f534-default-rtdb.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
