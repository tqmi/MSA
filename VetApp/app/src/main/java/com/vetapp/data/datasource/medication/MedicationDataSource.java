package com.vetapp.data.datasource.medication;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.data.datasource.DBRef;

public class MedicationDataSource {

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference medColRef = firestore.collection(DBRef.MEDICINE_COL);

    public static void addChangeListener(EventListener<QuerySnapshot> callback) {
        medColRef.addSnapshotListener(callback);
    }

}
