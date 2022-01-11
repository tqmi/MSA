package com.vetapp.data.datasource.user;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vetapp.data.datasource.DBRef;
import com.vetapp.data.models.user.User;
import com.vetapp.data.models.user.UserType;
import com.vetapp.data.persistent.user.UserState;

public class UserDataSource {

    private static final long ONE_MEGABYTE = 1024 * 1024 * 10;
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference usersColRef = firestore.collection(DBRef.USER_COL);

    public static void loadUser(FirebaseUser user, OnCompleteListener<DocumentSnapshot> callback){

        firestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(callback);

    }

    public static void setUser(User user, OnCompleteListener callback) {
        if (user.getData().getType() == UserType.VET)
            firestore.collection("Users").document(user.getUID()).set(user.getData()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    VetDataSource.addVetScheduleTemplate(user.getUID(), callback);
                }
            });
        else
            firestore.collection("Users").document(user.getUID()).set(user.getData()).addOnCompleteListener(callback);
    }

    public static void setUserProfilePicture(String userid, Uri imuri, OnCompleteListener callback) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference imref = ref.child(userid).child("profile.png");

        imref.putFile(imuri).addOnCompleteListener(callback);
    }

    public static void getProfilePicture(String uid, OnCompleteListener<byte[]> callback) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference imref = ref.child(uid).child("profile.png");

        imref.getBytes(ONE_MEGABYTE).addOnCompleteListener(callback);
    }

    public static void updateField(String field, String val, OnCompleteListener callback) {
        usersColRef.document(UserState.getUID()).update(field, val).addOnCompleteListener(callback);
    }


}
