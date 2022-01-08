package com.vetapp.ui.main_page.client.activities.appointment;

import static com.vetapp.business.util.LogHelper.getTag;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.data.models.vet.Vet;
import com.vetapp.databinding.ClientNewAppointmentActivityBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        Button submitBtn = binding.submitBtn;

        viewModel = new ViewModelProvider(this, new NewAppointmentViewModelFactory()).get(NewAppointmentViewModel.class);

        Vet model = (Vet) getIntent().getExtras().get("model");

        setupSelectPetSpinner(selectPet);
        setupSelectVisitTypeSpinner(selectVisitType);
        setupSelectTimeSlotSpinner(selectTimeSlot, model);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Schedule.TimeSlot oldts = timeSlots.get(0);
//                Schedule.TimeSlot newts = new Schedule.TimeSlot(oldts.getStart(), Schedule.TimeSlot.TimeSlotStatus.BUSY);
//
//                VetDataSource.updateScheduleTimeSlot(Calendar.getInstance(), model, oldts, newts, new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//
//                    }
//                });

            }
        });


    }

    private void setupSelectTimeSlotSpinner(Spinner selectTimeSlot, Vet model) {
        List<Schedule.TimeSlot> timeSlots = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter<Schedule.TimeSlot>(getApplicationContext(),
                android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTimeSlot.setAdapter(adapter);

        VetDataSource.setChangeListenerSchedule(Calendar.getInstance(), model, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(getTag(), "Error occured on schdule listener: " + error.getMessage());
                    return;
                }

                Schedule sc = value.toObject(Schedule.class);
                if (sc != null) {
                    Log.d(getTag(), "got schedule successfully" + sc);

                    List<Schedule.TimeSlot> availableTimeSlots = sc.getTimeSlots().stream().filter(new Predicate<Schedule.TimeSlot>() {
                        @Override
                        public boolean test(Schedule.TimeSlot timeSlot) {
                            if (timeSlot.getStatus() == Schedule.TimeSlot.TimeSlotStatus.FREE)
                                return true;
                            return false;
                        }
                    }).sorted(new Comparator<Schedule.TimeSlot>() {
                        @Override
                        public int compare(Schedule.TimeSlot o1, Schedule.TimeSlot o2) {
                            return o1.compare(o2);
                        }
                    }).collect(Collectors.toList());


                    timeSlots.clear();
                    timeSlots.addAll(availableTimeSlots);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d(getTag(), "schedule not found");
                    VetDataSource.createScheduleFromTemplate(Calendar.getInstance(), model, null);
                }

            }
        });

    }

    private void setupSelectVisitTypeSpinner(Spinner selectVisitType) {

    }

    private void setupSelectPetSpinner(Spinner selectPet) {

    }
}
