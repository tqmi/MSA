package com.vetapp.ui.home.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vetapp.business.login.LoginHandler;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.databinding.FragmentHomeBinding;
import com.vetapp.ui.addpet.AddPetActivity;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button btnAddPet = binding.btnAddPet;
        RecyclerView layout = binding.idRVPets;

        btnAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPetActivity.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        layout.setLayoutManager(layoutManager);
        homeViewModel.getPets().observe(getViewLifecycleOwner(), new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                PetAdapter adapter = new PetAdapter(getContext(),pets);
                layout.setAdapter(adapter);
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}