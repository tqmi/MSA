package com.vetapp.ui.main_page.vet.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vetapp.R;
import com.vetapp.data.datasource.pet.PetDataSource;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.data.models.vet.Vet;
import com.vetapp.data.persistent.user.UserState;
import com.vetapp.databinding.VetFragmentProfileBinding;
import com.vetapp.ui.main_page.vet.activities.addprescription.AddPrescriptionActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VetProfileFragment extends Fragment {

    private VetProfileViewModel vetProfileViewModel;
    private VetFragmentProfileBinding binding;
    private Vet vet;

    private ImageView vetProfPic;
    private TextView tvName;
    private TextView tvClinicName;
    private TextView tvAddress;
    private RatingBar rbRating;
    private List<Schedule.TimeSlot> scData = new ArrayList<>();
    private RecyclerView scLayout;
    private ProgressBar progressBar;
    private TextView freeText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vetProfileViewModel =
                new ViewModelProvider(this).get(VetProfileViewModel.class);
//        inflater.inflate(R.layout.fragment_home,container,false);
        binding = VetFragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vetProfPic = binding.vetAvatar;
        tvName = binding.vetName;
        tvClinicName = binding.vetClinicName;
        tvAddress = binding.vetAddress;
        scLayout = binding.vetScheduleLayout;
        progressBar = binding.progressBar;
        freeText = binding.vetFreeText;

        rbRating = binding.vetRating;
        rbRating.setRating(3.5f);

        VetDataSource.getVetProfile(UserState.getUID(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    vet = task.getResult().toObject(Vet.class);

                    updateFields();

                } else {
                    Log.d(getTag(), "Error loading pet:" + task.getException());
                }
            }
        });

        VetDataSource.getVetProfilePicture(UserState.getUID(), new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    byte[] im = task.getResult();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0, im.length);

                    vetProfPic.setImageBitmap(bitmap);
                } else {

                }
            }
        });

        LinearLayoutManager hourLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        scLayout.setLayoutManager(hourLayoutManager);

        HourAdapter hourAdapter = new HourAdapter(getContext(), scData);
        scLayout.setAdapter(hourAdapter);

        VetDataSource.setChangeListenerSchedule(Calendar.getInstance(), UserState.getUID(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    Log.d(getTag(), "loaded schedule");
                    Schedule schedule = value.toObject(Schedule.class);

                    progressBar.setVisibility(View.GONE);
                    scData.clear();

                    if (schedule != null) {

                        Schedule.TimeSlot tss = schedule.getTimeSlots().stream().filter((ts) -> {
                            if (ts.getStatus() == Schedule.TimeSlot.TimeSlotStatus.BUSY)
                                return true;
                            return false;
                        }).findFirst().orElse(null);
                        if (tss != null) {
                            scData.add(tss);
                            freeText.setVisibility(View.GONE);
                        } else {
                            freeText.setVisibility(View.VISIBLE);
                        }
                    }

                    scLayout.getAdapter().notifyDataSetChanged();

                } else {
                    Log.d(getTag(), "error loading schedule : " + error);
                }
            }
        });


        return root;
    }

    private void updateFields() {
        tvName.setText(vet.getName());
        tvClinicName.setText(vet.getClinicName());
        tvAddress.setText(vet.getAddress());
        rbRating.setRating(vet.getRating());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {

        private List<Schedule.TimeSlot> data;
        private Context context;

        public HourAdapter(Context context, List<Schedule.TimeSlot> data) {
            this.data = data;
            this.context = context;
        }

        @NonNull
        @Override
        public HourAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_hour_cardview_layout, parent, false);
            return new HourAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HourAdapter.ViewHolder holder, int position) {
            Schedule.TimeSlot model = data.get(position);
            holder.tvStartTime.setText(model.getStart().toString());
            holder.tvEndTime.setText(model.getStart().add(model.getAppointment().getVisitType().getDuration()).toString());
            holder.tvConsultationType.setText(model.getAppointment().getVisitType().getName());
            holder.tvPetName.setText(model.getAppointment().getPetName());
            holder.tvOwnerName.setText(model.getAppointment().getOwnerName());
            holder.btnAddPrescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AddPrescriptionActivity.class);
                    intent.putExtra("ownerID", model.getAppointment().getOwnerId());
                    intent.putExtra("petID", model.getAppointment().getPetId());
                    startActivity(intent);
                }
            });

            holder.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar date = Calendar.getInstance();

                    PetDataSource.deleteAppointmentDone(model.getAppointment().getOwnerId(), model.getAppointment().getPetId(), model.getAppointment(), null);
                    VetDataSource.markAppointmentDone(date, vet, model, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ConstraintLayout timeLayout, infoLayout;
            private TextView tvStartTime, tvEndTime, tvConsultationType, tvPetName, tvOwnerName;
            private Button btnDone, btnAddPrescription;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.timeLayout = itemView.findViewById(R.id.card_sc_hour_time_layout);
                this.infoLayout = itemView.findViewById(R.id.card_sc_hour_info_layout);
                this.tvStartTime = itemView.findViewById(R.id.card_sc_hour_start_time);
                this.tvEndTime = itemView.findViewById(R.id.card_sc_hour_end_time);
                this.tvConsultationType = itemView.findViewById(R.id.card_sc_hour_consultation_type);
                this.tvPetName = itemView.findViewById(R.id.card_sc_hour_pet_name);
                this.tvOwnerName = itemView.findViewById(R.id.card_sc_hour_owner_name);
                this.btnDone = itemView.findViewById(R.id.card_sc_hour_done_button);
                this.btnAddPrescription = itemView.findViewById(R.id.card_sc_hour_add_prescription_button);
            }
        }
    }
}