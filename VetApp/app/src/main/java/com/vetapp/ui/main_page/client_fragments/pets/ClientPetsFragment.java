package com.vetapp.ui.main_page.client_fragments.pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vetapp.data.models.pet.Pet;
import com.vetapp.databinding.ClientFragmentPetsBinding;
import com.vetapp.ui.addpet.AddPetActivity;
import com.vetapp.ui.main_page.common.pet_card_view.PetAdapter;

import java.util.List;

public class ClientPetsFragment extends Fragment {

    private ClientPetsViewModel clientPetsViewModel;
    private ClientFragmentPetsBinding binding;
    private PetAdapter adapter;
    private List<Pet> data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clientPetsViewModel =
                new ViewModelProvider(this).get(ClientPetsViewModel.class);
//        inflater.inflate(R.layout.fragment_home,container,false);
        binding = ClientFragmentPetsBinding.inflate(inflater, container, false);
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

        data = clientPetsViewModel.getPets().getValue();

        adapter = new PetAdapter(getContext(),data);
        layout.setAdapter(adapter);


        clientPetsViewModel.getPets().observe(getViewLifecycleOwner(), new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                data.clear();
                data.addAll(pets);
                adapter.notifyDataSetChanged();
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