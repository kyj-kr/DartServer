package com.example.MyServer;

import com.example.MyServer.domain.TokenVo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

@Service
public class FireDbService {

    public static final String COL_NAME = "users";

    public String createUserDetail(TokenVo tokenVo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME)
                .document(tokenVo.getDeviceToken()).set(tokenVo);

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public TokenVo getUserDetail(String deviceToken) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> collectionsApiFuture = dbFirestore.collection(COL_NAME)
                .document(deviceToken).get();

        DocumentSnapshot documentSnapshot = collectionsApiFuture.get();

        TokenVo tokenVo = null;

        if(documentSnapshot.exists()) {
            tokenVo = documentSnapshot.toObject(TokenVo.class);
        }
        return tokenVo;
    }

    public String updateUserDetail(TokenVo tokenVo) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = dbFirestore.collection(COL_NAME)
                .document(tokenVo.getDeviceToken()).set(tokenVo);

        return apiFuture.get().getUpdateTime().toString();
    }

    public String deleteUserDetail(String deviceToken) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = dbFirestore.collection(COL_NAME)
                .document(deviceToken).delete();

        return "Document with User Device Token: " + deviceToken + " has been deleted";
    }
}
