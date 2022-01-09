package com.vetapp.ui.main_page.client.activities.appointment;

import static com.vetapp.business.util.LogHelper.getTag;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.appointment.Appointment;
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

    private List<Schedule.TimeSlot> timeSlots = new ArrayList<>();
    private ArrayAdapter selectTimeSlotAdapter;
    private ListenerRegistration selectTimeSlotListener;

    private List<VisitType> visitTypes = new ArrayList<>();
    private ArrayAdapter selectVisitTypeAdapter;
    private ListenerRegistration selectVisitTypeListener;

    private ArrayAdapter selectPetAdapter;

    private Appointment appointmentdata;

    private Spinner selectTimeSlot;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ClientNewAppointmentActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spinner selectPet = binding.selectPet;
        Spinner selectVisitType = binding.selectVisitType;
        selectTimeSlot = binding.selectTimeSlot;
        EditText selectDate = binding.editTextDate;
        Button submitBtn = binding.submitBtn;
        selectVisitType.setEnabled(true);
        selectTimeSlot.setEnabled(false);
        selectDate.setEnabled(true);
        submitBtn.setEnabled(false);

        viewModel = new ViewModelProvider(this, new NewAppointmentViewModelFactory()).get(NewAppointmentViewModel.class);

        Vet model = (Vet) getIntent().getExtras().get("model");

        setupSelectPetSpinner(selectPet);
        setupSelectVisitTypeSpinner(selectVisitType, model);
        setupSelectTimeSlotSpinner(selectTimeSlot);
        setupselectDatePicker(selectDate);

        Pet selectedPet = (Pet) selectPet.getSelectedItem();

        appointmentdata = new Appointment(selectedPet.getName(), UserState.getCurrentUser().getData().getFullName(), selectedPet.getDocid(), UserState.getUID(), (VisitType) selectVisitType.getSelectedItem(), null, null);

        selectPet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appointmentdata.setPetName(UserState.getCurrentUser().getData().getPets().get(position).getName());
                appointmentdata.setPetId(UserState.getCurrentUser().getData().getPets().get(position).getDocid());
                Log.d(getTag(), "appointmentData : " + appointmentdata);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                appointmentdata.setPetName("");
                appointmentdata.setPetId("");
            }
        });

        selectVisitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appointmentdata.setVisitType(visitTypes.get(position));
                Log.d(getTag(), "appointmentData : " + appointmentdata);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                appointmentdata.setVisitType(null);
            }
        });

        selectTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appointmentdata.setTimeSlot(timeSlots.get(position));
                Log.d(getTag(), "appointmentData : " + appointmentdata);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                appointmentdata.setTimeSlot(null);
            }
        });

        selectDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(appointmentdata.getDate().toDate());
                addSelectTimeSlotListener(cal, model);
                selectTimeSlot.setEnabled(true);
                submitBtn.setEnabled(true);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentdata.setPetName(((Pet) selectPet.getSelectedItem()).getName());
                appointmentdata.setPetId(((Pet) selectPet.getSelectedItem()).getDocid());
                appointmentdata.setVisitType((VisitType) selectVisitType.getSelectedItem());
                appointmentdata.setTimeSlot((Schedule.TimeSlot) selectTimeSlot.getSelectedItem());

                Schedule.TimeSlot nTimeslot = new Schedule.TimeSlot(appointmentdata.getTimeSlot().getStart(), Schedule.TimeSlot.TimeSlotStatus.BUSY, appointmentdata);

                Calendar cal = Calendar.getInstance();
                cal.setTime(appointmentdata.getDate().toDate());
                VetDataSource.updateScheduleTimeSlot(cal, model, appointmentdata.getTimeSlot(), nTimeslot, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        finish();
                    }
                });

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
                String myFormat = "dd/MM/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                appointmentdata.setDate(new Timestamp(myCalendar.getTime()));
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

    private void setupSelectTimeSlotSpinner(Spinner selectTimeSlot) {

        selectTimeSlotAdapter = new ArrayAdapter<Schedule.TimeSlot>(getApplicationContext(),
                android.R.layout.simple_spinner_item, timeSlots);
        selectTimeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTimeSlot.setAdapter(selectTimeSlotAdapter);


    }

    private void addSelectTimeSlotListener(Calendar date, Vet vet) {

        if (selectTimeSlotListener != null)
            selectTimeSlotListener.remove();

        selectTimeSlotListener = VetDataSource.setChangeListenerSchedule(date, vet, new EventListener<DocumentSnapshot>() {
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
                    selectTimeSlotAdapter.notifyDataSetChanged();

                } else {
                    Log.d(getTag(), "schedule not found");
                    VetDataSource.createScheduleFromTemplate(date, vet, null);
                }

            }
        });
    }

    private void setupSelectVisitTypeSpinner(Spinner selectVisitType, Vet vet) {

        selectVisitTypeAdapter = new ArrayAdapter<VisitType>(getApplicationContext(),
                android.R.layout.simple_spinner_item, visitTypes);
        selectVisitTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectVisitType.setAdapter(selectVisitTypeAdapter);

        selectVisitTypeListener = VetDataSource.setChangeListenerVisitTypes(vet, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(getTag(), "Error getting visit types : " + error.getMessage());
                    return;
                }
                visitTypes.clear();
                visitTypes.addAll(value.toObjects(VisitType.class));
                selectVisitTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupSelectPetSpinner(Spinner selectPet) {
        selectPetAdapter = new ArrayAdapter<Pet>(getApplicationContext(),
                android.R.layout.simple_spinner_item, UserState.getCurrentUser().getData().getPets());
        selectPetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectPet.setAdapter(selectPetAdapter);
    }
}
