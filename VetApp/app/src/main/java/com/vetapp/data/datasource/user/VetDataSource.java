package com.vetapp.data.datasource.user;

import static com.vetapp.business.util.LogHelper.getTag;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vetapp.data.datasource.DBRef;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.data.models.vet.Vet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VetDataSource {

    private static final long ONE_MEGABYTE = 1024 * 1024;

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference usersColRef = firestore.collection(DBRef.USER_COL);


    public static void setChangeListenerVets(EventListener<QuerySnapshot> callback) {
        usersColRef.whereEqualTo("type", "VET").addSnapshotListener(callback);
    }

    public static void getVetProfilePicture(String vetID, OnCompleteListener<byte[]> callback) {

        Log.d("debug", vetID);

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference imref = ref.child(vetID).child("profile.png");

        imref.getBytes(ONE_MEGABYTE).addOnCompleteListener(callback);
    }

    public static void getVetShedule(Calendar date, Vet vet, OnCompleteListener<DocumentSnapshot> callback) {
        StringBuilder date_string = new StringBuilder();
        Log.d("date", date.get(Calendar.YEAR) + "");
        Log.d("date", date.get(Calendar.MONTH) + "");
        Log.d("date", date.get(Calendar.DAY_OF_MONTH) + "");

        date_string.append(date.get(Calendar.YEAR)).append(date.get(Calendar.MONTH)).append(date.get(Calendar.DAY_OF_MONTH));


        usersColRef.document(vet.getDocid()).collection(DBRef.SCHEDULE_COL).document(date_string.toString()).get().addOnCompleteListener(callback);
    }

    public static void addVetScheduleTemplate(String vetid) {
        Schedule.TimePoint startTime = new Schedule.TimePoint(10, 00);
        Schedule.TimePoint endTime = new Schedule.TimePoint(20, 00);
        List<Schedule.TimeSlot> timeSlots = new ArrayList<>();

        for (int i = startTime.getHour(); i < endTime.getHour(); i++) {
            for (int j = 0; j < 4; j++) {
                timeSlots.add(new Schedule.TimeSlot(new Schedule.TimePoint(i, j * 15), Schedule.TimeSlot.TimeSlotStatus.FREE));
            }
        }

        Schedule template = new Schedule(startTime, endTime, timeSlots);
        usersColRef.document(vetid).collection(DBRef.SCHEDULE_COL).document("template").set(template);
    }

    public static void createScheduleFromTemplate(Calendar date, Vet vet, OnCompleteListener callback) {
        usersColRef.document(vet.getDocid()).collection(DBRef.SCHEDULE_COL).document("template").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Schedule tempalte = task.getResult().toObject(Schedule.class);
                    StringBuilder date_string = new StringBuilder();
                    Log.d("date", date.get(Calendar.YEAR) + "");
                    Log.d("date", date.get(Calendar.MONTH) + "");
                    Log.d("date", date.get(Calendar.DAY_OF_MONTH) + "");

                    date_string.append(date.get(Calendar.YEAR)).append(date.get(Calendar.MONTH)).append(date.get(Calendar.DAY_OF_MONTH));
                    usersColRef.document(vet.getDocid()).collection(DBRef.SCHEDULE_COL).document(date_string.toString()).set(tempalte).addOnCompleteListener(callback);
                } else {
                    Log.d(getTag(), "Couldnt get template schedule");
                }
            }
        });
    }


}
