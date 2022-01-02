package com.vetapp.ui.main_page.client.fragments.vets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vetapp.data.models.vet.Vet;
import com.vetapp.databinding.ClientFragmentVetsBinding;
import com.vetapp.ui.main_page.client.fragments.vets.vet_card_view.VetAdapter;

import java.util.List;

public class ClientVetsFragment extends Fragment {

    private ClientVetsViewModel clientVetsViewModel;
    private ClientFragmentVetsBinding binding;
    private List<Vet> data;
    private VetAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clientVetsViewModel =
                new ViewModelProvider(this).get(ClientVetsViewModel.class);

        binding = ClientFragmentVetsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView layout = binding.idRVVets;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layout.setLayoutManager(layoutManager);

        data = clientVetsViewModel.getVets().getValue();

        adapter = new VetAdapter(getContext(), data);
        layout.setAdapter(adapter);


        clientVetsViewModel.getVets().observe(getViewLifecycleOwner(), new Observer<List<Vet>>() {
            @Override
            public void onChanged(List<Vet> vets) {
                data.clear();
                data.addAll(vets);
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