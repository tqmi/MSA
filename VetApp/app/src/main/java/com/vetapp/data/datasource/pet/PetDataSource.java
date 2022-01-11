package com.vetapp.data.datasource.pet;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vetapp.data.datasource.DBRef;
import com.vetapp.data.models.appointment.Appointment;
import com.vetapp.data.models.medication.Prescription;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.data.persistent.user.UserState;

public class PetDataSource {

    private static final long ONE_MEGABYTE = 1024 * 1024 * 10;

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference usersColRef = firestore.collection(DBRef.USER_COL);

    //Current user
    public static ListenerRegistration setChangeListenerPets(EventListener<QuerySnapshot> callback) {
        return setChangeListenerUserPets(UserState.getUID(), callback);
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

    public static void writePetImage(Uri imuri,String petId, OnCompleteListener callback){
        writeUserPetImage(UserState.getUID(),petId,imuri,callback);
    }

    public static void deletePet(Pet pet) {
        deleteUserPet(UserState.getUID(), pet);
    }

    public static void setPetImageTrue(String id) {
        usersColRef.document(UserState.getUID()).collection(DBRef.PET_COL).document(id).update("hasPicture", true);
    }

    public static void updatePet(Pet pet, OnCompleteListener callback) {
        usersColRef.document(UserState.getUID()).collection(DBRef.PET_COL).document(pet.getDocid()).set(pet).addOnCompleteListener(callback);
    }

    //Generic
    public static void addPetPrescription(String userid, String petid, Prescription data, OnCompleteListener callback) {
        usersColRef.document(userid).collection(DBRef.PET_COL).document(petid).update("prescriptions", FieldValue.arrayUnion(data)).addOnCompleteListener(callback);
    }

    public static void addPetAppointment(String userid, String petid, Appointment data, OnCompleteListener callback) {
        usersColRef.document(userid).collection(DBRef.PET_COL).document(petid).update("appointments", FieldValue.arrayUnion(data)).addOnCompleteListener(callback);
    }

    public static void loadUserPet(String userid, String petid, OnCompleteListener<DocumentSnapshot> callback) {
        usersColRef.document(userid).collection(DBRef.PET_COL).document(petid).get().addOnCompleteListener(callback);
    }

    public static void loadUserPets(String userid, OnCompleteListener<QuerySnapshot> callback) {
        usersColRef.document(userid).collection(DBRef.PET_COL).get().addOnCompleteListener(callback);
    }

    public static ListenerRegistration setChangeListenerUserPets(String userid, EventListener<QuerySnapshot> callback) {
        return usersColRef.document(userid).collection(DBRef.PET_COL).addSnapshotListener(callback);
    }

    public static void writeUserPet(String userid, Pet pet, OnCompleteListener callback) {
        usersColRef.document(userid).collection(DBRef.PET_COL).add(pet).addOnCompleteListener(callback);
    }

    public static void getUserPetImage(String userid, Pet pet, OnCompleteListener<byte[]> callback) {

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference imref = ref.child(userid).child(pet.getDocid()).child("profile.png");

        imref.getBytes(ONE_MEGABYTE).addOnCompleteListener(callback);

    }

    private static void writeUserPetImage(String userid, String petid,Uri imuri, OnCompleteListener callback) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference imref = ref.child(userid).child(petid).child("profile.png");

        imref.putFile(imuri).addOnCompleteListener(callback);
    }

    private static void deleteUserPet(String userid, Pet pet) {
        usersColRef.document(userid).collection(DBRef.PET_COL).document(pet.getDocid()).delete();
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference imref = ref.child(userid).child(pet.getDocid()).child("profile.png");
        imref.delete();
    }

    public static void deleteAppointmentDone(String userid, String petid, Appointment data, OnCompleteListener callback) {
        usersColRef.document(userid).collection(DBRef.PET_COL).document(petid).update("appointments", FieldValue.arrayRemove(data)).addOnCompleteListener(callback);
    }


}
