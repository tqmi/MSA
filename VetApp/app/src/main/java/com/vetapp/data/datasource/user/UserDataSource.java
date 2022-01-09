package com.vetapp.data.datasource.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vetapp.data.models.user.User;
import com.vetapp.data.models.user.UserType;

public class UserDataSource {

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

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


}
