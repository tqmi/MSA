package com.vetapp.ui.main_page.vet.fragments.schedule;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vetapp.R;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.data.models.vet.Vet;
import com.vetapp.data.persistent.user.UserState;
import com.vetapp.databinding.VetFragmentScheduleBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class VetScheduleFragment extends Fragment {

    private VetScheduleViewModel vetScheduleViewModel;
    private VetFragmentScheduleBinding binding;
    private RecyclerView dayLayout;
    private RecyclerView hourLayout;
    private List<Schedule.TimeSlot> hourData = new ArrayList<>();
    private Vet vet = null;

    private static int[] daycount = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static String[] monthNames = {"IAN", "FEB", "MAR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vetScheduleViewModel =
                new ViewModelProvider(this).get(VetScheduleViewModel.class);

        binding = VetFragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dayLayout = binding.scDayRecyclerView;

        LinearLayoutManager dayLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        dayLayout.setLayoutManager(dayLayoutManager);

        List<Day> dayData = generateDays(Calendar.getInstance().get(Calendar.YEAR));

        DayAdapter dayAdapter = new DayAdapter(getContext(), dayData);
        dayLayout.setAdapter(dayAdapter);

        dayLayoutManager.scrollToPosition(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1);


        hourLayout = binding.scHourRecyclerView;
        LinearLayoutManager hourLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        hourLayout.setLayoutManager(hourLayoutManager);

        HourAdapter hourAdapter = new HourAdapter(getContext(), hourData);
        hourLayout.setAdapter(hourAdapter);

        loadHourData(dayAdapter.selected);

        return root;
    }

    private List<Day> generateDays(int year) {
        List<Day> days = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int numDays = daycount[i];
            if (i == 1 && year % 4 == 0)
                numDays = 29;
            for (int j = 1; j <= numDays; j++) {
                days.add(new Day(j, i));
            }
        }
        return days;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadHourData(Day day) {

        if (vet == null) {
            VetDataSource.getVetProfile(UserState.getUID(), new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        vet = task.getResult().toObject(Vet.class);
                        loadHourData(day);
                    } else {
                        Log.d(getTag(), "Couldnt find vet: " + task.getException());
                    }
                }
            });
            return;
        }

        Calendar date = Calendar.getInstance();

        date.set(Calendar.MONTH, day.month);
        date.set(Calendar.DAY_OF_MONTH, day.day);

        Log.d(getTag(), "date : " + date);

        VetDataSource.getVetShedule(date, vet, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(getTag(), "loaded schedule");
                    Schedule schedule = task.getResult().toObject(Schedule.class);

                    hourData.clear();

                    if (schedule != null)
                        hourData.addAll(schedule.getTimeSlots().stream().filter((ts) -> {
                            if (ts.getStatus() == Schedule.TimeSlot.TimeSlotStatus.BUSY)
                                return true;
                            return false;
                        }).collect(Collectors.toList()));

                    hourLayout.getAdapter().notifyDataSetChanged();

                } else {
                    Log.d(getTag(), "error loading schedule : " + task.getException());
                }
            }
        });


    }

    public class Day {
        private int day;
        private int month;
        private boolean selected = false;

        public Day(int day, int month) {
            this.day = day;
            this.month = month;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

        private Context context;
        private List<Day> data;
        public Day selected;
        private ViewHolder selectedViewHolder;

        public DayAdapter(Context context, List<Day> data) {
            this.context = context;
            this.data = data;
            this.selected = data.get(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1);
            selected.setSelected(true);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_day_cardview_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Day model = data.get(position);
            holder.tvMonth.setText(monthNames[model.getMonth()]);
            holder.tvDay.setText(model.getDay() + "");
            if (model.isSelected()) {
                holder.layout.setBackgroundColor(0xffa047ff);
            } else {
                holder.layout.setBackgroundColor(0xffffffff);
            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected.setSelected(false);

                    selected = model;

                    model.setSelected(true);
                    dayLayout.getAdapter().notifyDataSetChanged();
                    loadHourData(model);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvMonth, tvDay;
            private RelativeLayout layout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.card_sc_day_layout);
                tvMonth = itemView.findViewById(R.id.card_sc_day_month);
                tvDay = itemView.findViewById(R.id.card_sc_day_day);
            }
        }
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_hour_cardview_layout, parent, false);
            return new HourAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Schedule.TimeSlot model = data.get(position);
            holder.tvStartTime.setText(model.getStart().toString());
            holder.tvEndTime.setText(model.getStart().add(model.getAppointment().getVisitType().getDuration()).toString());
            holder.tvConsultationType.setText(model.getAppointment().getVisitType().getName());
            holder.tvPetName.setText(model.getAppointment().getPetName());
            holder.tvOwnerName.setText(model.getAppointment().getOwnerName());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ConstraintLayout timeLayout, infoLayout;
            private TextView tvStartTime, tvEndTime, tvConsultationType, tvPetName, tvOwnerName;
            private Button btnDone;

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
            }
        }
    }


}