package com.vetapp.data.datasource.pet;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.data.datasource.DBRef;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.data.persistent.user.UserState;

public class PetDataSource {


    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference usersColRef = firestore.collection(DBRef.USER_COL);

    //Current user
    public static void setChangeListenerPets(EventListener<QuerySnapshot> callback){
        setChangeListenerUserPets(UserState.getUID(),callback);
    }

    public static void loadPets(OnCompleteListener<QuerySnapshot> callback){
        loadUserPets(UserState.getUID(),callback);
    }

    public static void writePet(Pet pet, OnCompleteListener callback){
        writeUserPet(UserState.getUID(),pet,callback);
    }



    //Generic
    public static void loadUserPet(String userid,String petid, OnCompleteListener<DocumentSnapshot> callback){
        usersColRef.document(userid).collection(DBRef.PET_COL).document(petid).get().addOnCompleteListener(callback);
    }

    public static void loadUserPets(String userid, OnCompleteListener<QuerySnapshot> callback){
        usersColRef.document(userid).collection(DBRef.PET_COL).get().addOnCompleteListener(callback);
    }

    public static void setChangeListenerUserPets(String userid, EventListener<QuerySnapshot> callback){
        usersColRef.document(userid).collection(DBRef.PET_COL).addSnapshotListener(callback);
    }

    public static void writeUserPet(String userid,Pet pet, OnCompleteListener callback){
        usersColRef.document(userid).collection(DBRef.PET_COL).add(pet).addOnCompleteListener(callback);
    }

}
