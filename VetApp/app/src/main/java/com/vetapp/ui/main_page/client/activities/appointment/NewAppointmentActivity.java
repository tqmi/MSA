package com.vetapp.ui.main_page.client.activities.appointment;

import static com.vetapp.business.util.LogHelper.getTag;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.data.models.vet.Vet;
import com.vetapp.databinding.ClientNewAppointmentActivityBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewAppointmentActivity extends AppCompatActivity {

    private ClientNewAppointmentActivityBinding binding;
    private NewAppointmentViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ClientNewAppointmentActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spinner selectPet = binding.selectPet;
        Spinner selectVisitType = binding.selectVisitType;
        Spinner selectTimeSlot = binding.selectTimeSlot;
        TextView selectDate = binding.editTextDate;

        List<Schedule.TimeSlot> timeSlots = new ArrayList<>();

        SpinnerAdapter adapter = new ArrayAdapter<Schedule.TimeSlot>(getApplicationContext(),
                android.R.layout.simple_spinner_item, timeSlots);
        selectTimeSlot.setAdapter(adapter);

        viewModel = new ViewModelProvider(this, new NewAppointmentViewModelFactory()).get(NewAppointmentViewModel.class);

        Vet model = (Vet) getIntent().getExtras().get("model");

        VetDataSource.getVetShedule(Calendar.getInstance(), model, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(getTag(), "db read successful");
                    Schedule sc = task.getResult().toObject(Schedule.class);
                    if (sc != null) {
                        Log.d(getTag(), "got schedule successfully" + sc);

                        timeSlots.clear();
                        timeSlots.addAll(sc.getTimeSlots());


                    } else {
                        Log.d(getTag(), "schedule not found");
                        VetDataSource.createScheduleFromTemplate(Calendar.getInstance(), model, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                VetDataSource.getVetShedule(Calendar.getInstance(), model, new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(getTag(), "db read successful");
                                            Schedule sc = task.getResult().toObject(Schedule.class);
                                            if (sc != null) {
                                                Log.d(getTag(), "got schedule successfully" + sc);

                                                timeSlots.clear();
                                                timeSlots.addAll(sc.getTimeSlots());


                                            } else {
                                                Log.d(getTag(), "schedule not found");

                                            }
                                        } else {
                                            Log.d(getTag(), "db read failed");
                                        }
                                    }
                                });
                            }
                        });
                    }
                } else {
                    Log.d(getTag(), "db read failed");
                }
            }
        });

    }
}
