package com.vetapp.data.datasource.user;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vetapp.data.datasource.DBRef;

public class VetDataSource {

    private static final long ONE_MEGABYTE = 1024 * 1024;

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference usersColRef = firestore.collection(DBRef.USER_COL);


    public static void getVetProfilePicture(String vetID, OnCompleteListener<byte[]> callback) {

        Log.d("debug", vetID);

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference imref = ref.child(vetID).child("profile.png");

        imref.getBytes(ONE_MEGABYTE).addOnCompleteListener(callback);
    }

    public static void setChangeListenerVets(EventListener<QuerySnapshot> callback) {
        usersColRef.whereEqualTo("type", "VET").addSnapshotListener(callback);
    }

}
