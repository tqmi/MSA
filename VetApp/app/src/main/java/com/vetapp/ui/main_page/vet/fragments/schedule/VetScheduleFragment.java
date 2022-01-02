package com.vetapp.ui.main_page.vet.fragments.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vetapp.databinding.VetFragmentScheduleBinding;

public class VetScheduleFragment extends Fragment {

    private VetScheduleViewModel vetScheduleViewModel;
    private VetFragmentScheduleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vetScheduleViewModel =
                new ViewModelProvider(this).get(VetScheduleViewModel.class);

        binding = VetFragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}