package com.vetapp.ui.main_page.client.activities.appointment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vetapp.databinding.ClientNewAppointmentActivityBinding;

public class NewAppointmentActivity extends AppCompatActivity {

    private ClientNewAppointmentActivityBinding binding;
    private NewAppointmentViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ClientNewAppointmentActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, new NewAppointmentViewModelFactory()).get(NewAppointmentViewModel.class);

    }
}
