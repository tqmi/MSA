package com.vetapp.ui.main_page.client.activities.appointment;

import static com.vetapp.business.util.LogHelper.getTag;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.data.models.vet.Vet;
import com.vetapp.data.models.vet.VisitType;
import com.vetapp.data.persistent.user.UserState;
import com.vetapp.databinding.ClientNewAppointmentActivityBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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
        EditText selectDate = binding.editTextDate;
        Button submitBtn = binding.submitBtn;
        selectVisitType.setEnabled(false);
        selectTimeSlot.setEnabled(false);
        selectDate.setEnabled(true);
        submitBtn.setEnabled(false);

        viewModel = new ViewModelProvider(this, new NewAppointmentViewModelFactory()).get(NewAppointmentViewModel.class);

        Vet model = (Vet) getIntent().getExtras().get("model");

        setupSelectPetSpinner(selectPet);
        setupSelectVisitTypeSpinner(selectVisitType, model);
        setupSelectTimeSlotSpinner(selectTimeSlot, model);
        setupselectDatePicker(selectDate);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    private void setupselectDatePicker(EditText selectDate) {

        Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                selectDate.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewAppointmentActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

    private void setupSelectVisitTypeSpinner(Spinner selectVisitType, Vet vet) {
        List<VisitType> visitTypes = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter<VisitType>(getApplicationContext(),
                android.R.layout.simple_spinner_item, visitTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectVisitType.setAdapter(adapter);

        VetDataSource.setChangeListenerVisitTypes(vet, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(getTag(), "Error getting visit types : " + error.getMessage());
                    return;
                }
                visitTypes.clear();
                visitTypes.addAll(value.toObjects(VisitType.class));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupSelectPetSpinner(Spinner selectPet) {
        ArrayAdapter adapter = new ArrayAdapter<Pet>(getApplicationContext(),
                android.R.layout.simple_spinner_item, UserState.getCurrentUser().getData().getPets());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectPet.setAdapter(adapter);


    }
}
