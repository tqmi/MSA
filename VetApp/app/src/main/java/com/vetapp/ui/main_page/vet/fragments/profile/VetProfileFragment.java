package com.vetapp.ui.main_page.vet.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vetapp.data.models.pet.Pet;
import com.vetapp.databinding.VetFragmentProfileBinding;
import com.vetapp.ui.main_page.client.fragments.pets.pet_card_view.PetAdapter;

import java.util.List;

public class VetProfileFragment extends Fragment {

    private VetProfileViewModel vetProfileViewModel;
    private VetFragmentProfileBinding binding;
    private PetAdapter adapter;
    private List<Pet> data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vetProfileViewModel =
                new ViewModelProvider(this).get(VetProfileViewModel.class);
//        inflater.inflate(R.layout.fragment_home,container,false);
        binding = VetFragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}