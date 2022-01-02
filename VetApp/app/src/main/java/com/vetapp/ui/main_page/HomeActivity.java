package com.vetapp.ui.main_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vetapp.R;
import com.vetapp.data.models.user.User;
import com.vetapp.data.models.user.UserType;
import com.vetapp.data.persistent.user.UserState;
import com.vetapp.databinding.ActivityHomeBinding;
import com.vetapp.ui.authentication.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureNavigationBar(binding);




        UserState.getUserLive().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(!UserState.isUserSignedIn()) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private void configureNavigationBar(ActivityHomeBinding binding) {
        UserType userType = UserState.getCurrentUser().getData().getType();

        if(userType == UserType.CLIENT){
            BottomNavigationView navView = binding.navViewClient;
            navView.setVisibility(View.VISIBLE);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.client_navigation_pets, R.id.client_navigation_vets, R.id.client_navigation_chat,R.id.client_navigation_settings)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.setGraph(R.navigation.mobile_navigation_client);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }else if(userType == UserType.VET){
            BottomNavigationView navView = binding.navViewVet;
            navView.setVisibility(View.VISIBLE);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.vet_navigation_profile, R.id.vet_navigation_schedule, R.id.vet_navigation_chat,R.id.vet_navigation_settings)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.setGraph(R.navigation.mobile_navigation_vet);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

}