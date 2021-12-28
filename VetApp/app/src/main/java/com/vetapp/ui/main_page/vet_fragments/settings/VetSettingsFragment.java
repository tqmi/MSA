package com.vetapp.ui.main_page.vet_fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vetapp.business.login.LoginHandler;
import com.vetapp.databinding.VetFragmentSettingsBinding;

public class VetSettingsFragment extends Fragment {

    private VetsettingsViewModel notificationsViewModel;
    private VetFragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(VetsettingsViewModel.class);

        binding = VetFragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final Button signoutButton = binding.btnSignout;
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginHandler.logout();
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
