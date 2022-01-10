package com.vetapp.ui.main_page.vet.activities.addprescription;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.R;
import com.vetapp.data.datasource.medication.MedicationDataSource;
import com.vetapp.data.datasource.pet.PetDataSource;
import com.vetapp.data.models.medication.Medicine;
import com.vetapp.data.models.medication.Prescription;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.databinding.VetAddPrescriptionActivityBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPrescriptionActivity extends AppCompatActivity {

    private VetAddPrescriptionActivityBinding binding;

    private List<Schedule.TimePoint> rvData;
    private List<Medicine> medications = new ArrayList<>();
    private Date startTime;
    private Date endTime;
    private String uid, pid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = VetAddPrescriptionActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView rv = binding.recyclerView;
        TextView tvStart = binding.startTime;
        TextView tvEnd = binding.endTime;
        Spinner spnMedicine = binding.spinner;
        Button addBtn = binding.AddTimeBtn;
        Button submitBtn = binding.submitBtn;
        TextView tvQuantity = binding.quantity;

        uid = getIntent().getExtras().getString("ownerID");
        pid = getIntent().getExtras().getString("petID");


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        rvData = new ArrayList<>();
        rvData.add(new Schedule.TimePoint(0, 0));

        AddPrespTimeAdapter adapter = new AddPrespTimeAdapter(rvData);
        rv.setAdapter(adapter);

        Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateStrat = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                tvStart.setText(dateFormat.format(myCalendar.getTime()));
                startTime = myCalendar.getTime();
            }
        };
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddPrescriptionActivity.this, dateStrat, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                tvEnd.setText(dateFormat.format(myCalendar.getTime()));
                endTime = myCalendar.getTime();
            }
        };
        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddPrescriptionActivity.this, dateEnd, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        ArrayAdapter<Medicine> arrayAdapter = new ArrayAdapter<Medicine>(getApplicationContext(), android.R.layout.simple_spinner_item, medications);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMedicine.setAdapter(arrayAdapter);

        MedicationDataSource.addChangeListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    return;
                medications.clear();
                medications.addAll(value.toObjects(Medicine.class));
                arrayAdapter.notifyDataSetChanged();
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvData.add(new Schedule.TimePoint(0, 0));
                adapter.notifyDataSetChanged();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = tvQuantity.getText().toString();
                Prescription data = new Prescription((Medicine) spnMedicine.getSelectedItem(), Integer.parseInt(q), new Timestamp(endTime), new Timestamp(startTime), rvData);
                PetDataSource.addPetPrescription(uid, pid, data, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        AddPrescriptionActivity.this.finish();
                    }
                });
            }
        });

    }

    public class AddPrespTimeAdapter extends RecyclerView.Adapter<AddPrespTimeAdapter.ViewHolder> {

        private List<Schedule.TimePoint> data;

        public AddPrespTimeAdapter(List<Schedule.TimePoint> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vet_add_prescription_time_cardview_layout, parent, false);
            return new AddPrespTimeAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Schedule.TimePoint model = data.get(position);
            holder.tvTime.setText(model.toString());
            holder.tvTime.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(AddPrescriptionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            model.setHour(selectedHour);
                            model.setMinute(selectedMinute);
                            holder.tvTime.setText(model.toString());
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(model);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tvTime;
            private Button btnDelete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tvTime = itemView.findViewById(R.id.card_apt_time);
                btnDelete = itemView.findViewById(R.id.card_apt_delete_btn);

            }
        }
    }


}
