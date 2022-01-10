package com.vetapp.ui.main_page.client.activities.vetdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vetapp.data.models.vet.Vet;
import com.vetapp.databinding.ClientVetDetailActivityBinding;

public class VetDetailsActivity extends AppCompatActivity {

    private VetDetailsViewModel viewModel;
    private ClientVetDetailActivityBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button goBackbtn = binding.btnDetailsToVetList;
        binding = ClientVetDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, new VetDetailsViewModelFactory()).get(VetDetailsViewModel.class);
        TextView textView = binding.textView3;

        Vet model = (Vet) getIntent().getExtras().get("model");

        textView.setText(model.getName());

        goBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
