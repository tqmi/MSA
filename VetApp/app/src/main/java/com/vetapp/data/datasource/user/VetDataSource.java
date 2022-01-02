package com.vetapp.data.datasource.user;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.data.datasource.DBRef;
import com.vetapp.data.persistent.user.UserState;

public class VetDataSource {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference usersColRef = firestore.collection(DBRef.USER_COL);

    public static void setChangeListenerVets(EventListener<QuerySnapshot> callback) {
        setChangeListenerUserVets(UserState.getUID(), callback);
    }

    private static void setChangeListenerUserVets(String uid, EventListener<QuerySnapshot> callback) {
        usersColRef.whereEqualTo("type", "VET").addSnapshotListener(callback);
    }


}
