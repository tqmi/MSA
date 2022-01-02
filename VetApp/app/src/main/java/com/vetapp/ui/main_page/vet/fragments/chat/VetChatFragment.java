package com.vetapp.ui.main_page.vet.fragments.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vetapp.databinding.VetFragmentChatBinding;

public class VetChatFragment extends Fragment {

    private VetChatViewModel vetChatViewModel;
    private VetFragmentChatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vetChatViewModel =
                new ViewModelProvider(this).get(VetChatViewModel.class);

        binding = VetFragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}