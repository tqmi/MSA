package com.vetapp.data.datasource.user;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vetapp.R;
import com.vetapp.data.models.user.User;
import com.vetapp.data.persistent.user.UserState;

public class UserDataSource {

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void loadUser(FirebaseUser user, OnCompleteListener<DocumentSnapshot> callback){

        firestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(callback);

    }

    public static void setUser(User user, OnCompleteListener callback){
        firestore.collection("Users").document(user.getUID()).set(user.getData()).addOnCompleteListener(callback);
    }


}
