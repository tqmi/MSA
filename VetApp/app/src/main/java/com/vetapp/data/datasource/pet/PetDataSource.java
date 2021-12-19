package com.vetapp.data.datasource.pet;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vetapp.data.datasource.DBRef;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.data.persistent.user.UserState;

public class PetDataSource {

    private static final long ONE_MEGABYTE = 1024 * 1024;

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

    public static void getPetImage(Pet pet, OnCompleteListener<byte[]> callback){
        getUserPetImage(UserState.getUID(),pet,callback);
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

    public static void getUserPetImage(String userid, Pet pet, OnCompleteListener<byte[]> callback){

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference imref = ref.child(userid).child(pet.getDocid()).child("profile.png");

        Log.d("img :", imref.getBucket());

//        imref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                Log.d("download uri:",task.getResult().toString());
//            }
//        });

        imref.getBytes(ONE_MEGABYTE).addOnCompleteListener(callback);

    }

}
