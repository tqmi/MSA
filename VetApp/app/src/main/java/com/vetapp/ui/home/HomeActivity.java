package com.vetapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.vetapp.R;
import com.vetapp.data.models.user.User;
import com.vetapp.data.models.user.UserType;
import com.vetapp.data.persistent.user.UserState;
import com.vetapp.databinding.ActivityHomeBinding;
import com.vetapp.ui.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = null;
        if(UserState.getCurrentUser().getData().getType() == UserType.CLIENT)
            navView = binding.navViewClient;
        else if(UserState.getCurrentUser().getData().getType() == UserType.VET)
            navView = binding.navViewVet;

        navView.setVisibility(View.VISIBLE);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);;
        if(UserState.getCurrentUser().getData().getType() == UserType.CLIENT)
            navController.setGraph(R.navigation.mobile_navigation_client);
        else if(UserState.getCurrentUser().getData().getType() == UserType.VET)
            navController.setGraph(R.navigation.mobile_navigation_vet);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(navView, navController);


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

}