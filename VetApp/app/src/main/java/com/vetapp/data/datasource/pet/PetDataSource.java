package com.vetapp.data.datasource.pet;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.data.persistent.user.UserState;

public class PetDataSource {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void loadPet(String id, OnCompleteListener<DocumentSnapshot> callback){
        DocumentReference docref =  firestore.collection("Users").document(UserState.getCurrentUser().getUID()).collection("pets").document(id);
        docref.get().addOnCompleteListener(callback);
    }

    public static void loadPets(OnCompleteListener<QuerySnapshot> callback){
        firestore.collection("Users").document(UserState.getCurrentUser().getUID()).collection("pets").get().addOnCompleteListener(callback);
    }

    public static void setChangeListener(EventListener<QuerySnapshot> callback){
        firestore.collection("Users").document(UserState.getCurrentUser().getUID()).collection("pets").addSnapshotListener(callback);
    }

}
