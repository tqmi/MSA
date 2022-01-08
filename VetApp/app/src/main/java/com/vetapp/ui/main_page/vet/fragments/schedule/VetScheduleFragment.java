package com.vetapp.ui.main_page.vet.fragments.schedule;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vetapp.R;
import com.vetapp.databinding.VetFragmentScheduleBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VetScheduleFragment extends Fragment {

    private VetScheduleViewModel vetScheduleViewModel;
    private VetFragmentScheduleBinding binding;

    private static int[] daycount = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static String[] monthNames = {"IAN", "FEB", "MAR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vetScheduleViewModel =
                new ViewModelProvider(this).get(VetScheduleViewModel.class);

        binding = VetFragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView dayLayout = binding.scDayRecyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        dayLayout.setLayoutManager(layoutManager);

        List<Day> data = generateDays(Calendar.getInstance().get(Calendar.YEAR));

        DayAdapter dayAdapter = new DayAdapter(getContext(), data);
        dayLayout.setAdapter(dayAdapter);

        RecyclerView hourLayout = binding.scHourRecyclerView;


        return root;
    }

    private List<Day> generateDays(int year) {
        List<Day> days = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int numDays = daycount[i];
            if (i == 1 && year % 4 == 0)
                numDays = 29;
            for (int j = 1; j <= numDays; j++) {
                days.add(new Day(j + "", monthNames[i]));
            }
        }
        return days;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class Day {
        private String day;
        private String month;

        public Day(String day, String month) {
            this.day = day;
            this.month = month;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }

    public class DayAdapter extends RecyclerView.Adapter<DayAdapter.Viewholder> {

        private Context context;
        private List<Day> data;

        public DayAdapter(Context context, List<Day> data) {
            this.context = context;
            this.data = data;
        }

        @NonNull
        @Override
        public DayAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_day_cardview_layout, parent, false);
            return new DayAdapter.Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DayAdapter.Viewholder holder, int position) {
            Day model = data.get(position);
            holder.tvMonth.setText(model.getMonth());
            holder.tvDay.setText(model.getDay());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            private TextView tvMonth, tvDay;

            public Viewholder(@NonNull View itemView) {
                super(itemView);
                tvMonth = itemView.findViewById(R.id.card_sc_day_month);
                tvDay = itemView.findViewById(R.id.card_sc_day_day);
            }
        }
    }


}